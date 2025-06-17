package com.example.score_system1.service;

import com.example.score_system1.entity.CourseArranging;
import com.example.score_system1.mapper.CourseArrangingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseArrangingService {

    @Autowired
    private CourseArrangingMapper courseArrangingMapper;

    public void insertCourseArranging(CourseArranging courseArranging) {
        courseArrangingMapper.insertCourseArranging(courseArranging);
    }

    public CourseArranging getCourseArrangingById(String caId) {
        return courseArrangingMapper.selectCourseArrangingById(caId);
    }

    public List<CourseArranging> getCourseArrangingsByCourseId(String cId) {
        return courseArrangingMapper.selectCourseArrangingsByCourseId(cId);
    }

    public List<CourseArranging> getCourseArrangingsByTeacherId(String teId) {
        return courseArrangingMapper.selectCourseArrangingsByTeacherId(teId);
    }

    public void updateCourseArranging(CourseArranging courseArranging) {
        courseArrangingMapper.updateCourseArranging(courseArranging);
    }

    public void deleteCourseArranging(String caId) {
        courseArrangingMapper.deleteCourseArranging(caId);
    }
}