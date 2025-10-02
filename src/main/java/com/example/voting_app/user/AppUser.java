package com.example.voting_app.user;

import com.example.voting_app.clazz.StudentClass;
import com.example.voting_app.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "[User]")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "StudentCode", length = 20, unique = true)
    private String studentCode; // nullable if not student

    @Column(name = "FullName", length = 100, nullable = false)
    private String fullName;

    @Column(name = "Email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "Gender", length = 1)
    private String gender;

    @Column(name = "BirthYear")
    private Integer birthYear;

    @Column(name = "Phone", length = 20)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClassID")
    private StudentClass studentClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoleID")
    private Role role;

    // getters and setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public StudentClass getStudentClass() { return studentClass; }
    public void setStudentClass(StudentClass studentClass) { this.studentClass = studentClass; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
