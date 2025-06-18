package com.example.score_system1.controller;

import com.example.score_system1.dto.ApiResponse;
import com.example.score_system1.entity.AcademicAffairsStaff;
import com.example.score_system1.service.AcademicAffairsStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic-affairs-staff")
public class AcademicAffairsStaffController {

    @Autowired
    private AcademicAffairsStaffService academicAffairsStaffService;

    @PostMapping
    public ApiResponse<String> insertAcademicAffairsStaff(@RequestBody AcademicAffairsStaff staff) {
        try {
            academicAffairsStaffService.insertAcademicAffairsStaff(staff);
            return ApiResponse.success("教务人员添加成功");
        } catch (Exception e) {
            return ApiResponse.error("添加教务人员失败: " + e.getMessage());
        }
    }

    @GetMapping("/{aasId}")
    public ApiResponse<AcademicAffairsStaff> getAcademicAffairsStaffById(@PathVariable String aasId) {
        try {
            AcademicAffairsStaff staff = academicAffairsStaffService.getAcademicAffairsStaffById(aasId);
            if (staff == null) {
                return ApiResponse.error("教务人员不存在", 404);
            }
            return ApiResponse.success("获取教务人员信息成功", staff);
        } catch (Exception e) {
            return ApiResponse.error("获取教务人员信息失败: " + e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<AcademicAffairsStaff>> getAllAcademicAffairsStaffs() {
        try {
            List<AcademicAffairsStaff> staffs = academicAffairsStaffService.getAllAcademicAffairsStaffs();
            return ApiResponse.success("获取教务人员列表成功", staffs);
        } catch (Exception e) {
            return ApiResponse.error("获取教务人员列表失败: " + e.getMessage());
        }
    }

    @PutMapping
    public ApiResponse<String> updateAcademicAffairsStaff(@RequestBody AcademicAffairsStaff staff) {
        try {
            academicAffairsStaffService.updateAcademicAffairsStaff(staff);
            return ApiResponse.success("教务人员信息更新成功");
        } catch (Exception e) {
            return ApiResponse.error("更新教务人员信息失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{aasId}")
    public ApiResponse<String> deleteAcademicAffairsStaff(@PathVariable String aasId) {
        try {
            academicAffairsStaffService.deleteAcademicAffairsStaff(aasId);
            return ApiResponse.success("教务人员删除成功");
        } catch (Exception e) {
            return ApiResponse.error("删除教务人员失败: " + e.getMessage());
        }
    }
}