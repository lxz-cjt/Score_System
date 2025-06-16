package com.example.score_system1.mapper;


import com.example.score_system1.entity.Appeal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AppealMapper {
    void insertAppeal(Appeal appeal);
    Appeal selectAppealById(String apId);
    List<Appeal> selectAppealsByStudentId(String stId);
    void updateAppeal(Appeal appeal);
    void deleteAppeal(String apId);
}