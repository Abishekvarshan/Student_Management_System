package com.example.studentmanagement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Column(name = "grade")
    private String grade;  // e.g., "A", "B+", "C"

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    // Default constructor
    public Enrollment() {
    }

    // Constructor
    public Enrollment(Student student, Course course, LocalDate enrollmentDate, EnrollmentStatus status) {
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", studentId=" + (student != null ? student.getId() : null) +
                ", courseId=" + (course != null ? course.getId() : null) +
                ", enrollmentDate=" + enrollmentDate +
                ", grade='" + grade + '\'' +
                ", status='" + status + '\'' +
                ", approvedAt='" + approvedAt + '\'' +
                '}';
    }
}