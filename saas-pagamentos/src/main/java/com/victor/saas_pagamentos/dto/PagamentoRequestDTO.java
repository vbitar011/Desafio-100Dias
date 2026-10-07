package com.victor.saas_pagamentos.dto;

import jakarta.validation.constraints.NotNull;

public record PagamentoRequestDTO(
        @NotNull(message = "O ID da assinatura é obrigatório.")
        Long assinaturaId
) {}