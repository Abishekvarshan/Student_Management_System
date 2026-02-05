package com.example.studentmanagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentmanagement.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    // Check if email already exists
    boolean existsByEmail(String email);
    
    // Optional: Find student by email
    Student findByEmail(String email);
}