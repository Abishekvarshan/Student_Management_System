package com.example.studentmanagement.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentmanagement.model.Enrollment;
import com.example.studentmanagement.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // POST: Enroll a student in a course
    @PostMapping
    public ResponseEntity<Enrollment> enrollStudent(@RequestBody Map<String, UUID> request) {
        UUID studentId = request.get("studentId");
        UUID courseId = request.get("courseId");
        Enrollment enrollment = enrollmentService.enrollStudent(studentId, courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    // GET: Get all enrollments
    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    // GET: Get enrollment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable UUID id) {
        Enrollment enrollment = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(enrollment);
    }

    // GET: Get enrollments by student ID
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable UUID studentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }

    // GET: Get enrollments by course ID
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourse(@PathVariable UUID courseId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
        return ResponseEntity.ok(enrollments);
    }

    // GET: Get enrollments by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStatus(@PathVariable String status) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStatus(status);
        return ResponseEntity.ok(enrollments);
    }

    // PATCH: Update grade
    @PatchMapping("/{id}/grade")
    public ResponseEntity<Enrollment> updateGrade(@PathVariable UUID id, @RequestBody Map<String, String> request) {
        String grade = request.get("grade");
        Enrollment enrollment = enrollmentService.updateGrade(id, grade);
        return ResponseEntity.ok(enrollment);
    }

    // PATCH: Update status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Enrollment> updateStatus(@PathVariable UUID id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        Enrollment enrollment = enrollmentService.updateStatus(id, status);
        return ResponseEntity.ok(enrollment);
    }

    // DELETE: Delete enrollment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable UUID id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE: Unenroll student from course
    @DeleteMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Void> unenrollStudent(@PathVariable UUID studentId, @PathVariable UUID courseId) {
        enrollmentService.unenrollStudent(studentId, courseId);
        return ResponseEntity.noContent().build();
    }
}