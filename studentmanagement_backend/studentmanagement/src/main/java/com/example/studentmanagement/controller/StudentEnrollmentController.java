package com.example.studentmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentmanagement.dto.EnrollmentStatusResponse;
import com.example.studentmanagement.exception.UnauthorizedException;
import com.example.studentmanagement.model.Enrollment;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.security.CurrentUserService;
import com.example.studentmanagement.service.EnrollmentService;
import com.example.studentmanagement.service.StudentService;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "*")
public class StudentEnrollmentController {

    private final EnrollmentService enrollmentService;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final StudentService studentService;

    public StudentEnrollmentController(EnrollmentService enrollmentService, CurrentUserService currentUserService,
            UserRepository userRepository, StudentService studentService) {
        this.enrollmentService = enrollmentService;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
        this.studentService = studentService;
    }

    /**
     * POST /student/enroll/{courseId}
     * - Reads student identity from JWT (email subject)
     * - Creates Enrollment with status REQUESTED
     */
    @PostMapping("/enroll/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Enrollment> requestEnrollment(@PathVariable UUID courseId) {
        String email = currentUserService.getCurrentUsername()
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));

        // Ensure user exists
        userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        UUID studentId = studentService.getStudentByEmail(email).getId();
        Enrollment enrollment = enrollmentService.requestEnrollment(studentId, courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    /**
     * GET /student/my-enrollments
     * - Returns only APPROVED enrollments for the logged-in student
     */
    @GetMapping("/my-enrollments")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Enrollment>> myApprovedEnrollments() {
        String email = currentUserService.getCurrentUsername()
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));
        userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        UUID studentId = studentService.getStudentByEmail(email).getId();
        return ResponseEntity.ok(enrollmentService.getApprovedEnrollmentsForStudent(studentId));
    }

    /**
     * GET /student/enrollment-status/{courseId}
     * Convenience endpoint for a student to check their own status for a given course.
     */
    @GetMapping("/enrollment-status/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentStatusResponse> myEnrollmentStatus(@PathVariable UUID courseId) {
        String email = currentUserService.getCurrentUsername()
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));
        userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        UUID studentId = studentService.getStudentByEmail(email).getId();
        Enrollment e = enrollmentService.getEnrollmentByStudentAndCourse(studentId, courseId);
        EnrollmentStatusResponse dto = new EnrollmentStatusResponse(
                e.getId(),
                studentId,
                courseId,
                e.getStatus(),
                e.getEnrollmentDate(),
                e.getApprovedAt());
        return ResponseEntity.ok(dto);
    }
}
