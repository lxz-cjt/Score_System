package io.github.lxzcjt.scoresystem.service;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.entity.ScoreLog;
import io.github.lxzcjt.scoresystem.repository.ScoreLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 成绩日志查询服务
 */
@Service
@RequiredArgsConstructor
public class ScoreLogService {

    private final ScoreLogRepository scoreLogRepository;

    /**
     * 分页查询成绩日志，支持按成绩编号/操作人过滤
     */
    public PageResult<ScoreLog> listLogs(String scoreId, String operatorId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ScoreLog> result;
        if (StringUtils.hasText(scoreId)) {
            result = scoreLogRepository.findByScoreIdOrderByOperationTimeDesc(scoreId, pageable);
        } else if (StringUtils.hasText(operatorId)) {
            result = scoreLogRepository.findByOperatorIdOrderByOperationTimeDesc(operatorId, pageable);
        } else {
            result = scoreLogRepository.findAllByOrderByOperationTimeDesc(pageable);
        }
        return PageResult.of(result);
    }
}
