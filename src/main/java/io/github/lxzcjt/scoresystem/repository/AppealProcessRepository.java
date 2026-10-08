package io.github.lxzcjt.scoresystem.repository;

import io.github.lxzcjt.scoresystem.entity.AppealProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppealProcessRepository extends JpaRepository<AppealProcess, Integer> {

    /**
     * 查询某申诉的完整处理过程（按时间正序，用于时间线展示）
     */
    List<AppealProcess> findByAppealIdOrderByProcessTimeAsc(String appealId);
}
