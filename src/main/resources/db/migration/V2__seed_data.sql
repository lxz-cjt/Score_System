-- ============================================================
-- V2 初始化示例数据（演示用）
-- 密码均已使用 BCrypt 加密：
--   学生 123456 / 教师 teacher123 / 教务 admin123
-- ============================================================

-- 学生（密码均为 123456）
INSERT INTO student (st_id, st_name, st_major, st_get_credit, password) VALUES
('2021001', '张三',   '计算机科学与技术', 45, '$2a$10$gz59G/1Ng1PvDOx7S0rnoub7ZbUQpv9Mqk5W7.nz//SsJ57hTxKGK'),
('2021002', '李四',   '计算机科学与技术', 38, '$2a$10$gz59G/1Ng1PvDOx7S0rnoub7ZbUQpv9Mqk5W7.nz//SsJ57hTxKGK'),
('2021003', '王五',   '软件工程',         52, '$2a$10$gz59G/1Ng1PvDOx7S0rnoub7ZbUQpv9Mqk5W7.nz//SsJ57hTxKGK'),
('2021004', '赵六',   '软件工程',         41, '$2a$10$gz59G/1Ng1PvDOx7S0rnoub7ZbUQpv9Mqk5W7.nz//SsJ57hTxKGK'),
('2021005', '钱七',   '数据科学与大数据技术', 36, '$2a$10$gz59G/1Ng1PvDOx7S0rnoub7ZbUQpv9Mqk5W7.nz//SsJ57hTxKGK'),
('2021006', '孙八',   '数据科学与大数据技术', 48, '$2a$10$gz59G/1Ng1PvDOx7S0rnoub7ZbUQpv9Mqk5W7.nz//SsJ57hTxKGK'),
('2021007', '周九',   '网络工程',         43, '$2a$10$gz59G/1Ng1PvDOx7S0rnoub7ZbUQpv9Mqk5W7.nz//SsJ57hTxKGK'),
('2021008', '吴十',   '网络工程',         39, '$2a$10$gz59G/1Ng1PvDOx7S0rnoub7ZbUQpv9Mqk5W7.nz//SsJ57hTxKGK'),
('2021009', '郑十一', '信息安全',         50, '$2a$10$gz59G/1Ng1PvDOx7S0rnoub7ZbUQpv9Mqk5W7.nz//SsJ57hTxKGK'),
('2021010', '王十二', '信息安全',         44, '$2a$10$gz59G/1Ng1PvDOx7S0rnoub7ZbUQpv9Mqk5W7.nz//SsJ57hTxKGK');

-- 教师（密码均为 teacher123）
INSERT INTO teacher (te_id, te_name, te_college, password) VALUES
('T001', '李教授',   '计算机学院', '$2a$10$TYU6l3Ju7l5vQdcpC.3JXeCWwWF8xSkesFl2GvuGMRw/VWpxn17zK'),
('T002', '张副教授', '软件学院',   '$2a$10$TYU6l3Ju7l5vQdcpC.3JXeCWwWF8xSkesFl2GvuGMRw/VWpxn17zK'),
('T003', '王讲师',   '数据科学学院', '$2a$10$TYU6l3Ju7l5vQdcpC.3JXeCWwWF8xSkesFl2GvuGMRw/VWpxn17zK'),
('T004', '刘教授',   '网络工程学院', '$2a$10$TYU6l3Ju7l5vQdcpC.3JXeCWwWF8xSkesFl2GvuGMRw/VWpxn17zK'),
('T005', '陈副教授', '信息安全学院', '$2a$10$TYU6l3Ju7l5vQdcpC.3JXeCWwWF8xSkesFl2GvuGMRw/VWpxn17zK'),
('T006', '林讲师',   '计算机学院',  '$2a$10$TYU6l3Ju7l5vQdcpC.3JXeCWwWF8xSkesFl2GvuGMRw/VWpxn17zK'),
('T007', '黄教授',   '软件学院',    '$2a$10$TYU6l3Ju7l5vQdcpC.3JXeCWwWF8xSkesFl2GvuGMRw/VWpxn17zK'),
('T008', '杨副教授', '数据科学学院', '$2a$10$TYU6l3Ju7l5vQdcpC.3JXeCWwWF8xSkesFl2GvuGMRw/VWpxn17zK');

-- 教务人员（密码均为 admin123）
INSERT INTO academic_affairs_staff (aas_id, aas_name, password) VALUES
('AAS001', '管理员一', '$2a$10$4sJnqnYqDiT7uzyGEZCK.OR1PA97PFXvIHNHhrsi9gDo1ZrgGuaY6'),
('AAS002', '管理员二', '$2a$10$4sJnqnYqDiT7uzyGEZCK.OR1PA97PFXvIHNHhrsi9gDo1ZrgGuaY6'),
('AAS003', '管理员三', '$2a$10$4sJnqnYqDiT7uzyGEZCK.OR1PA97PFXvIHNHhrsi9gDo1ZrgGuaY6');

-- 课程
INSERT INTO course (c_id, c_name, c_credit) VALUES
('CS101', '计算机程序设计基础', 4),
('CS102', '数据结构与算法',     4),
('CS103', '计算机组成原理',     3),
('CS104', '操作系统',           3),
('CS105', '数据库系统原理',     3),
('CS106', '计算机网络',         3),
('CS107', '软件工程',           3),
('CS108', '人工智能导论',       2),
('CS109', '机器学习',           3),
('CS110', '信息安全基础',       2);

