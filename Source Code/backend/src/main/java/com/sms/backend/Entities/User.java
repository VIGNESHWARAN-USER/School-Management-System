package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    private Long userId;

    private String name;
    private String email;
    private String role; // Admin / Teacher / Parent / Student
}