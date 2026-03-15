package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Parent {

    @Id
    private Long parentId;

    private String parentName;
    private int age;
    private String mobileNumber;
    private String email;
    private String password;
    private String address;
}