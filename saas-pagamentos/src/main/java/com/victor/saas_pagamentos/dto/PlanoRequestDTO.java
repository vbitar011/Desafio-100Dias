package com.victor.saas_pagamentos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PlanoRequestDTO(
        @NotBlank(message = "O nome do plano é obrigatório.")
        String nome,

        @NotNull(message = "O valor do plano é obrigatório.")
        @Positive(message = "O valor do plano deve ser maior que zero.")
        Double valor
) {}