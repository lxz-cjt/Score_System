package com.example.score_system1.controller;

import com.example.score_system1.dto.ApiResponse;
import com.example.score_system1.entity.AcademicAffairsStaff;
import com.example.score_system1.entity.Student;
import com.example.score_system1.entity.Teacher;
import com.example.score_system1.enums.UserRole;
import com.example.score_system1.service.AcademicAffairsStaffService;
import com.example.score_system1.service.StudentService;
import com.example.score_system1.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 * 提供统一的用户管理功能，包括学生、教师、教务人员的管理
 */
@RestController
@RequestMapping("/api/user-management")
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserManagementController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AcademicAffairsStaffService academicAffairsStaffService;

    // ==================== 学生管理 ====================

    @GetMapping("/students")
    @Operation(summary = "获取所有学生", description = "获取系统中所有学生信息")
    public ApiResponse<List<Student>> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            return ApiResponse.success("获取学生列表成功", students);
        } catch (Exception e) {
            return ApiResponse.error("获取学生列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/students/{stId}")
    @Operation(summary = "根据学号获取学生", description = "根据学号获取特定学生信息")
    public ApiResponse<Student> getStudentById(
            @Parameter(description = "学号") @PathVariable String stId) {
        try {
            Student student = studentService.getStudentById(stId);
            if (student != null) {
                return ApiResponse.success("获取学生信息成功", student);
            } else {
                return ApiResponse.error("学生不存在", 404);
            }
        } catch (Exception e) {
            return ApiResponse.error("获取学生信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/students")
    @Operation(summary = "新增学生", description = "新增学生信息")
    public ApiResponse<Student> createStudent(@Valid @RequestBody Student student) {
        try {
            Student savedStudent = studentService.insertStudent(student);
            return ApiResponse.success("新增学生成功", savedStudent);
        } catch (Exception e) {
            return ApiResponse.error("新增学生失败: " + e.getMessage());
        }
    }

    @PutMapping("/students/{stId}")
    @Operation(summary = "更新学生信息", description = "更新指定学生的信息")
    public ApiResponse<Student> updateStudent(
            @Parameter(description = "学号") @PathVariable String stId,
            @Valid @RequestBody Student student) {
        try {
            student.setStudentId(stId);
            Student updatedStudent = studentService.updateStudent(student);
            return ApiResponse.success("更新学生信息成功", updatedStudent);
        } catch (Exception e) {
            return ApiResponse.error("更新学生信息失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/students/{stId}")
    @Operation(summary = "删除学生", description = "根据学号删除学生")
    public ApiResponse<Void> deleteStudent(
            @Parameter(description = "学号") @PathVariable String stId) {
        try {
            studentService.deleteStudent(stId);
            return ApiResponse.success("删除学生成功");
        } catch (Exception e) {
            return ApiResponse.error("删除学生失败: " + e.getMessage());
        }
    }

    @GetMapping("/students/search")
    @Operation(summary = "搜索学生", description = "根据专业或姓名搜索学生")
    public ApiResponse<List<Student>> searchStudents(
            @Parameter(description = "专业") @RequestParam(required = false) String major,
            @Parameter(description = "姓名关键字") @RequestParam(required = false) String name) {
        try {
            List<Student> students;
            if (major != null && !major.isEmpty()) {
                students = studentService.getStudentsByMajor(major);
            } else if (name != null && !name.isEmpty()) {
                students = studentService.getStudentsByNameContaining(name);
            } else {
                students = studentService.getAllStudents();
            }
            return ApiResponse.success("搜索学生成功", students);
        } catch (Exception e) {
            return ApiResponse.error("搜索学生失败: " + e.getMessage());
        }
    }

    // ==================== 教师管理 ====================

    @GetMapping("/teachers")
    @Operation(summary = "获取所有教师", description = "获取系统中所有教师信息")
    public ApiResponse<List<Teacher>> getAllTeachers() {
        try {
            List<Teacher> teachers = teacherService.getAllTeachers();
            return ApiResponse.success("获取教师列表成功", teachers);
        } catch (Exception e) {
            return ApiResponse.error("获取教师列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/teachers/{teId}")
    @Operation(summary = "根据教师编号获取教师", description = "根据教师编号获取特定教师信息")
    public ApiResponse<Teacher> getTeacherById(
            @Parameter(description = "教师编号") @PathVariable String teId) {
        try {
            Teacher teacher = teacherService.getTeacherById(teId);
            if (teacher != null) {
                return ApiResponse.success("获取教师信息成功", teacher);
            } else {
                return ApiResponse.error("教师不存在", 404);
            }
        } catch (Exception e) {
            return ApiResponse.error("获取教师信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/teachers")
    @Operation(summary = "新增教师", description = "新增教师信息")
    public ApiResponse<Teacher> createTeacher(@Valid @RequestBody Teacher teacher) {
        try {
            Teacher savedTeacher = teacherService.insertTeacher(teacher);
            return ApiResponse.success("新增教师成功", savedTeacher);
        } catch (Exception e) {
            return ApiResponse.error("新增教师失败: " + e.getMessage());
        }
    }

    @PutMapping("/teachers/{teId}")
    @Operation(summary = "更新教师信息", description = "更新指定教师的信息")
    public ApiResponse<Teacher> updateTeacher(
            @Parameter(description = "教师编号") @PathVariable String teId,
            @Valid @RequestBody Teacher teacher) {
        try {
            teacher.setTeacherId(teId);
            Teacher updatedTeacher = teacherService.updateTeacher(teacher);
            return ApiResponse.success("更新教师信息成功", updatedTeacher);
        } catch (Exception e) {
            return ApiResponse.error("更新教师信息失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/teachers/{teId}")
    @Operation(summary = "删除教师", description = "根据教师编号删除教师")
    public ApiResponse<Void> deleteTeacher(
            @Parameter(description = "教师编号") @PathVariable String teId) {
        try {
            teacherService.deleteTeacher(teId);
            return ApiResponse.success("删除教师成功");
        } catch (Exception e) {
            return ApiResponse.error("删除教师失败: " + e.getMessage());
        }
    }

    // ==================== 教务人员管理 ====================

    @GetMapping("/staff")
    @Operation(summary = "获取所有教务人员", description = "获取系统中所有教务人员信息")
    public ApiResponse<List<AcademicAffairsStaff>> getAllStaff() {
        try {
            List<AcademicAffairsStaff> staffList = academicAffairsStaffService.getAllAcademicAffairsStaffs();
            return ApiResponse.success("获取教务人员列表成功", staffList);
        } catch (Exception e) {
            return ApiResponse.error("获取教务人员列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/staff/{aasId}")
    @Operation(summary = "根据工号获取教务人员", description = "根据工号获取特定教务人员信息")
    public ApiResponse<AcademicAffairsStaff> getStaffById(
            @Parameter(description = "工号") @PathVariable String aasId) {
        try {
            AcademicAffairsStaff staff = academicAffairsStaffService.getAcademicAffairsStaffById(aasId);
            if (staff != null) {
                return ApiResponse.success("获取教务人员信息成功", staff);
            } else {
                return ApiResponse.error("教务人员不存在", 404);
            }
        } catch (Exception e) {
            return ApiResponse.error("获取教务人员信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/staff")
    @Operation(summary = "新增教务人员", description = "新增教务人员信息")
    public ApiResponse<AcademicAffairsStaff> createStaff(@Valid @RequestBody AcademicAffairsStaff staff) {
        try {
            AcademicAffairsStaff savedStaff = academicAffairsStaffService.insertAcademicAffairsStaff(staff);
            return ApiResponse.success("新增教务人员成功", savedStaff);
        } catch (Exception e) {
            return ApiResponse.error("新增教务人员失败: " + e.getMessage());
        }
    }

    @PutMapping("/staff/{aasId}")
    @Operation(summary = "更新教务人员信息", description = "更新指定教务人员的信息")
    public ApiResponse<AcademicAffairsStaff> updateStaff(
            @Parameter(description = "工号") @PathVariable String aasId,
            @Valid @RequestBody AcademicAffairsStaff staff) {
        try {
            staff.setAcademicAffairsStaffId(aasId);
            AcademicAffairsStaff updatedStaff = academicAffairsStaffService.updateAcademicAffairsStaff(staff);
            return ApiResponse.success("更新教务人员信息成功", updatedStaff);
        } catch (Exception e) {
            return ApiResponse.error("更新教务人员信息失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/staff/{aasId}")
    @Operation(summary = "删除教务人员", description = "根据工号删除教务人员")
    public ApiResponse<Void> deleteStaff(
            @Parameter(description = "工号") @PathVariable String aasId) {
        try {
            academicAffairsStaffService.deleteAcademicAffairsStaff(aasId);
            return ApiResponse.success("删除教务人员成功");
        } catch (Exception e) {
            return ApiResponse.error("删除教务人员失败: " + e.getMessage());
        }
    }
} 