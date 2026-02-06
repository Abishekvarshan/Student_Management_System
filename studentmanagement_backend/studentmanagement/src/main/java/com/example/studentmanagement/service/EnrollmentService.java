package com.example.studentmanagement.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.studentmanagement.exception.EnrollmentAlreadyExistsException;
import com.example.studentmanagement.exception.EnrollmentNotFoundException;
import com.example.studentmanagement.model.Course;
import com.example.studentmanagement.model.Enrollment;
import com.example.studentmanagement.model.EnrollmentStatus;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.EnrollmentRepository;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentService studentService;
    private final CourseService courseService;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, 
                           StudentService studentService, 
                           CourseService courseService) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    /**
     * Student enrollment request flow.
     * Creates an enrollment with {@link EnrollmentStatus#REQUESTED}.
     */
    @Transactional
    public Enrollment requestEnrollment(UUID studentId, UUID courseId) {
        Student student = studentService.getStudentById(studentId);
        Course course = courseService.getCourseById(courseId);

        // Prevent duplicate enrollment requests/enrollments
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new EnrollmentAlreadyExistsException(
                    "Enrollment already exists (requested/approved/rejected) for this student and course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.REQUESTED);
        enrollment.setApprovedAt(null);

        return enrollmentRepository.save(enrollment);
    }

    // Get all enrollments
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    // Get enrollment by ID
    public Enrollment getEnrollmentById(UUID id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with id: " + id));
    }

    public Enrollment getEnrollmentByStudentAndCourse(UUID studentId, UUID courseId) {
        // Ensure student & course exist (meaningful 404s if not)
        studentService.getStudentById(studentId);
        courseService.getCourseById(courseId);

        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Enrollment not found for studentId=" + studentId + " and courseId=" + courseId));
    }

    // Get all enrollments for a student
    public List<Enrollment> getEnrollmentsByStudentId(UUID studentId) {
        // Verify student exists
        studentService.getStudentById(studentId);
        return enrollmentRepository.findByStudentId(studentId);
    }

    // Get all enrollments for a course
    public List<Enrollment> getEnrollmentsByCourseId(UUID courseId) {
        // Verify course exists
        courseService.getCourseById(courseId);
        return enrollmentRepository.findByCourseId(courseId);
    }

    // Get enrollments by status
    public List<Enrollment> getEnrollmentsByStatus(EnrollmentStatus status) {
        return enrollmentRepository.findByStatus(status);
    }

    // Update enrollment grade
    @Transactional
    public Enrollment updateGrade(UUID enrollmentId, String grade) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        enrollment.setGrade(grade);
        return enrollmentRepository.save(enrollment);
    }

    // Update enrollment status
    @Transactional
    public Enrollment updateStatus(UUID enrollmentId, EnrollmentStatus status) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        enrollment.setStatus(status);
        return enrollmentRepository.save(enrollment);
    }

    // Student API: fetch approved enrollments for student
    public List<Enrollment> getApprovedEnrollmentsForStudent(UUID studentId) {
        studentService.getStudentById(studentId);
        return enrollmentRepository.findByStudentIdAndStatus(studentId, EnrollmentStatus.APPROVED);
    }

    // Admin API: pending enrollments
    public List<Enrollment> getPendingEnrollments() {
        return enrollmentRepository.findByStatus(EnrollmentStatus.REQUESTED);
    }

    // Admin API: approve enrollment
    @Transactional
    public Enrollment approveEnrollment(UUID enrollmentId) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        enrollment.setStatus(EnrollmentStatus.APPROVED);
        enrollment.setApprovedAt(LocalDateTime.now());
        return enrollmentRepository.save(enrollment);
    }

    // Admin API: reject enrollment
    @Transactional
    public Enrollment rejectEnrollment(UUID enrollmentId) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        enrollment.setStatus(EnrollmentStatus.REJECTED);
        enrollment.setApprovedAt(null);
        return enrollmentRepository.save(enrollment);
    }

    // Delete enrollment (unenroll student)
    @Transactional
    public void deleteEnrollment(UUID enrollmentId) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        enrollmentRepository.delete(enrollment);
    }

    // Unenroll student from course
    @Transactional
    public void unenrollStudent(UUID studentId, UUID courseId) {
        Student student = studentService.getStudentById(studentId);
        Course course = courseService.getCourseById(courseId);

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                    "Student is not enrolled in this course"
                ));

        enrollmentRepository.delete(enrollment);
    }
}