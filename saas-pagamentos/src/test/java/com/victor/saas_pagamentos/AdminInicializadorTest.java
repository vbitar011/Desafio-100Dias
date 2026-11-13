package com.victor.saas_pagamentos;

import com.victor.saas_pagamentos.infra.security.AdminInicializador;
import com.victor.saas_pagamentos.model.User;
import com.victor.saas_pagamentos.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminInicializadorTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private AdminInicializador adminInicializador;

    @Test
    void naoDeveCriarAdminSeVariaveisAusentes() {
        ReflectionTestUtils.setField(adminInicializador, "adminEmail", null);
        adminInicializador.run(new DefaultApplicationArguments());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deveCriarAdminQuandoNaoExiste() {
        ReflectionTestUtils.setField(adminInicializador, "adminEmail", "admin@test.com");
        ReflectionTestUtils.setField(adminInicializador, "adminSenha", "senhaSegura123");

        when(userRepository.findByEmail("admin@test.com")).thenReturn(null);
        when(passwordEncoder.encode("senhaSegura123")).thenReturn("hash");

        adminInicializador.run(new DefaultApplicationArguments());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void naoDeveDuplicarAdminExistente() {
        ReflectionTestUtils.setField(adminInicializador, "adminEmail", "admin@test.com");
        ReflectionTestUtils.setField(adminInicializador, "adminSenha", "senhaSegura123");

        when(userRepository.findByEmail("admin@test.com")).thenReturn(new User());

        adminInicializador.run(new DefaultApplicationArguments());
        verify(userRepository, never()).save(any());
    }
}