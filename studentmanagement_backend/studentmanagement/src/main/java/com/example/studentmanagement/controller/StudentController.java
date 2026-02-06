package com.example.studentmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;  // Add this import
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.security.CurrentUserService;
import com.example.studentmanagement.service.StudentService;
import com.example.studentmanagement.model.User;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;

    @Autowired
    public StudentController(StudentService studentService, CurrentUserService currentUserService,
            UserRepository userRepository) {
        this.studentService = studentService;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
    }

    // POST: Create new student
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LECTURE')")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.addStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    // GET: Fetch all students
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LECTURE')")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // GET: Fetch current student's profile
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURE','STUDENT')")
    public ResponseEntity<Student> getMyProfile() {
        String username = currentUserService.getCurrentUsername()
                .orElseThrow(() -> new RuntimeException("Unauthorized"));
        userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Student student = studentService.getStudentByEmail(username);
        return ResponseEntity.ok(student);
    }

    // GET: Fetch student by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURE','STUDENT')")
    public ResponseEntity<Student> getStudentById(@PathVariable UUID id) {  // Changed from Long to UUID
        enforceStudentOwnership(id);
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    // PUT: Update student
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURE','STUDENT')")
    public ResponseEntity<Student> updateStudent(@PathVariable UUID id, @RequestBody Student student) {  // Changed from Long to UUID
        enforceStudentOwnership(id);
        Student updatedStudent = studentService.updateStudent(id, student);
        return ResponseEntity.ok(updatedStudent);
    }

    // DELETE: Delete student
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID id) {  // Changed from Long to UUID
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    private void enforceStudentOwnership(UUID studentId) {
        String username = currentUserService.getCurrentUsername()
                .orElseThrow(() -> new RuntimeException("Unauthorized"));
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("STUDENT".equals(user.getRole())) {
            Student student = studentService.getStudentById(studentId);
            if (!user.getEmail().equalsIgnoreCase(student.getEmail())) {
                throw new RuntimeException("Access denied: students can only access their own profile");
            }
        }
    }
}