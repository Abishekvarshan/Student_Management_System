package com.example.studentmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.studentmanagement.exception.EnrollmentAlreadyExistsException;
import com.example.studentmanagement.exception.EnrollmentNotFoundException;
import com.example.studentmanagement.model.Course;
import com.example.studentmanagement.model.Enrollment;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.EnrollmentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

    // Enroll a student in a course
    @Transactional
    public Enrollment enrollStudent(UUID studentId, UUID courseId) {
        Student student = studentService.getStudentById(studentId);
        Course course = courseService.getCourseById(courseId);

        // Check if already enrolled
        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new EnrollmentAlreadyExistsException(
                "Student '" + student.getName() + "' is already enrolled in course '" + course.getCourseName() + "'"
            );
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus("ACTIVE");

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
    public List<Enrollment> getEnrollmentsByStatus(String status) {
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
    public Enrollment updateStatus(UUID enrollmentId, String status) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        enrollment.setStatus(status);
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