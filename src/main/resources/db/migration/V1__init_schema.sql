-- ============================================================
-- V1 初始化数据库表结构
-- 兼容 MySQL 8 与 H2（MySQL 兼容模式）
-- ============================================================

-- 学生表
CREATE TABLE student (
    st_id         VARCHAR(20)  NOT NULL,
    st_name       VARCHAR(50)  NOT NULL COMMENT '姓名',
    st_major      VARCHAR(100) NOT NULL COMMENT '专业',
    st_get_credit INT          NOT NULL DEFAULT 0 COMMENT '已获得学分',
    password      VARCHAR(100) NOT NULL COMMENT '登录密码（BCrypt）',
    PRIMARY KEY (st_id)
);

-- 教师表
CREATE TABLE teacher (
    te_id      VARCHAR(20)  NOT NULL,
    te_name    VARCHAR(50)  NOT NULL COMMENT '姓名',
    te_college VARCHAR(100) NOT NULL COMMENT '所属学院',
    password   VARCHAR(100) NOT NULL COMMENT '登录密码（BCrypt）',
    PRIMARY KEY (te_id)
);

-- 教务人员表
CREATE TABLE academic_affairs_staff (
    aas_id   VARCHAR(20)  NOT NULL,
    aas_name VARCHAR(50)  NOT NULL COMMENT '姓名',
    password VARCHAR(100) NOT NULL COMMENT '登录密码（BCrypt）',
    PRIMARY KEY (aas_id)
);

-- 课程表
CREATE TABLE course (
    c_id     VARCHAR(20) NOT NULL,
    c_name   VARCHAR(50) NOT NULL COMMENT '课程名称',
    c_credit SMALLINT    NOT NULL COMMENT '学分',
    PRIMARY KEY (c_id)
);

-- 课程安排表（排课）
CREATE TABLE course_arranging (
    ca_id     VARCHAR(50) NOT NULL,
    c_id      VARCHAR(20) NOT NULL COMMENT '课程编号',
    te_id     VARCHAR(20) NOT NULL COMMENT '教师工号',
    class_time VARCHAR(50) NOT NULL COMMENT '上课时间',
    classroom VARCHAR(50) NOT NULL COMMENT '上课地点',
    PRIMARY KEY (ca_id),
    CONSTRAINT fk_arranging_course  FOREIGN KEY (c_id)  REFERENCES course (c_id),
    CONSTRAINT fk_arranging_teacher FOREIGN KEY (te_id) REFERENCES teacher (te_id)
);

-- 成绩表（一名学生一门课程一条记录）
CREATE TABLE score (
    score_id              VARCHAR(50) NOT NULL,
    daily_score           INT         NOT NULL COMMENT '平时成绩',
    exam_score            INT         NOT NULL COMMENT '考试成绩',
    total_score           INT         NOT NULL COMMENT '总评成绩（平时30%+考试70%）',
    credit_gain_condition VARCHAR(20) NOT NULL COMMENT '学分获得条件：优秀/通过/不通过',
    make_up_exam          BOOLEAN     NOT NULL DEFAULT FALSE COMMENT '是否需要补考',
    st_id                 VARCHAR(20) NOT NULL COMMENT '学号',
    c_id                  VARCHAR(20) NOT NULL COMMENT '课程编号',
    PRIMARY KEY (score_id),
    CONSTRAINT uk_score_student_course UNIQUE (st_id, c_id),
    CONSTRAINT fk_score_student FOREIGN KEY (st_id) REFERENCES student (st_id),
    CONSTRAINT fk_score_course  FOREIGN KEY (c_id)  REFERENCES course (c_id)
);

-- 成绩变更日志表
CREATE TABLE score_log (
    log_id                INT AUTO_INCREMENT PRIMARY KEY,
    score_id              VARCHAR(50) NOT NULL COMMENT '成绩编号',
    st_id                 VARCHAR(20) NOT NULL COMMENT '学号',
    c_id                  VARCHAR(20) NOT NULL COMMENT '课程编号',
    operation_type        VARCHAR(20) NOT NULL COMMENT '操作类型：INSERT/UPDATE/DELETE',
    old_score             INT COMMENT '修改前总评',
    new_score             INT COMMENT '修改后总评',
    old_credit_condition  VARCHAR(20) COMMENT '修改前学分获得条件',
    new_credit_condition  VARCHAR(20) COMMENT '修改后学分获得条件',
    operation_time        DATETIME    NOT NULL COMMENT '操作时间',
    operator_id           VARCHAR(50) NOT NULL COMMENT '操作人工号',
    INDEX idx_score_log_score_id (score_id),
    INDEX idx_score_log_operator_id (operator_id)
);

-- 成绩申诉表
CREATE TABLE appeal (
    ap_id          VARCHAR(50)  NOT NULL,
    st_id          VARCHAR(20)  NOT NULL COMMENT '学号（发起人）',
    c_id           VARCHAR(20)  NOT NULL COMMENT '被申诉课程编号',
    te_id          VARCHAR(20) COMMENT '处理教师工号',
    aas_id         VARCHAR(20) COMMENT '审核教务工号',
    appeal_reason  VARCHAR(500) NOT NULL COMMENT '申诉理由',
    appeal_time    DATETIME     NOT NULL COMMENT '申诉时间',
    appeal_result  VARCHAR(500) COMMENT '申诉结果说明',
    appeal_status  VARCHAR(30)  NOT NULL COMMENT '状态：PENDING/SUBMITTED_TO_ADMIN/APPROVED/REJECTED/CANCELLED',
    result_time    DATETIME COMMENT '审核完成时间',
    PRIMARY KEY (ap_id),
    INDEX idx_appeal_student (st_id),
    INDEX idx_appeal_teacher (te_id),
    INDEX idx_appeal_status (appeal_status),
    CONSTRAINT fk_appeal_student FOREIGN KEY (st_id) REFERENCES student (st_id),
    CONSTRAINT fk_appeal_course  FOREIGN KEY (c_id)  REFERENCES course (c_id)
);

-- 申诉处理记录表
CREATE TABLE appeal_process (
    process_id       INT AUTO_INCREMENT PRIMARY KEY,
    ap_id            VARCHAR(50)   NOT NULL COMMENT '申诉编号',
    process_step     VARCHAR(100)  NOT NULL COMMENT '处理步骤',
    processor_id     VARCHAR(50)   NOT NULL COMMENT '处理人编号',
    processor_name   VARCHAR(50)   NOT NULL COMMENT '处理人姓名',
    process_time     DATETIME      NOT NULL COMMENT '处理时间',
    process_opinion  VARCHAR(1000) COMMENT '处理意见',
    CONSTRAINT fk_process_appeal FOREIGN KEY (ap_id) REFERENCES appeal (ap_id)
);