-- 课程安排
INSERT INTO course_arranging (ca_id, c_id, te_id, class_time, classroom) VALUES
('CA001', 'CS101', 'T001', '周一 1-2节, 周三 3-4节', '计算机楼101'),
('CA002', 'CS102', 'T001', '周二 5-6节, 周四 7-8节', '计算机楼102'),
('CA003', 'CS103', 'T002', '周五 1-2节, 周六 3-4节', '计算机楼103'),
('CA004', 'CS104', 'T002', '周一 3-4节, 周三 5-6节', '计算机楼104'),
('CA005', 'CS105', 'T003', '周二 7-8节, 周四 1-2节', '计算机楼105'),
('CA006', 'CS106', 'T004', '周五 3-4节, 周日 5-6节', '计算机楼106'),
('CA007', 'CS107', 'T002', '周一 5-6节, 周三 7-8节', '计算机楼107'),
('CA008', 'CS108', 'T003', '周二 1-2节, 周四 3-4节', '计算机楼108'),
('CA009', 'CS109', 'T003', '周五 5-6节, 周六 7-8节', '计算机楼109'),
('CA010', 'CS110', 'T005', '周一 7-8节, 周三 1-2节', '计算机楼110');

-- 成绩
INSERT INTO score (score_id, daily_score, exam_score, total_score, credit_gain_condition, make_up_exam, st_id, c_id) VALUES
('SC001', 85, 88, 87, '通过',   FALSE, '2021001', 'CS101'),
('SC002', 78, 82, 80, '通过',   FALSE, '2021001', 'CS102'),
('SC003', 92, 89, 90, '优秀',   FALSE, '2021001', 'CS103'),
('SC004', 65, 68, 67, '通过',   FALSE, '2021002', 'CS101'),
('SC005', 55, 58, 57, '不通过', TRUE,  '2021002', 'CS102'),
('SC006', 75, 78, 77, '通过',   FALSE, '2021002', 'CS103'),
('SC007', 88, 91, 90, '优秀',   FALSE, '2021003', 'CS101'),
('SC008', 83, 85, 84, '通过',   FALSE, '2021003', 'CS102'),
('SC009', 79, 81, 80, '通过',   FALSE, '2021003', 'CS104'),
('SC010', 72, 75, 74, '通过',   FALSE, '2021004', 'CS101'),
('SC011', 68, 70, 69, '通过',   FALSE, '2021004', 'CS105'),
('SC012', 85, 87, 86, '通过',   FALSE, '2021004', 'CS106'),
('SC013', 45, 48, 47, '不通过', TRUE,  '2021005', 'CS101'),
('SC014', 82, 85, 84, '通过',   FALSE, '2021005', 'CS105'),
('SC015', 76, 78, 77, '通过',   FALSE, '2021005', 'CS108');

-- 申诉（AP002 演示"教师已处理、待教务审核"状态）
INSERT INTO appeal (ap_id, st_id, c_id, te_id, aas_id, appeal_reason, appeal_time, appeal_result, appeal_status, result_time) VALUES
('AP001', '2021002', 'CS102', 'T001', NULL, '对CS102课程成绩有异议，认为评分不公平，请重新评定', CURRENT_TIMESTAMP, NULL, 'PENDING', NULL),
('AP002', '2021005', 'CS101', 'T001', NULL, '对CS101课程成绩不满意，请求重新评分', CURRENT_TIMESTAMP, NULL, 'SUBMITTED_TO_ADMIN', NULL),
('AP003', '2021001', 'CS103', 'T002', NULL, 'CS103课程期末考试答题卡可能存在问题，请核查', CURRENT_TIMESTAMP, NULL, 'PENDING', NULL);

-- 申诉处理记录
INSERT INTO appeal_process (ap_id, process_step, processor_id, processor_name, process_time, process_opinion) VALUES
('AP001', '学生提交申诉', '2021002', '李四',   CURRENT_TIMESTAMP, '对CS102课程成绩有异议，认为评分不公平，请重新评定'),
('AP002', '学生提交申诉', '2021005', '钱七',   CURRENT_TIMESTAMP, '对CS101课程成绩不满意，请求重新评分'),
('AP002', '教师处理',     'T001',    '李教授', CURRENT_TIMESTAMP, '已重新核对试卷，成绩无误，提交教务审核'),
('AP003', '学生提交申诉', '2021001', '张三',   CURRENT_TIMESTAMP, 'CS103课程期末考试答题卡可能存在问题，请核查');

-- 成绩变更日志
INSERT INTO score_log (score_id, st_id, c_id, operation_type, old_score, new_score, old_credit_condition, new_credit_condition, operation_time, operator_id) VALUES
('SC001', '2021001', 'CS101', 'INSERT', NULL, 87, NULL, '通过',   CURRENT_TIMESTAMP, 'T001'),
('SC002', '2021001', 'CS102', 'INSERT', NULL, 80, NULL, '通过',   CURRENT_TIMESTAMP, 'T001'),
('SC005', '2021002', 'CS102', 'INSERT', NULL, 57, NULL, '不通过', CURRENT_TIMESTAMP, 'T001'),
('SC005', '2021002', 'CS102', 'UPDATE', 57,   65, '不通过', '通过', CURRENT_TIMESTAMP, 'T001'),
('SC013', '2021005', 'CS101', 'INSERT', NULL, 47, NULL, '不通过', CURRENT_TIMESTAMP, 'T001');
