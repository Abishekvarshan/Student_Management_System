package com.example.studentmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.studentmanagement.exception.CourseAlreadyExistsException;
import com.example.studentmanagement.exception.CourseNotFoundException;
import com.example.studentmanagement.model.Course;
import com.example.studentmanagement.repository.CourseRepository;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Add a new course with validation
    public Course addCourse(Course course) {
        // Check if course code already exists
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new CourseAlreadyExistsException(
                "Course with code '" + course.getCourseCode() + "' already exists"
            );
        }
        return courseRepository.save(course);
    }

    // Get all courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Get course by ID
    public Course getCourseById(UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
    }

    // Get course by course code
    public Course getCourseByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with code: " + courseCode));
    }

    // Search courses by name
    public List<Course> searchCoursesByName(String courseName) {
        return courseRepository.findByCourseNameContainingIgnoreCase(courseName);
    }

    // Update course with validation
    // Update course with validation
public Course updateCourse(UUID id, Course courseDetails) {
    Course course = getCourseById(id);
    
    // Check if the new course code is different and already exists
    if (!course.getCourseCode().equals(courseDetails.getCourseCode()) && 
        courseRepository.existsByCourseCode(courseDetails.getCourseCode())) {
        throw new CourseAlreadyExistsException(
            "Course with code '" + courseDetails.getCourseCode() + "' already exists"
        );
    }
    
    course.setCourseName(courseDetails.getCourseName());
    course.setCourseCode(courseDetails.getCourseCode());
    course.setCredits(courseDetails.getCredits());
    course.setDescription(courseDetails.getDescription());
    course.setDuration(courseDetails.getDuration());  // Add this line
    
    return courseRepository.save(course);
}
    // Delete course
    public void deleteCourse(UUID id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }

    // Check if course exists
    public boolean courseExists(UUID id) {
        return courseRepository.existsById(id);
    }
}