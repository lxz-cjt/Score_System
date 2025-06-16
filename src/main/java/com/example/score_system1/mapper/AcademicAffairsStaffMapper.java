package com.example.score_system1.mapper;


import com.example.score_system1.entity.AcademicAffairsStaff;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AcademicAffairsStaffMapper {
    void insertAcademicAffairsStaff(AcademicAffairsStaff academicAffairsStaff);
    AcademicAffairsStaff selectAcademicAffairsStaffById(String asId);
    List<AcademicAffairsStaff> selectAllAcademicAffairsStaffs();
    void updateAcademicAffairsStaff(AcademicAffairsStaff academicAffairsStaff);
    void deleteAcademicAffairsStaff(String asId);
}