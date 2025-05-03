package com.filo.yazilimmuh_filo.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate createdDate;
    @Lob
    private String content;

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private User generatedBy;

    public void generate() {
        this.createdDate = LocalDate.now();
        // generation logic...
    }

    public byte[] export(String format) {
        // export to PDF/CSV...
        return new byte[0];
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(User generatedBy) {
        this.generatedBy = generatedBy;
    }
}