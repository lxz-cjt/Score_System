package io.github.lxzcjt.scoresystem;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API 集成测试：基于 H2 内存库 + Flyway 示例数据，覆盖核心业务流程
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("登录：种子用户 2021001/123456 登录成功（验证 BCrypt 密码校验）")
    void loginSuccess() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", "2021001", "password", "123456"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.user.role").value("student"))
                .andExpect(jsonPath("$.data.user.name").value("张三"))
                .andExpect(jsonPath("$.data.token").isNotEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("登录：密码错误返回 401")
    void loginWrongPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", "2021001", "password", "wrong-password"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    @Order(3)
    @DisplayName("学生成绩：2021001 有 3 门成绩且关联出课程名称")
    void studentScores() throws Exception {
        mockMvc.perform(get("/api/scores/student/2021001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].courseName").isNotEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("参数校验：成绩超出 0-100 返回 400")
    void createScoreValidation() throws Exception {
        mockMvc.perform(post("/api/scores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "studentId", "2021006",
                                "courseId", "CS101",
                                "dailyScore", 150,
                                "examScore", 90,
                                "operatorId", "T001"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @Order(5)
    @DisplayName("成绩录入：成功后总评自动计算，重复录入返回 409")
    void createAndDuplicateScore() throws Exception {
        Map<String, Object> body = Map.of(
                "studentId", "2021006",
                "courseId", "CS101",
                "dailyScore", 80,
                "examScore", 90,
                "operatorId", "T001");

        // 80*0.3 + 90*0.7 = 87
        mockMvc.perform(post("/api/scores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalScore").value(87))
                .andExpect(jsonPath("$.data.creditGainCondition").value("通过"))
                .andExpect(jsonPath("$.data.makeUpExam").value(false));

        mockMvc.perform(post("/api/scores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409));
    }

    @Test
    @Order(6)
    @DisplayName("申诉全流程：学生提交 -> 教师处理 -> 教务审核通过")
    void appealWorkflow() throws Exception {
        // 学生 2021003 对 CS101（授课教师 T001）发起申诉
        MvcResult createResult = mockMvc.perform(post("/api/appeals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "studentId", "2021003",
                                "courseId", "CS101",
                                "reason", "对总评成绩有异议，申请重新核查"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.appealStatus").value("PENDING"))
                .andExpect(jsonPath("$.data.teacherId").value("T001"))
                .andReturn();

        JsonNode created = objectMapper.readTree(createResult.getResponse().getContentAsString());
        String appealId = created.path("data").path("appealId").asText();

        // 重复提交 -> 409
        mockMvc.perform(post("/api/appeals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "studentId", "2021003",
                                "courseId", "CS101",
                                "reason", "重复提交"))))
                .andExpect(status().isConflict());

        // 教师 T001 处理 -> 待教务审核
        mockMvc.perform(put("/api/appeals/{id}/process", appealId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "teacherId", "T001",
                                "opinion", "已重新核对试卷，成绩无误"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.appealStatus").value("SUBMITTED_TO_ADMIN"));

        // 教务 AAS001 审核通过 -> 已通过
        mockMvc.perform(put("/api/appeals/{id}/review", appealId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "staffId", "AAS001",
                                "approved", true,
                                "opinion", "同意复核结果"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.appealStatus").value("APPROVED"));

        // 处理过程时间线应有 3 条记录
        mockMvc.perform(get("/api/appeals/{id}/processes", appealId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)));
    }

    @Test
    @Order(7)
    @DisplayName("分页查询：学生列表第 1 页 5 条，总数 10")
    void studentPagination() throws Exception {
        mockMvc.perform(get("/api/students")
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(10))
                .andExpect(jsonPath("$.data.records", hasSize(5)))
                .andExpect(jsonPath("$.data.page").value(1));
    }

    @Test
    @Order(8)
    @DisplayName("教务 Dashboard：统计数字与种子数据一致")
    void adminDashboard() throws Exception {
        mockMvc.perform(get("/api/dashboard/admin/AAS001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalStudents").value(10))
                .andExpect(jsonPath("$.data.totalTeachers").value(8))
                .andExpect(jsonPath("$.data.totalCourses").value(10))
                .andExpect(jsonPath("$.data.scoreDistribution", hasSize(5)))
                .andExpect(jsonPath("$.data.courseAverages", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @Order(9)
    @DisplayName("资源不存在：查询不存在的学生返回 404")
    void studentNotFound() throws Exception {
        mockMvc.perform(get("/api/students/9999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }
}
