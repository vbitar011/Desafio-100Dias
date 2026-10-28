package com.victor.saas_pagamentos.infra.security;

import com.victor.saas_pagamentos.model.User;
import com.victor.saas_pagamentos.model.UserRole;
import com.victor.saas_pagamentos.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInicializador implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminInicializador.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.senha}")
    private String adminSenha;

    public AdminInicializador(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (adminEmail == null || adminSenha == null || adminEmail.isBlank() || adminSenha.isBlank()) {
            logger.info("Variáveis APP_ADMIN_EMAIL e APP_ADMIN_PASSWORD ausentes. Nenhum ADMIN inicial criado.");
            return;
        }

        //Validação manual básica de e-mail e estrita de senha sem expor a senha no Log
        if (!adminEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalStateException("Falha no Bootstrap: APP_ADMIN_EMAIL possui um formato inválido.");
        }
        if (adminSenha.length() < 8 || adminSenha.length() > 72) {
            throw new IllegalStateException("Falha no Bootstrap: APP_ADMIN_PASSWORD deve ter entre 8 e 72 caracteres.");
        }

        if (userRepository.findByEmail(adminEmail) == null) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminSenha));
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
            logger.info("Usuário ADMIN inicial ('{}') criado com sucesso via variáveis de ambiente.", adminEmail);
        } else {
            logger.info("O e-mail configurado para o ADMIN inicial já existe. Nenhuma ação realizada.");
        }
    }
}