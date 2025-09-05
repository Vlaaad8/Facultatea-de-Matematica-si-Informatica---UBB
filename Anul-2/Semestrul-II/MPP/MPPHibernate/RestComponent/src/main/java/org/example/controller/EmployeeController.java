package org.example.controller;

import io.jsonwebtoken.Jwts;
import org.example.clientfx.Employee;
import org.example.clientfx.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("ryanair/login")
@CrossOrigin(origins="http://localhost:5174")

public class EmployeeController {
    private static final long EXPIRATION_TIME=360000;

    @Autowired
    private EmployeeRepository employeeRepository;

    public static String generateJWToken(String username){
        String token=Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(JWTUtils.getKey(),Jwts.SIG.HS512)
                .compact()
                .trim();
        System.out.println(token);
        return token;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Employee employee=employeeRepository.login(loginRequest.username(),loginRequest.password()).orElse(null);
        if(employee==null) {
            return ResponseEntity.notFound().build();
        }
        System.out.println("Am inceput sa generez un token pentru login!");
        String token=generateJWToken(loginRequest.username());
        System.out.println(token);
        return ResponseEntity.ok(token);
    }
}
