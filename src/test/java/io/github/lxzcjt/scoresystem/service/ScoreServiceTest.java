package io.github.lxzcjt.scoresystem.service;

import io.github.lxzcjt.scoresystem.common.exception.BusinessException;
import io.github.lxzcjt.scoresystem.dto.request.ScoreSaveRequest;
import io.github.lxzcjt.scoresystem.dto.request.ScoreUpdateRequest;
import io.github.lxzcjt.scoresystem.dto.response.GpaVO;
import io.github.lxzcjt.scoresystem.dto.response.ScoreVO;
import io.github.lxzcjt.scoresystem.entity.Course;
import io.github.lxzcjt.scoresystem.entity.Score;
import io.github.lxzcjt.scoresystem.entity.ScoreLog;
import io.github.lxzcjt.scoresystem.repository.CourseRepository;
import io.github.lxzcjt.scoresystem.repository.ScoreLogRepository;
import io.github.lxzcjt.scoresystem.repository.ScoreRepository;
import io.github.lxzcjt.scoresystem.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * ScoreService 单元测试
 */
@ExtendWith(MockitoExtension.class)
class ScoreServiceTest {

    @Mock
    private ScoreRepository scoreRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private ScoreLogRepository scoreLogRepository;

    @InjectMocks
    private ScoreService scoreService;

    @Nested
    @DisplayName("总评成绩计算")
    class TotalScoreCalculation {

        @Test
        @DisplayName("平时30% + 考试70%，四舍五入")
        void weightedAverage() {
            assertEquals(87, ScoreService.calculateTotalScore(85, 88));   // 25.5 + 61.6 = 87.1
            assertEquals(100, ScoreService.calculateTotalScore(100, 100));
            assertEquals(0, ScoreService.calculateTotalScore(0, 0));
            assertEquals(60, ScoreService.calculateTotalScore(60, 60));
        }
    }

    @Nested
    @DisplayName("学分获得条件与补考判定")
    class CreditCondition {

        @Test
        @DisplayName(">=90 优秀，>=60 通过，其余不通过")
        void conditions() {
            assertEquals("优秀", ScoreService.resolveCreditCondition(95));
            assertEquals("优秀", ScoreService.resolveCreditCondition(90));
            assertEquals("通过", ScoreService.resolveCreditCondition(89));
            assertEquals("通过", ScoreService.resolveCreditCondition(60));
            assertEquals("不通过", ScoreService.resolveCreditCondition(59));
        }

        @Test
        @DisplayName("总评 < 60 需要补考")
        void makeUpExam() {
            assertTrue(ScoreService.needMakeUpExam(59));
            assertTrue(ScoreService.needMakeUpExam(0));
            assertTrue(!ScoreService.needMakeUpExam(60));
            assertTrue(!ScoreService.needMakeUpExam(100));
        }
    }

    @Nested
    @DisplayName("绩点换算")
    class GradePoint {

        @Test
        @DisplayName("分数段边界")
        void boundaries() {
            assertEquals(4.0, ScoreService.gradePointOf(90));
            assertEquals(4.0, ScoreService.gradePointOf(100));
            assertEquals(3.7, ScoreService.gradePointOf(85));
            assertEquals(1.0, ScoreService.gradePointOf(60));
            assertEquals(0.0, ScoreService.gradePointOf(59));
        }
    }

    @Nested
    @DisplayName("成绩录入")
    class CreateScore {

        private final ScoreSaveRequest request = new ScoreSaveRequest("2021001", "CS101", 85, 88, "T001");

        @Test
        @DisplayName("成功：自动计算总评并写入日志")
        void success() {
            when(studentRepository.existsByStudentId("2021001")).thenReturn(true);
            when(courseRepository.existsByCourseId("CS101")).thenReturn(true);
            when(scoreRepository.existsByStudentIdAndCourseId("2021001", "CS101")).thenReturn(false);
            when(studentRepository.findAllById(any())).thenReturn(List.of());
            when(courseRepository.findAllById(any())).thenReturn(List.of());

            ScoreVO result = scoreService.createScore(request);

            assertEquals(87, result.totalScore());
            assertEquals("通过", result.creditGainCondition());
            assertEquals(false, result.makeUpExam());
            verify(scoreRepository).save(any(Score.class));
            verify(scoreLogRepository).save(any(ScoreLog.class));
        }

