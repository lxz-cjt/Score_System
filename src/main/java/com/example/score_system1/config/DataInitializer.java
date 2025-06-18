package com.example.score_system1.config;

import com.example.score_system1.entity.*;
import com.example.score_system1.enums.AppealStatus;
import com.example.score_system1.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 数据初始化器
 * 应用启动时自动生成示例数据
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private AcademicAffairsStaffRepository academicAffairsStaffRepository;
    
    @Autowired
    private ScoreRepository scoreRepository;
    
    @Autowired
    private CourseArrangingRepository courseArrangingRepository;
    
    @Autowired
    private AppealRepository appealRepository;
    
    @Autowired
    private ScoreLogRepository scoreLogRepository;

    @Autowired
    private AppealProcessRepository appealProcessRepository;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有数据，如果有则不初始化
        if (studentRepository.count() > 0) {
            logger.info("数据库中已存在数据，跳过数据初始化");
            return;
        }
        
        logger.info("开始初始化示例数据...");
        
        // 初始化学生数据
        initStudents();
        
        // 初始化教师数据
        initTeachers();
        
        // 初始化课程数据
        initCourses();
        
        // 初始化教务管理人员数据
        initAcademicAffairsStaff();
        
        // 初始化课程安排数据
        initCourseArrangings();
        
        // 初始化成绩数据
        initScores();
        
        // 初始化申诉数据
        initAppeals();
        
        // 初始化申诉处理过程数据
        initAppealProcesses();
        
        // 初始化成绩日志数据
        initScoreLogs();
        
        logger.info("示例数据初始化完成！");
    }
    
    /**
     * 初始化学生数据
     */
    private void initStudents() {
        List<Student> students = Arrays.asList(
            new Student("2021001", "张三", "计算机科学与技术", 45, "123456"),
            new Student("2021002", "李四", "计算机科学与技术", 38, "123456"),
            new Student("2021003", "王五", "软件工程", 52, "123456"),
            new Student("2021004", "赵六", "软件工程", 41, "123456"),
            new Student("2021005", "钱七", "数据科学与大数据技术", 36, "123456"),
            new Student("2021006", "孙八", "数据科学与大数据技术", 48, "123456"),
            new Student("2021007", "周九", "网络工程", 43, "123456"),
            new Student("2021008", "吴十", "网络工程", 39, "123456"),
            new Student("2021009", "郑十一", "信息安全", 50, "123456"),
            new Student("2021010", "王十二", "信息安全", 44, "123456")
        );
        
        studentRepository.saveAll(students);
        logger.info("已初始化 {} 个学生记录", students.size());
    }
    
    /**
     * 初始化教师数据
     */
    private void initTeachers() {
        List<Teacher> teachers = Arrays.asList(
            new Teacher("T001", "李教授", "计算机科学与技术", "teacher123"),
            new Teacher("T002", "张副教授", "软件工程", "teacher123"),
            new Teacher("T003", "王讲师", "数据科学与大数据技术", "teacher123"),
            new Teacher("T004", "刘教授", "网络工程", "teacher123"),
            new Teacher("T005", "陈副教授", "信息安全", "teacher123"),
            new Teacher("T006", "林讲师", "计算机科学与技术", "teacher123"),
            new Teacher("T007", "黄教授", "软件工程", "teacher123"),
            new Teacher("T008", "杨副教授", "数据科学与大数据技术", "teacher123")
        );
        
        teacherRepository.saveAll(teachers);
        logger.info("已初始化 {} 个教师记录", teachers.size());
    }

    /**
     * 初始化教务管理人员数据
     */
    private void initAcademicAffairsStaff() {
        List<AcademicAffairsStaff> staffList = Arrays.asList(
                new AcademicAffairsStaff("AAS001", "管理员一", "admin123"),
                new AcademicAffairsStaff("AAS002", "管理员二", "admin123"),
                new AcademicAffairsStaff("AAS003", "管理员三", "admin123")
        );

        academicAffairsStaffRepository.saveAll(staffList);
        logger.info("已初始化 {} 个教务管理人员记录", staffList.size());
    }

    /**
     * 初始化课程数据
     */
    private void initCourses() {
        List<Course> courses = Arrays.asList(
            new Course("CS101", "计算机程序设计基础", (short) 4),
            new Course("CS102", "数据结构与算法", (short) 4),
            new Course("CS103", "计算机组成原理", (short) 3),
            new Course("CS104", "操作系统", (short) 3),
            new Course("CS105", "数据库系统原理", (short) 3),
            new Course("CS106", "计算机网络", (short) 3),
            new Course("CS107", "软件工程", (short) 3),
            new Course("CS108", "人工智能导论", (short) 2),
            new Course("CS109", "机器学习", (short) 3),
            new Course("CS110", "信息安全基础", (short) 2)
        );
        
        courseRepository.saveAll(courses);
        logger.info("已初始化 {} 个课程记录", courses.size());
    }
    
    /**
     * 初始化课程安排数据
     */
    private void initCourseArrangings() {
        List<CourseArranging> courseArrangings = Arrays.asList(
            new CourseArranging("CS101", "T001", "CA001", "周一 1-2节, 周三 3-4节", "计算机楼101"),
            new CourseArranging("CS102", "T001", "CA002", "周二 5-6节, 周四 7-8节", "计算机楼102"),
            new CourseArranging("CS103", "T002", "CA003", "周五 1-2节, 周六 3-4节", "计算机楼103"),
            new CourseArranging("CS104", "T002", "CA004", "周一 3-4节, 周三 5-6节", "计算机楼104"),
            new CourseArranging("CS105", "T003", "CA005", "周二 7-8节, 周四 1-2节", "计算机楼105"),
            new CourseArranging("CS106", "T004", "CA006", "周五 3-4节, 周日 5-6节", "计算机楼106"),
            new CourseArranging("CS107", "T002", "CA007", "周一 5-6节, 周三 7-8节", "计算机楼107"),
            new CourseArranging("CS108", "T003", "CA008", "周二 1-2节, 周四 3-4节", "计算机楼108"),
            new CourseArranging("CS109", "T003", "CA009", "周五 5-6节, 周六 7-8节", "计算机楼109"),
            new CourseArranging("CS110", "T005", "CA010", "周一 7-8节, 周三 1-2节", "计算机楼110")
        );
        
        courseArrangingRepository.saveAll(courseArrangings);
        logger.info("已初始化 {} 个课程安排记录", courseArrangings.size());
    }
    
    /**
     * 初始化成绩数据
     */
    private void initScores() {
        List<Score> scores = Arrays.asList(
            // 学生2021001的成绩
            new Score(85, 88, 87, "通过", false, "SC001", "2021001", "CS101"),
            new Score(78, 82, 80, "通过", false, "SC002", "2021001", "CS102"),
            new Score(92, 89, 90, "优秀", false, "SC003", "2021001", "CS103"),
            
            // 学生2021002的成绩
            new Score(65, 68, 67, "通过", false, "SC004", "2021002", "CS101"),
            new Score(55, 58, 57, "不通过", true, "SC005", "2021002", "CS102"),
            new Score(75, 78, 77, "通过", false, "SC006", "2021002", "CS103"),
            
            // 学生2021003的成绩
            new Score(88, 91, 90, "优秀", false, "SC007", "2021003", "CS101"),
            new Score(83, 85, 84, "通过", false, "SC008", "2021003", "CS102"),
            new Score(79, 81, 80, "通过", false, "SC009", "2021003", "CS104"),
            
            // 学生2021004的成绩
            new Score(72, 75, 74, "通过", false, "SC010", "2021004", "CS101"),
            new Score(68, 70, 69, "通过", false, "SC011", "2021004", "CS105"),
            new Score(85, 87, 86, "通过", false, "SC012", "2021004", "CS106"),
            
            // 学生2021005的成绩
            new Score(45, 48, 47, "不通过", true, "SC013", "2021005", "CS101"),
            new Score(82, 85, 84, "通过", false, "SC014", "2021005", "CS105"),
            new Score(76, 78, 77, "通过", false, "SC015", "2021005", "CS108")
        );
        
        scoreRepository.saveAll(scores);
        logger.info("已初始化 {} 个成绩记录", scores.size());
    }
    
    /**
     * 初始化申诉数据
     */
    private void initAppeals() {
        List<Appeal> appeals = Arrays.asList(
            new Appeal("2021002", "AAS001", "T001", "AP001", 
                      new java.util.Date(), "对CS102课程成绩有异议，认为评分不公平，请重新评定"),
            new Appeal("2021005", "AAS001", "T001", "AP002", 
                      new java.util.Date(), "对CS101课程成绩不满意，请求重新评分"),
            new Appeal("2021001", "AAS002", "T002", "AP003", 
                      new java.util.Date(), "CS103课程期末考试答题卡可能存在问题，请核查")
        );
        
        appealRepository.saveAll(appeals);
        logger.info("已初始化 {} 个申诉记录", appeals.size());
    }
    
    /**
     * 初始化申诉处理过程数据
     */
    private void initAppealProcesses() {
        java.util.Date currentDate = new java.util.Date();
        
        List<com.example.score_system1.entity.AppealProcess> appealProcesses = Arrays.asList();
        
        appealProcessRepository.saveAll(appealProcesses);
        logger.info("已初始化 {} 个申诉处理过程记录", appealProcesses.size());
    }
    
    /**
     * 初始化成绩日志数据
     */
    private void initScoreLogs() {
        Date currentDate = new Date(System.currentTimeMillis());
        
        List<ScoreLog> scoreLogs = Arrays.asList(
            new ScoreLog(0, "SC001", "2021001", "CS101", "新增", 0, 87, "", "通过", currentDate, "T001"),
            new ScoreLog(0, "SC002", "2021001", "CS102", "新增", 0, 80, "", "通过", currentDate, "T001"),
            new ScoreLog(0, "SC005", "2021002", "CS102", "新增", 0, 57, "", "不通过", currentDate, "T001"),
            new ScoreLog(0, "SC005", "2021002", "CS102", "修改", 57, 65, "不通过", "通过", currentDate, "T001"),
            new ScoreLog(0, "SC013", "2021005", "CS101", "新增", 0, 47, "", "不通过", currentDate, "T001")
        );
        
        scoreLogRepository.saveAll(scoreLogs);
        logger.info("已初始化 {} 个成绩日志记录", scoreLogs.size());
    }
} 