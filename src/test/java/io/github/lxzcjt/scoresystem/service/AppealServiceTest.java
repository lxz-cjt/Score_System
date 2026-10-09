package io.github.lxzcjt.scoresystem.service;

import io.github.lxzcjt.scoresystem.common.exception.BusinessException;
import io.github.lxzcjt.scoresystem.dto.request.AppealCancelRequest;
import io.github.lxzcjt.scoresystem.dto.request.AppealCreateRequest;
import io.github.lxzcjt.scoresystem.dto.request.AppealProcessRequest;
import io.github.lxzcjt.scoresystem.dto.request.AppealReviewRequest;
import io.github.lxzcjt.scoresystem.dto.response.AppealVO;
import io.github.lxzcjt.scoresystem.entity.AcademicAffairsStaff;
import io.github.lxzcjt.scoresystem.entity.Appeal;
import io.github.lxzcjt.scoresystem.entity.AppealProcess;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * AppealService 单元测试：申诉流程状态机
 */
@ExtendWith(MockitoExtension.class)
class AppealServiceTest {

    @Mock
    private AppealRepository appealRepository;
    @Mock
    private AppealProcessRepository appealProcessRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private AcademicAffairsStaffRepository staffRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CourseArrangingRepository arrangingRepository;
    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private AppealService appealService;

    private Student sampleStudent() {
        return Student.builder().studentId("2021001").studentName("张三").major("计算机").getCredit(45).password("x").build();
    }

    private Appeal pendingAppeal() {
        return Appeal.builder()
                .appealId("AP001").studentId("2021001").courseId("CS101").teacherId("T001")
                .appealReason("对成绩有异议").appealTime(new Date())
                .appealStatus(AppealStatus.PENDING)
                .build();
    }

    @Nested
    @DisplayName("学生发起申诉")
    class CreateAppeal {

        private final AppealCreateRequest request = new AppealCreateRequest("2021001", "CS101", "对成绩有异议");

        @Test
        @DisplayName("成功：状态为待处理，自动分配授课教师，写入处理记录")
        void success() {
            when(studentRepository.findByStudentId("2021001")).thenReturn(Optional.of(sampleStudent()));
            when(courseRepository.existsByCourseId("CS101")).thenReturn(true);
            when(scoreRepository.existsByStudentIdAndCourseId("2021001", "CS101")).thenReturn(true);
            when(appealRepository.existsByStudentIdAndCourseIdAndAppealStatusIn(anyString(), anyString(), anyList()))
                    .thenReturn(false);
            when(arrangingRepository.findFirstByCourseId("CS101")).thenReturn(Optional.of(
                    CourseArranging.builder().arrangingId("CA001").courseId("CS101").teacherId("T001")
                            .classTime("周一").classroom("101").build()));

            AppealVO result = appealService.createAppeal(request);

            assertEquals(AppealStatus.PENDING, result.appealStatus());
            assertEquals("T001", result.teacherId());
            verify(appealRepository).save(any(Appeal.class));
            verify(appealProcessRepository).save(any(AppealProcess.class));
        }

        @Test
        @DisplayName("没有该课程成绩 -> 400")
        void noScore() {
            when(studentRepository.findByStudentId("2021001")).thenReturn(Optional.of(sampleStudent()));
            when(courseRepository.existsByCourseId("CS101")).thenReturn(true);
            when(scoreRepository.existsByStudentIdAndCourseId("2021001", "CS101")).thenReturn(false);

            BusinessException e = assertThrows(BusinessException.class, () -> appealService.createAppeal(request));
            assertEquals(400, e.getErrorCode().getCode());
        }

        @Test
        @DisplayName("已有进行中的申诉 -> 409")
        void duplicate() {
            when(studentRepository.findByStudentId("2021001")).thenReturn(Optional.of(sampleStudent()));
            when(courseRepository.existsByCourseId("CS101")).thenReturn(true);
            when(scoreRepository.existsByStudentIdAndCourseId("2021001", "CS101")).thenReturn(true);
            when(appealRepository.existsByStudentIdAndCourseIdAndAppealStatusIn(anyString(), anyString(), anyList()))
                    .thenReturn(true);

            BusinessException e = assertThrows(BusinessException.class, () -> appealService.createAppeal(request));
            assertEquals(409, e.getErrorCode().getCode());
        }
    }

    @Nested
    @DisplayName("学生取消申诉")
    class CancelAppeal {

        @Test
        @DisplayName("只能取消本人的申诉 -> 403")
        void notOwner() {
            when(appealRepository.findByAppealId("AP001")).thenReturn(Optional.of(pendingAppeal()));

            AppealCancelRequest request = new AppealCancelRequest("2021999", "不想申诉了");
            BusinessException e = assertThrows(BusinessException.class,
                    () -> appealService.cancelAppeal("AP001", request));
            assertEquals(403, e.getErrorCode().getCode());
        }

