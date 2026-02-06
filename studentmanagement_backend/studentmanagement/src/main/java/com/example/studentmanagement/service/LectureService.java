package com.example.studentmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.studentmanagement.model.Lecture;
import com.example.studentmanagement.repository.LectureRepository;

@Service
public class LectureService {

    private final LectureRepository lectureRepository;

    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public Lecture createLecture(Lecture lecture) {
        lecture.setId(null);
        return lectureRepository.save(lecture);
    }

    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    public Lecture getLectureById(UUID id) {
        return lectureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecture not found with id: " + id));
    }

    public Lecture updateLecture(UUID id, Lecture details) {
        Lecture lecture = getLectureById(id);
        lecture.setName(details.getName());
        lecture.setDepartment(details.getDepartment());
        lecture.setRating(details.getRating());
        lecture.setSubjects(details.getSubjects());
        lecture.setCodes(details.getCodes());
        lecture.setContact(details.getContact());
        return lectureRepository.save(lecture);
    }

    public void deleteLecture(UUID id) {
        Lecture lecture = getLectureById(id);
        lectureRepository.delete(lecture);
    }
}