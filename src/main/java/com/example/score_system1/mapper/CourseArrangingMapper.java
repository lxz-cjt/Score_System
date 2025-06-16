package com.example.score_system1.mapper;


import com.example.score_system1.entity.CourseArranging;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseArrangingMapper {
    void insertCourseArranging(CourseArranging courseArranging);
    CourseArranging selectCourseArrangingById(String caId);
    List<CourseArranging> selectCourseArrangingsByCourseId(String cId);
    List<CourseArranging> selectCourseArrangingsByTeacherId(String teId);
    void updateCourseArranging(CourseArranging courseArranging);
    void deleteCourseArranging(String caId);
}