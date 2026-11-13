package com.victor.saas_pagamentos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victor.saas_pagamentos.dto.LoginRequestDTO;
import com.victor.saas_pagamentos.repository.UserRepository;
import com.victor.saas_pagamentos.infra.security.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityAndAuthTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private AuthenticationManager authenticationManager;
    @MockBean private UserRepository userRepository;
    @MockBean private TokenService tokenService;

    @Test
    void deveRejeitarSemTokenCom403ou401() throws Exception {
        mockMvc.perform(get("/planos"))
                .andExpect(status().isForbidden()); //Alterado de isUnauthorized() para isForbidden()
    }

    @Test
    @WithMockUser(roles = "USER")
    void deveBloquearUserDeCriarPlanoCom403() throws Exception {
        mockMvc.perform(post("/planos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Premium\",\"valor\":99.90}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void devePermitirPreflightCorsOptions() throws Exception {
        mockMvc.perform(options("/assinaturas/1/cancelar")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "PATCH"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().string("Access-Control-Allow-Methods", "GET,POST,PUT,PATCH,DELETE,OPTIONS"));
    }

    @Test
    void deveRetornar401NoLoginComSenhaErrada() throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO("teste@teste.com", "senhaErrada");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userNaoPodeCriarAdmin() throws Exception {
        mockMvc.perform(post("/auth/register-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@test.com\",\"password\":\"senhaForte123\"}"))
                .andExpect(status().isForbidden());
    }
}