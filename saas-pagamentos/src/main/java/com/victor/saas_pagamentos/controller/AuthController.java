package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.dto.*;
import com.victor.saas_pagamentos.infra.RegraDeNegocioException;
import com.victor.saas_pagamentos.infra.security.TokenService;
import com.victor.saas_pagamentos.model.Cliente;
import com.victor.saas_pagamentos.model.User;
import com.victor.saas_pagamentos.model.UserRole;
import com.victor.saas_pagamentos.repository.ClienteRepository;
import com.victor.saas_pagamentos.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    public AuthController(UserRepository userRepository, ClienteRepository clienteRepository, TokenService tokenService, PasswordEncoder passwordEncoder, AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.clienteRepository = clienteRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
        var auth = authManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegistroRequestDTO dto) {
        if (userRepository.findByEmail(dto.email()) != null) throw new RegraDeNegocioException("E-mail já está em uso.");

        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        clienteRepository.save(cliente);

        User newUser = new User();
        newUser.setEmail(dto.email());
        newUser.setPassword(passwordEncoder.encode(dto.password()));
        newUser.setRole(UserRole.USER);
        newUser.setCliente(cliente); // Associa
        userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register-admin")
    public ResponseEntity<Void> registerAdmin(@RequestBody @Valid RegistroAdminRequestDTO dto) {
        if (userRepository.findByEmail(dto.email()) != null) {
            throw new RegraDeNegocioException("E-mail já está em uso.");
        }

        User newUser = new User();
        newUser.setEmail(dto.email());
        newUser.setPassword(passwordEncoder.encode(dto.password()));
        newUser.setRole(UserRole.ADMIN);
        //Não vinculamos Cliente, pois ADMIN é apenas de sistema
        userRepository.save(newUser);

        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).build();
    }
}