        @Test
        @DisplayName("教师已处理后不可取消 -> 400")
        void wrongStatus() {
            Appeal appeal = pendingAppeal();
            appeal.setAppealStatus(AppealStatus.SUBMITTED_TO_ADMIN);
            when(appealRepository.findByAppealId("AP001")).thenReturn(Optional.of(appeal));

            AppealCancelRequest request = new AppealCancelRequest("2021001", "不想申诉了");
            BusinessException e = assertThrows(BusinessException.class,
                    () -> appealService.cancelAppeal("AP001", request));
            assertEquals(400, e.getErrorCode().getCode());
        }

        @Test
        @DisplayName("成功：状态变为已取消")
        void success() {
            Appeal appeal = pendingAppeal();
            when(appealRepository.findByAppealId("AP001")).thenReturn(Optional.of(appeal));
            when(studentRepository.findByStudentId("2021001")).thenReturn(Optional.of(sampleStudent()));

            AppealVO result = appealService.cancelAppeal("AP001", new AppealCancelRequest("2021001", "已私下沟通"));

            assertEquals(AppealStatus.CANCELLED, result.appealStatus());
            verify(appealProcessRepository).save(any(AppealProcess.class));
        }
    }

    @Nested
    @DisplayName("教师处理申诉")
    class ProcessAppeal {

        private final AppealProcessRequest request = new AppealProcessRequest("T001", "已重新核对试卷");

        @Test
        @DisplayName("成功：流转至待教务审核")
        void success() {
            Appeal appeal = pendingAppeal();
            when(appealRepository.findByAppealId("AP001")).thenReturn(Optional.of(appeal));
            when(teacherRepository.findByTeacherId("T001")).thenReturn(Optional.of(
                    Teacher.builder().teacherId("T001").teacherName("李教授").college("计算机学院").password("x").build()));

            AppealVO result = appealService.processAppeal("AP001", request);

            assertEquals(AppealStatus.SUBMITTED_TO_ADMIN, result.appealStatus());
            verify(appealProcessRepository).save(any(AppealProcess.class));
        }

        @Test
        @DisplayName("非指定教师处理 -> 403")
        void notAssignedTeacher() {
            Appeal appeal = pendingAppeal();
            when(appealRepository.findByAppealId("AP001")).thenReturn(Optional.of(appeal));
            when(teacherRepository.findByTeacherId("T002")).thenReturn(Optional.of(
                    Teacher.builder().teacherId("T002").teacherName("张副教授").college("软件学院").password("x").build()));

            AppealProcessRequest otherTeacher = new AppealProcessRequest("T002", "我来处理");
            BusinessException e = assertThrows(BusinessException.class,
                    () -> appealService.processAppeal("AP001", otherTeacher));
            assertEquals(403, e.getErrorCode().getCode());
        }

        @Test
        @DisplayName("状态非待处理 -> 400")
        void wrongStatus() {
            Appeal appeal = pendingAppeal();
            appeal.setAppealStatus(AppealStatus.APPROVED);
            when(appealRepository.findByAppealId("AP001")).thenReturn(Optional.of(appeal));

            BusinessException e = assertThrows(BusinessException.class,
                    () -> appealService.processAppeal("AP001", request));
            assertEquals(400, e.getErrorCode().getCode());
        }
    }

    @Nested
    @DisplayName("教务审核申诉")
    class ReviewAppeal {

        private Appeal submittedAppeal() {
            Appeal appeal = pendingAppeal();
            appeal.setAppealStatus(AppealStatus.SUBMITTED_TO_ADMIN);
            return appeal;
        }

        @Test
        @DisplayName("审核通过：状态已通过并记录结果时间")
        void approved() {
            Appeal appeal = submittedAppeal();
            when(appealRepository.findByAppealId("AP001")).thenReturn(Optional.of(appeal));
            when(staffRepository.findByStaffId("AAS001")).thenReturn(Optional.of(
                    AcademicAffairsStaff.builder().staffId("AAS001").staffName("管理员一").password("x").build()));

            AppealVO result = appealService.reviewAppeal("AP001",
                    new AppealReviewRequest("AAS001", true, "同意复核结果"));

            assertEquals(AppealStatus.APPROVED, result.appealStatus());
            assertNotNull(result.resultTime());
            assertEquals("AAS001", appeal.getStaffId());
        }

        @Test
        @DisplayName("审核拒绝：状态已拒绝")
        void rejected() {
            Appeal appeal = submittedAppeal();
            when(appealRepository.findByAppealId("AP001")).thenReturn(Optional.of(appeal));
            when(staffRepository.findByStaffId("AAS001")).thenReturn(Optional.of(
                    AcademicAffairsStaff.builder().staffId("AAS001").staffName("管理员一").password("x").build()));

            AppealVO result = appealService.reviewAppeal("AP001",
                    new AppealReviewRequest("AAS001", false, "维持原成绩"));

            assertEquals(AppealStatus.REJECTED, result.appealStatus());
        }

        @Test
        @DisplayName("状态非待审核 -> 400")
        void wrongStatus() {
            when(appealRepository.findByAppealId("AP001")).thenReturn(Optional.of(pendingAppeal()));

            AppealReviewRequest request = new AppealReviewRequest("AAS001", true, "同意");
            BusinessException e = assertThrows(BusinessException.class,
                    () -> appealService.reviewAppeal("AP001", request));
            assertEquals(400, e.getErrorCode().getCode());
        }
    }
}
