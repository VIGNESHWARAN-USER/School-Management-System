package com.sms.backend.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String parentEmail;
    private String name;
    private int age;
    private String email;
    private String password;
    private String classId;
    private String address;

    // Parent Relation
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Parent parent;

    //  Attendance
    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private List<StudentAttendance> attendenceList;

    //  Registrations
    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private List<Registration> registrationList;

}