package com.sms.backend.Controllers;

import com.sms.backend.Config.JwtUtils;
import com.sms.backend.DTO.AuthRequest;
import com.sms.backend.DTO.AuthResponse;
import com.sms.backend.Services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService studentUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            CustomUserDetailsService userDetailsService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.studentUserDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            System.out.println(authRequest.getEmail());
            System.out.println(authRequest.getPassword());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );

            final UserDetails userDetails = studentUserDetailsService.loadUserByUsername(authRequest.getEmail());

            System.out.println("RAW PASSWORD: " + authRequest.getPassword());
            System.out.println("DB PASSWORD: " + userDetails.getPassword());

            System.out.println("MATCH: " +
                    passwordEncoder.matches(
                            authRequest.getPassword(),
                            userDetails.getPassword()
                    ));


            final String token = jwtUtils.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(token, userDetails));
        }
        catch(UsernameNotFoundException e)
        {
            return ResponseEntity.status(404).body("User Not Found");
        }
        catch (AuthenticationException e) {
            return ResponseEntity.status(404).body("Invalid credentials");
        }

    }
}
