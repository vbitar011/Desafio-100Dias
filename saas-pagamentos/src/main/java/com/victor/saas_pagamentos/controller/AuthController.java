package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.dto.LoginRequestDTO;
import com.victor.saas_pagamentos.dto.RegistroRequestDTO;
import com.victor.saas_pagamentos.dto.TokenResponseDTO;
import com.victor.saas_pagamentos.infra.security.TokenService;
import com.victor.saas_pagamentos.model.User;
import com.victor.saas_pagamentos.model.UserRole;
import com.victor.saas_pagamentos.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new TokenResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegistroRequestDTO data) {
        if (this.repository.findByEmail(data.email()) != null) {
            throw new RuntimeException("E-mail já cadastrado no sistema");
        }

        String encryptedPassword = passwordEncoder.encode(data.senha());
        //Força papel USER para evitar elevação de privilégios via API
        User newUser = new User(data.email(), encryptedPassword, UserRole.USER);

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}