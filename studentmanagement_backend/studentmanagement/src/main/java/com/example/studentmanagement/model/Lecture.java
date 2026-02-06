package com.example.studentmanagement.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "lectures")
public class Lecture {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "rating")
    private Double rating;

    @ElementCollection
    @CollectionTable(name = "lecture_subjects", joinColumns = @JoinColumn(name = "lecture_id"))
    @Column(name = "subject")
    private List<String> subjects = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "lecture_codes", joinColumns = @JoinColumn(name = "lecture_id"))
    @Column(name = "code")
    private List<String> codes = new ArrayList<>();

    @Column(name = "contact")
    private String contact;

    public Lecture() {
    }

    public Lecture(String name, String department, Double rating, List<String> subjects, List<String> codes,
            String contact) {
        this.name = name;
        this.department = department;
        this.rating = rating;
        this.subjects = subjects;
        this.codes = codes;
        this.contact = contact;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}