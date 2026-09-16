package com.victor.saas_pagamentos.dto;

import java.time.LocalDate;

public record PagamentoResponseDTO(
        Long id,
        Long assinaturaId,
        Double valor,
        LocalDate dataVencimento,
        String status
) {}