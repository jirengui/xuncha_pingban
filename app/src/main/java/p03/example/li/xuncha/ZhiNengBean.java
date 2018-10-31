package p03.example.li.xuncha;

import java.io.Serializable;

/**
 * Created by li on 2018/3/18.
 */

public class ZhiNengBean  implements Serializable {
    private String date;
    private String teacherName;
    private String course_name;
    private String student_class;
    private String classroom;
    private String teacher_status;
    private String student_status;
    private String picture;
    public ZhiNengBean(String date, String teacherName, String course_name, String student_class, String classroom, String teacher_status, String student_status){
        this.date = date;
        this.teacherName = teacherName;
        this.course_name = course_name;
        this.student_class = student_class;
        this.classroom = classroom;
        this.teacher_status = teacher_status;
        this.student_status = student_status;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTeacher_status() {
        return teacher_status;
    }

    public void setTeacher_status(String teacher_status) {
        this.teacher_status = teacher_status;
    }

    public String getStudent_status() {
        return student_status;
    }

    public void setStudent_status(String student_status) {
        this.student_status = student_status;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
