
package com.hostel.model;

public class Student {
    private String studentId;
    private String fullName;
    private String email;
    private String phone;
    private String course;

    public Student(String studentId, String fullName,
                   String email, String phone, String course) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.course = course;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCourse() {
        return course;
    }

    @Override
    public String toString() {
        return "Student ID: " + studentId +
               "\nFull Name: " + fullName +
               "\nEmail: " + email +
               "\nPhone: " + phone +
               "\nCourse: " + course;
    }
}
