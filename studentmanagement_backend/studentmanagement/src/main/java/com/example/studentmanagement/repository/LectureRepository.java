package com.example.studentmanagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentmanagement.model.Lecture;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, UUID> {
}