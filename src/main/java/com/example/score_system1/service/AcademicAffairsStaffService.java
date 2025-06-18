package com.example.score_system1.service;

import com.example.score_system1.entity.AcademicAffairsStaff;
import com.example.score_system1.repository.AcademicAffairsStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 教务管理人员业务服务类
 * 提供教务管理人员相关的业务逻辑处理
 */
@Service
public class AcademicAffairsStaffService {

    @Autowired
    private AcademicAffairsStaffRepository academicAffairsStaffRepository;

    /**
     * 新增教务管理人员
     * @param academicAffairsStaff 教务管理人员信息
     * @return 保存后的教务管理人员信息
     */
    public AcademicAffairsStaff insertAcademicAffairsStaff(AcademicAffairsStaff academicAffairsStaff) {
        return academicAffairsStaffRepository.save(academicAffairsStaff);
    }

    /**
     * 根据工号获取教务管理人员信息
     * @param aasId 工号
     * @return 教务管理人员信息，不存在则返回null
     */
    public AcademicAffairsStaff getAcademicAffairsStaffById(String aasId) {
        Optional<AcademicAffairsStaff> staff = academicAffairsStaffRepository.findByAcademicAffairsStaffId(aasId);
        return staff.orElse(null);
    }

    /**
     * 获取所有教务管理人员列表
     * @return 教务管理人员列表
     */
    public List<AcademicAffairsStaff> getAllAcademicAffairsStaffs() {
        return academicAffairsStaffRepository.findAll();
    }

    /**
     * 更新教务管理人员信息
     * @param academicAffairsStaff 教务管理人员信息
     * @return 更新后的教务管理人员信息
     */
    public AcademicAffairsStaff updateAcademicAffairsStaff(AcademicAffairsStaff academicAffairsStaff) {
        return academicAffairsStaffRepository.save(academicAffairsStaff);
    }

    /**
     * 根据工号删除教务管理人员
     * @param aasId 工号
     */
    public void deleteAcademicAffairsStaff(String aasId) {
        academicAffairsStaffRepository.deleteById(aasId);
    }

    /**
     * 根据姓名模糊查询教务管理人员
     * @param aasName 姓名
     * @return 教务管理人员列表
     */
    public List<AcademicAffairsStaff> getAcademicAffairsStaffsByNameContaining(String aasName) {
        return academicAffairsStaffRepository.findByAcademicAffairsStaffNameContaining(aasName);
    }

    /**
     * 验证教务管理人员登录
     * @param aasId 工号
     * @param password 密码
     * @return 教务管理人员信息，验证失败返回null
     */
    public AcademicAffairsStaff validateAcademicAffairsStaff(String aasId, String password) {
        Optional<AcademicAffairsStaff> staff = academicAffairsStaffRepository.findByAcademicAffairsStaffIdAndPassword(aasId, password);
        return staff.orElse(null);
    }
}