package io.github.lxzcjt.scoresystem.service;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.exception.BusinessException;
import io.github.lxzcjt.scoresystem.dto.request.AppealCancelRequest;
import io.github.lxzcjt.scoresystem.dto.request.AppealCreateRequest;
import io.github.lxzcjt.scoresystem.dto.request.AppealProcessRequest;
import io.github.lxzcjt.scoresystem.dto.request.AppealReviewRequest;
import io.github.lxzcjt.scoresystem.dto.response.AppealVO;
import io.github.lxzcjt.scoresystem.entity.Appeal;
import io.github.lxzcjt.scoresystem.entity.AppealProcess;
import io.github.lxzcjt.scoresystem.entity.Course;
import io.github.lxzcjt.scoresystem.entity.CourseArranging;
import io.github.lxzcjt.scoresystem.entity.Student;
import io.github.lxzcjt.scoresystem.entity.Teacher;
import io.github.lxzcjt.scoresystem.enums.AppealStatus;
import io.github.lxzcjt.scoresystem.repository.AcademicAffairsStaffRepository;
import io.github.lxzcjt.scoresystem.repository.AppealProcessRepository;
import io.github.lxzcjt.scoresystem.repository.AppealRepository;
import io.github.lxzcjt.scoresystem.repository.CourseArrangingRepository;
import io.github.lxzcjt.scoresystem.repository.CourseRepository;
import io.github.lxzcjt.scoresystem.repository.ScoreRepository;
import io.github.lxzcjt.scoresystem.repository.StudentRepository;
import io.github.lxzcjt.scoresystem.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 成绩申诉服务
 *
 * 流程状态机：
 * 学生提交(PENDING) -> 教师处理(SUBMITTED_TO_ADMIN) -> 教务审核(APPROVED/REJECTED)
 * 学生在教师处理前可取消(CANCELLED)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppealService {

    /** 申诉进行中（不可重复提交）的状态集合 */
    private static final List<AppealStatus> ONGOING_STATUSES =
            List.of(AppealStatus.PENDING, AppealStatus.SUBMITTED_TO_ADMIN);

    private final AppealRepository appealRepository;
    private final AppealProcessRepository appealProcessRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AcademicAffairsStaffRepository staffRepository;
    private final CourseRepository courseRepository;
    private final CourseArrangingRepository arrangingRepository;
    private final ScoreRepository scoreRepository;

    /**
     * 学生发起申诉
     */
    @Transactional
    public AppealVO createAppeal(AppealCreateRequest request) {
        Student student = studentRepository.findByStudentId(request.studentId())
                .orElseThrow(() -> BusinessException.notFound("学生不存在: " + request.studentId()));
        if (!courseRepository.existsByCourseId(request.courseId())) {
            throw BusinessException.notFound("课程不存在: " + request.courseId());
        }
        if (!scoreRepository.existsByStudentIdAndCourseId(request.studentId(), request.courseId())) {
            throw BusinessException.badRequest("您没有该课程的成绩记录，无法发起申诉");
        }
        if (appealRepository.existsByStudentIdAndCourseIdAndAppealStatusIn(
                request.studentId(), request.courseId(), ONGOING_STATUSES)) {
            throw BusinessException.conflict("您已提交过该课程的申诉且正在处理中，请勿重复提交");
        }
        // 按课程安排自动分配授课教师
        CourseArranging arranging = arrangingRepository.findFirstByCourseId(request.courseId())
                .orElseThrow(() -> BusinessException.badRequest("该课程暂无授课教师安排，暂不能提交申诉"));

        Appeal appeal = Appeal.builder()
                .appealId(generateId())
                .studentId(request.studentId())
                .courseId(request.courseId())
                .teacherId(arranging.getTeacherId())
                .appealReason(request.reason())
                .appealTime(new Date())
                .appealStatus(AppealStatus.PENDING)
                .build();
        appealRepository.save(appeal);

        appendProcess(appeal, "学生提交申诉", student.getStudentId(), student.getStudentName(), request.reason());
        log.info("学生 {} 对课程 {} 发起申诉: {}", request.studentId(), request.courseId(), appeal.getAppealId());
        return toVO(appeal);
    }

    /**
     * 学生取消申诉（仅限本人且教师尚未处理）
     */
    @Transactional
    public AppealVO cancelAppeal(String appealId, AppealCancelRequest request) {
        Appeal appeal = findAppeal(appealId);
        if (!appeal.getStudentId().equals(request.studentId())) {
            throw BusinessException.forbidden("只能取消本人发起的申诉");
        }
        if (appeal.getAppealStatus() != AppealStatus.PENDING) {
            throw BusinessException.badRequest("教师已开始处理，当前状态不可取消");
        }
        Student student = studentRepository.findByStudentId(request.studentId()).orElse(null);

        appeal.setAppealStatus(AppealStatus.CANCELLED);
        appealRepository.save(appeal);

        appendProcess(appeal, "学生取消申诉", request.studentId(),
                student != null ? student.getStudentName() : request.studentId(), request.reason());
        log.info("申诉 {} 已被学生 {} 取消", appealId, request.studentId());
        return toVO(appeal);
    }

    /**
     * 教师处理申诉，提交处理意见后流转给教务审核
     */
    @Transactional
    public AppealVO processAppeal(String appealId, AppealProcessRequest request) {
        Appeal appeal = findAppeal(appealId);
        if (appeal.getAppealStatus() != AppealStatus.PENDING) {
            throw BusinessException.badRequest("该申诉当前状态不可处理");
        }
        Teacher teacher = teacherRepository.findByTeacherId(request.teacherId())
                .orElseThrow(() -> BusinessException.notFound("教师不存在: " + request.teacherId()));
        if (!teacher.getTeacherId().equals(appeal.getTeacherId())) {
            throw BusinessException.forbidden("该申诉未分配给您处理");
        }

        appeal.setAppealStatus(AppealStatus.SUBMITTED_TO_ADMIN);
        appealRepository.save(appeal);

        appendProcess(appeal, "教师处理", teacher.getTeacherId(), teacher.getTeacherName(), request.opinion());
        log.info("教师 {} 处理申诉 {}，流转至教务审核", request.teacherId(), appealId);
        return toVO(appeal);
    }

    /**
     * 教务审核申诉，给出最终结论
     */
    @Transactional
    public AppealVO reviewAppeal(String appealId, AppealReviewRequest request) {
        Appeal appeal = findAppeal(appealId);
        if (appeal.getAppealStatus() != AppealStatus.SUBMITTED_TO_ADMIN) {
            throw BusinessException.badRequest("该申诉当前状态不可审核");
        }
        var staff = staffRepository.findByStaffId(request.staffId())
                .orElseThrow(() -> BusinessException.notFound("教务人员不存在: " + request.staffId()));

        AppealStatus result = Boolean.TRUE.equals(request.approved())
                ? AppealStatus.APPROVED : AppealStatus.REJECTED;
        appeal.setAppealStatus(result);
        appeal.setStaffId(staff.getStaffId());
        appeal.setAppealResult(request.opinion());
        appeal.setResultTime(new Date());
        appealRepository.save(appeal);

        appendProcess(appeal, "教务审核（" + result.getLabel() + "）",
                staff.getStaffId(), staff.getStaffName(), request.opinion());
        log.info("教务 {} 审核申诉 {}，结论: {}", request.staffId(), appealId, result);
        return toVO(appeal);
    }

    // ==================== 查询 ====================

    /**
     * 学生端：我的申诉列表
     */
    public List<AppealVO> listStudentAppeals(String studentId) {
        if (!studentRepository.existsByStudentId(studentId)) {
            throw BusinessException.notFound("学生不存在: " + studentId);
        }
        return enrichBatch(appealRepository.findByStudentIdOrderByAppealTimeDesc(studentId));
    }

    /**
     * 教师端：分配给我的申诉（pendingOnly=true 时只看待处理）
     */
    public PageResult<AppealVO> listTeacherAppeals(String teacherId, boolean pendingOnly, int page, int size) {
        if (!teacherRepository.existsByTeacherId(teacherId)) {
            throw BusinessException.notFound("教师不存在: " + teacherId);
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "appealTime"));
        Page<Appeal> result = pendingOnly
                ? appealRepository.findByTeacherIdAndAppealStatusIn(teacherId, List.of(AppealStatus.PENDING), pageable)
                : appealRepository.findByTeacherId(teacherId, pageable);
        return PageResult.of(result, enrichBatch(result.getContent()));
    }

    /**
     * 教务端：全量申诉列表，支持按状态过滤
     */
    public PageResult<AppealVO> listAppeals(AppealStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "appealTime"));
        Page<Appeal> result = status != null
                ? appealRepository.findByAppealStatus(status, pageable)
                : appealRepository.findAll(pageable);
        return PageResult.of(result, enrichBatch(result.getContent()));
    }

    public AppealVO getAppeal(String appealId) {
        return toVO(findAppeal(appealId));
    }

    /**
     * 申诉处理过程（时间线）
     */
    public List<AppealProcess> getAppealProcesses(String appealId) {
        findAppeal(appealId);
        return appealProcessRepository.findByAppealIdOrderByProcessTimeAsc(appealId);
    }

    // ==================== 内部方法 ====================

    private Appeal findAppeal(String appealId) {
        return appealRepository.findByAppealId(appealId)
                .orElseThrow(() -> BusinessException.notFound("申诉不存在: " + appealId));
    }

    private void appendProcess(Appeal appeal, String step, String processorId, String processorName, String opinion) {
        AppealProcess process = AppealProcess.builder()
                .appealId(appeal.getAppealId())
                .processStep(step)
                .processorId(processorId)
                .processorName(processorName)
                .processTime(new Date())
                .processOpinion(opinion)
                .build();
        appealProcessRepository.save(process);
    }

    private AppealVO toVO(Appeal appeal) {
        return enrichBatch(List.of(appeal)).get(0);
    }

    /**
     * 批量组装 VO：一次性查出关联的学生/课程/教师，避免 N+1 查询
     */
    private List<AppealVO> enrichBatch(List<Appeal> appeals) {
        Set<String> studentIds = appeals.stream().map(Appeal::getStudentId).collect(Collectors.toSet());
        Set<String> courseIds = appeals.stream().map(Appeal::getCourseId).collect(Collectors.toSet());
        Set<String> teacherIds = appeals.stream().map(Appeal::getTeacherId)
                .filter(StringUtils::hasText).collect(Collectors.toSet());

        Map<String, Student> studentMap = studentRepository.findAllById(studentIds).stream()
                .collect(Collectors.toMap(Student::getStudentId, Function.identity()));
        Map<String, Course> courseMap = courseRepository.findAllById(courseIds).stream()
                .collect(Collectors.toMap(Course::getCourseId, Function.identity()));
        Map<String, Teacher> teacherMap = teacherIds.isEmpty()
                ? Map.of()
                : teacherRepository.findAllById(teacherIds).stream()
                        .collect(Collectors.toMap(Teacher::getTeacherId, Function.identity()));

        return appeals.stream().map(appeal -> {
            Student student = studentMap.get(appeal.getStudentId());
            Course course = courseMap.get(appeal.getCourseId());
            Teacher teacher = appeal.getTeacherId() != null ? teacherMap.get(appeal.getTeacherId()) : null;
            return new AppealVO(
                    appeal.getAppealId(),
                    appeal.getStudentId(),
                    student != null ? student.getStudentName() : null,
                    appeal.getCourseId(),
                    course != null ? course.getCourseName() : null,
                    appeal.getTeacherId(),
                    teacher != null ? teacher.getTeacherName() : null,
                    appeal.getAppealReason(),
                    appeal.getAppealTime(),
                    appeal.getAppealStatus(),
                    appeal.getAppealStatus().getLabel(),
                    appeal.getAppealResult(),
                    appeal.getResultTime());
        }).toList();
    }

    private String generateId() {
        return "AP" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}
