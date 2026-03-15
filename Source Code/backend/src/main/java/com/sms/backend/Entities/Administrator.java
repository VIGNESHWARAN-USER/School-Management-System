package com.sms.backend.Entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Administrator {
    @Id
    private Long adminId;

    private String adminName;
    private String email;
    private String password;
}
