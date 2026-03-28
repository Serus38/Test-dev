package com.testdev.test_dev.controller;

import com.testdev.test_dev.config.JwtUtil;
import com.testdev.test_dev.dto.LoginRequest;
import com.testdev.test_dev.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {
    "http://localhost:4200",
    "http://localhost:8081",
    "test-dev-production-33f8.up.railway.app"
})
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Usuarios de ejemplo (En producción, validar contra la base de datos)
    private static final String DEMO_USERNAME = "admin";
    private static final String DEMO_PASSWORD = "password123";

    /**
     * Login de usuario: valida credenciales y retorna JWT + metadata de sesion.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // Validación de credenciales
        // En producción, buscar el usuario en la base de datos y comparar contraseñas encriptadas
        if (!isValidUser(loginRequest.getUsername(), loginRequest.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        // Generar token JWT
        String token = jwtUtil.generateToken(loginRequest.getUsername());
        
        LoginResponse response = new LoginResponse(
                token,
                loginRequest.getUsername(),
                86400000 // 24 horas en milisegundos
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de verificacion rapida para saber si el token enviado sigue siendo valido.
     */
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.ok(false);
            }

            String token = authorizationHeader.substring(7);
            boolean isValid = jwtUtil.validateToken(token);
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    /**
     * Validacion temporal de demo. Reemplazar por consulta a BD + verificacion de hash.
     */
    private boolean isValidUser(String username, String password) {
        // En producción, validar contra la base de datos
        // Por ahora, usar credenciales de demostración
        return DEMO_USERNAME.equals(username) && DEMO_PASSWORD.equals(password);
    }
}

