package com.example.score_system1.entity;

public class Course {
    private String cId;
    private String cName;
    private short cCredit;

    public Long id;
    public String courseCode;
    public String courseName;
    public String teachername;
    public short credits;
    public String coursetype;
    public String schedule;
    public String classroom;
    public int capacity;
    public int enrolled;

    //  id: 1,
    //      courseCode: 'CS101',
    //      courseName: '计算机程序设计基础',
    //      teacherName: '李教授',
    //      credits: 4,
    //      courseType: 'required',
    //      schedule: '周一 1-2节，周三 3-4节',
    //      classroom: '计算机楼101',
    //      capacity: 60,
    //      enrolled: 45



    public Course(String cId, String cName, short cCredit) {
        this.cId = cId;
        this.cName = cName;
        this.cCredit = cCredit;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public short getcCredit() {
        return cCredit;
    }

    public void setcCredit(short cCredit) {
        this.cCredit = cCredit;
    }

    public void setCourseCode(String cId) {
        this.courseCode = cId;
    }
    public void setCourseName(String cName) {
        this.courseName = this.cName;
    }

    @Override
    public String toString() {
        return "Course{" +
                "cId='" + cId + '\'' +
                ", cName='" + cName + '\'' +
                ", cCredit=" + cCredit +
                '}';
    }
}
