package com.example.studentmanagement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.studentmanagement.model.EnrollmentStatus;

public class EnrollmentStatusResponse {

    private UUID enrollmentId;
    private UUID studentId;
    private UUID courseId;
    private EnrollmentStatus status;
    private LocalDate enrollmentDate;
    private LocalDateTime approvedAt;

    public EnrollmentStatusResponse() {
    }

    public EnrollmentStatusResponse(UUID enrollmentId, UUID studentId, UUID courseId, EnrollmentStatus status,
            LocalDate enrollmentDate, LocalDateTime approvedAt) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.status = status;
        this.enrollmentDate = enrollmentDate;
        this.approvedAt = approvedAt;
    }

    public UUID getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(UUID enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }
}