        @Test
        @DisplayName("重复录入同一学生同一课程 -> 409")
        void duplicate() {
            when(studentRepository.existsByStudentId("2021001")).thenReturn(true);
            when(courseRepository.existsByCourseId("CS101")).thenReturn(true);
            when(scoreRepository.existsByStudentIdAndCourseId("2021001", "CS101")).thenReturn(true);

            BusinessException e = assertThrows(BusinessException.class, () -> scoreService.createScore(request));
            assertEquals(409, e.getErrorCode().getCode());
            verify(scoreRepository, never()).save(any());
        }

        @Test
        @DisplayName("学生不存在 -> 404")
        void studentNotFound() {
            when(studentRepository.existsByStudentId("2021001")).thenReturn(false);

            BusinessException e = assertThrows(BusinessException.class, () -> scoreService.createScore(request));
            assertEquals(404, e.getErrorCode().getCode());
            verify(scoreRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("成绩修改")
    class UpdateScore {

        @Test
        @DisplayName("成绩记录不存在 -> 404")
        void notFound() {
            when(scoreRepository.findByScoreId("SC999")).thenReturn(Optional.empty());

            ScoreUpdateRequest request = new ScoreUpdateRequest(80, 90, "T001");
            BusinessException e = assertThrows(BusinessException.class,
                    () -> scoreService.updateScore("SC999", request));
            assertEquals(404, e.getErrorCode().getCode());
        }

        @Test
        @DisplayName("成功：总评重算并记录修改前后日志")
        void success() {
            Score existing = Score.builder()
                    .scoreId("SC001").studentId("2021001").courseId("CS101")
                    .dailyScore(50).examScore(50).totalScore(50)
                    .creditGainCondition("不通过").makeUpExam(true)
                    .build();
            when(scoreRepository.findByScoreId("SC001")).thenReturn(Optional.of(existing));
            when(studentRepository.findAllById(any())).thenReturn(List.of());
            when(courseRepository.findAllById(any())).thenReturn(List.of());

            ScoreVO result = scoreService.updateScore("SC001", new ScoreUpdateRequest(90, 90, "T001"));

            assertEquals(90, result.totalScore());
            assertEquals("优秀", result.creditGainCondition());
            verify(scoreLogRepository).save(any(ScoreLog.class));
        }
    }

    @Nested
    @DisplayName("GPA 计算")
    class GpaCalculation {

        @Test
        @DisplayName("按课程学分加权计算 GPA")
        void weightedGpa() {
            // CS101 4学分 90分(4.0)，CS102 2学分 60分(1.0) -> GPA = (4*4.0 + 2*1.0) / 6 = 3.0
            when(studentRepository.existsByStudentId("2021001")).thenReturn(true);
            when(scoreRepository.findByStudentId("2021001")).thenReturn(List.of(
                    Score.builder().scoreId("S1").studentId("2021001").courseId("CS101").totalScore(90).build(),
                    Score.builder().scoreId("S2").studentId("2021001").courseId("CS102").totalScore(60).build()));
            when(courseRepository.findAllById(any())).thenReturn(List.of(
                    Course.builder().courseId("CS101").courseName("课程1").courseCredit((short) 4).build(),
                    Course.builder().courseId("CS102").courseName("课程2").courseCredit((short) 2).build()));
            when(scoreRepository.findAverageScoreByStudentId("2021001")).thenReturn(75.0);

            GpaVO summary = scoreService.getStudentScoreSummary("2021001");

            assertEquals(3.0, summary.gpa());
            assertEquals(6.0, summary.totalCredits());
            assertEquals(2, summary.scoreCount());
            assertNotNull(summary.averageScore());
        }
    }
}
