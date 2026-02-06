package com.example.studentmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.studentmanagement.exception.StudentAlreadyExistsException;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;  // Add this import

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Add a new student
     public Student addStudent(Student student) {
        // Check if email already exists
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new StudentAlreadyExistsException(
                "Student with email '" + student.getEmail() + "' already exists"
            );
        }
        return studentRepository.save(student);
    }
    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Student getStudentById(UUID id) {  // Changed from Long to UUID
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public Student getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            throw new RuntimeException("Student not found with email: " + email);
        }
        return student;
    }

    // Update student
    public Student updateStudent(UUID id, Student studentDetails) {  // Changed from Long to UUID
        Student student = getStudentById(id);
        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());
        student.setAge(studentDetails.getAge());
        return studentRepository.save(student);
    }

    // Delete student
    public void deleteStudent(UUID id) {  // Changed from Long to UUID
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }
}