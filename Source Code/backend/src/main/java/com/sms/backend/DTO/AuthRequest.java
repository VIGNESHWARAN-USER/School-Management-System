package com.sms.backend.DTO;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
    private String role;
}