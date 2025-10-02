package com.example.voting_app.clazz;

import com.example.voting_app.faculty.Faculty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "[Class]")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StudentClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ClassID")
    private Integer classId;

    @Column(name = "ClassName", length = 50, nullable = false)
    private String className;

    @Column(name = "YearStart")
    private Integer yearStart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FacultyID")
    private Faculty faculty;

    public Integer getClassId() { return classId; }
    public void setClassId(Integer classId) { this.classId = classId; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public Integer getYearStart() { return yearStart; }
    public void setYearStart(Integer yearStart) { this.yearStart = yearStart; }
    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }
}
