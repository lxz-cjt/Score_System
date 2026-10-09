package io.github.lxzcjt.scoresystem.repository;

import io.github.lxzcjt.scoresystem.entity.ScoreLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreLogRepository extends JpaRepository<ScoreLog, Integer> {

    Page<ScoreLog> findByScoreIdOrderByOperationTimeDesc(String scoreId, Pageable pageable);

    Page<ScoreLog> findByOperatorIdOrderByOperationTimeDesc(String operatorId, Pageable pageable);

    Page<ScoreLog> findAllByOrderByOperationTimeDesc(Pageable pageable);
}
