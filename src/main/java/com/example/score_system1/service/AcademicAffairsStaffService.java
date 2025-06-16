package com.example.score_system1.service;

import com.example.score_system1.entity.AcademicAffairsStaff;
import com.example.score_system1.mapper.AcademicAffairsStaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademicAffairsStaffService {

    @Autowired
    private AcademicAffairsStaffMapper academicAffairsStaffMapper;

    public void insertAcademicAffairsStaff(AcademicAffairsStaff academicAffairsStaff) {
        academicAffairsStaffMapper.insertAcademicAffairsStaff(academicAffairsStaff);
    }

    public AcademicAffairsStaff getAcademicAffairsStaffById(String aasId) {
        return academicAffairsStaffMapper.selectAcademicAffairsStaffById(aasId);
    }

    public List<AcademicAffairsStaff> getAllAcademicAffairsStaffs() {
        return academicAffairsStaffMapper.selectAllAcademicAffairsStaffs();
    }

    public void updateAcademicAffairsStaff(AcademicAffairsStaff academicAffairsStaff) {
        academicAffairsStaffMapper.updateAcademicAffairsStaff(academicAffairsStaff);
    }

    public void deleteAcademicAffairsStaff(String aasId) {
        academicAffairsStaffMapper.deleteAcademicAffairsStaff(aasId);
    }
}