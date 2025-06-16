package com.example.score_system1.service;

import com.example.score_system1.entity.Appeal;
import com.example.score_system1.mapper.AppealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppealService {

    @Autowired
    private AppealMapper appealMapper;

    public void insertAppeal(Appeal appeal) {
        appealMapper.insertAppeal(appeal);
    }

    public Appeal getAppealById(String apId) {
        return appealMapper.selectAppealById(apId);
    }

    public List<Appeal> getAppealsByStudentId(String stId) {
        return appealMapper.selectAppealsByStudentId(stId);
    }

    public void updateAppeal(Appeal appeal) {
        appealMapper.updateAppeal(appeal);
    }

    public void deleteAppeal(String apId) {
        appealMapper.deleteAppeal(apId);
    }
}