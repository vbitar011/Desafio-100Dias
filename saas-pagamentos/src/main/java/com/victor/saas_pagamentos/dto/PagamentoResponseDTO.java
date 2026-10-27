package com.victor.saas_pagamentos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PagamentoResponseDTO(
        Long id,
        Long assinaturaId,
        BigDecimal valor,
        LocalDate dataVencimento,
        String status
) {}