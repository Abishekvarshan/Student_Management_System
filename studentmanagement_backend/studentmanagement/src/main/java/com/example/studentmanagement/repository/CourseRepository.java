package com.example.studentmanagement.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentmanagement.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    
    // Check if course code already exists
    boolean existsByCourseCode(String courseCode);
    
    // Find course by course code
    Optional<Course> findByCourseCode(String courseCode);
    
    // Search courses by name (case-insensitive, partial match)
    java.util.List<Course> findByCourseNameContainingIgnoreCase(String courseName);
}