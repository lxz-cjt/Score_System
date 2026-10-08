package io.github.lxzcjt.scoresystem.service;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.exception.BusinessException;
import io.github.lxzcjt.scoresystem.dto.request.StaffSaveRequest;
import io.github.lxzcjt.scoresystem.entity.AcademicAffairsStaff;
import io.github.lxzcjt.scoresystem.repository.AcademicAffairsStaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 教务人员管理服务（教务侧）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StaffService {

    private final AcademicAffairsStaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    public PageResult<AcademicAffairsStaff> listStaff(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<AcademicAffairsStaff> result = StringUtils.hasText(keyword)
                ? staffRepository.findByStaffNameContaining(keyword, pageable)
                : staffRepository.findAll(pageable);
        return PageResult.of(result);
    }

    public AcademicAffairsStaff getStaff(String staffId) {
        return staffRepository.findByStaffId(staffId)
                .orElseThrow(() -> BusinessException.notFound("教务人员不存在: " + staffId));
    }

    @Transactional
    public AcademicAffairsStaff createStaff(StaffSaveRequest request) {
        if (staffRepository.existsByStaffId(request.staffId())) {
            throw BusinessException.conflict("教务工号已存在: " + request.staffId());
        }
        if (!StringUtils.hasText(request.password())) {
            throw BusinessException.badRequest("新增教务人员时初始密码不能为空");
        }
        AcademicAffairsStaff staff = AcademicAffairsStaff.builder()
                .staffId(request.staffId())
                .staffName(request.staffName())
                .password(passwordEncoder.encode(request.password()))
                .build();
        log.info("新增教务人员: {}", staff.getStaffId());
        return staffRepository.save(staff);
    }

    @Transactional
    public AcademicAffairsStaff updateStaff(String staffId, StaffSaveRequest request) {
        AcademicAffairsStaff staff = getStaff(staffId);
        staff.setStaffName(request.staffName());
        if (StringUtils.hasText(request.password())) {
            staff.setPassword(passwordEncoder.encode(request.password()));
        }
        log.info("更新教务人员: {}", staffId);
        return staffRepository.save(staff);
    }

    @Transactional
    public void deleteStaff(String staffId) {
        AcademicAffairsStaff staff = getStaff(staffId);
        log.info("删除教务人员: {}", staffId);
        staffRepository.delete(staff);
    }
}
