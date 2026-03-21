package com.sms.backend.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String mobileNumber;
    private String email;
    private String password;
    private String address;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
    private List<Student> studentList;
}