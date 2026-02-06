package com.example.studentmanagement.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentmanagement.model.Course;
import com.example.studentmanagement.model.Enrollment;
import com.example.studentmanagement.model.EnrollmentStatus;
import com.example.studentmanagement.model.Student;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    
    // Find all enrollments for a specific student
    List<Enrollment> findByStudent(Student student);
    
    // Find all enrollments for a specific course
    List<Enrollment> findByCourse(Course course);
    
    // Find enrollment by student and course
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);

    Optional<Enrollment> findByStudentIdAndCourseId(UUID studentId, UUID courseId);
    
    // Find enrollments by student ID
    List<Enrollment> findByStudentId(UUID studentId);
    
    // Find enrollments by course ID
    List<Enrollment> findByCourseId(UUID courseId);
    
    // Find enrollments by status
    List<Enrollment> findByStatus(EnrollmentStatus status);
    
    // Check if student is already enrolled in a course
    boolean existsByStudentAndCourse(Student student, Course course);

    // For student API
    boolean existsByStudentIdAndCourseId(UUID studentId, UUID courseId);

    List<Enrollment> findByStudentIdAndStatus(UUID studentId, EnrollmentStatus status);
}