package com.example.score_system1.controller;

import com.example.score_system1.entity.AcademicAffairsStaff;
import com.example.score_system1.service.AcademicAffairsStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/academic-affairs-staff")
public class AcademicAffairsStaffController {

    @Autowired
    private AcademicAffairsStaffService academicAffairsStaffService;

    @PostMapping
    public ResponseEntity<String> insertAcademicAffairsStaff(@RequestBody AcademicAffairsStaff staff) {
        try {
            academicAffairsStaffService.insertAcademicAffairsStaff(staff);
            return ResponseEntity.ok("教务人员添加成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("添加教务人员失败: " + e.getMessage());
        }
    }

    @GetMapping("/{aasId}")
    public ResponseEntity<AcademicAffairsStaff> getAcademicAffairsStaffById(@PathVariable String aasId) {
        try {
            AcademicAffairsStaff staff = academicAffairsStaffService.getAcademicAffairsStaffById(aasId);
            if (staff == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
            return ResponseEntity.ok(staff);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<AcademicAffairsStaff>> getAllAcademicAffairsStaffs() {
        try {
            List<AcademicAffairsStaff> staffs = academicAffairsStaffService.getAllAcademicAffairsStaffs();
            return ResponseEntity.ok(staffs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping
    public ResponseEntity<String> updateAcademicAffairsStaff(@RequestBody AcademicAffairsStaff staff) {
        try {
            academicAffairsStaffService.updateAcademicAffairsStaff(staff);
            return ResponseEntity.ok("教务人员信息更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("更新教务人员信息失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{aasId}")
    public ResponseEntity<String> deleteAcademicAffairsStaff(@PathVariable String aasId) {
        try {
            academicAffairsStaffService.deleteAcademicAffairsStaff(aasId);
            return ResponseEntity.ok("教务人员删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("删除教务人员失败: " + e.getMessage());
        }
    }
}