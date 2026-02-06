package com.example.studentmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentmanagement.model.Enrollment;
import com.example.studentmanagement.service.EnrollmentService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminEnrollmentController {

    private final EnrollmentService enrollmentService;

    public AdminEnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    /**
     * GET /admin/enrollments/pending
     * Returns enrollments with status REQUESTED.
     */
    @GetMapping("/enrollments/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Enrollment>> pendingEnrollments() {
        return ResponseEntity.ok(enrollmentService.getPendingEnrollments());
    }

    /**
     * PUT /admin/enrollments/{enrollmentId}/approve
     */
    @PutMapping("/enrollments/{enrollmentId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Enrollment> approve(@PathVariable UUID enrollmentId) {
        return ResponseEntity.ok(enrollmentService.approveEnrollment(enrollmentId));
    }

    /**
     * PUT /admin/enrollments/{enrollmentId}/reject
     */
    @PutMapping("/enrollments/{enrollmentId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Enrollment> reject(@PathVariable UUID enrollmentId) {
        return ResponseEntity.ok(enrollmentService.rejectEnrollment(enrollmentId));
    }
}
