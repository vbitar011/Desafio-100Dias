package com.victor.saas_pagamentos.dto;

import jakarta.validation.constraints.NotNull;

public record AssinaturaRequestDTO(
        @NotNull(message = "O ID do cliente é obrigatório.")
        Long clienteId,

        @NotNull(message = "O ID do plano é obrigatório.")
        Long planoId
) {}