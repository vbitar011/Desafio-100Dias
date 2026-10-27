package com.victor.saas_pagamentos.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistroRequestDTO(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String password
) {}