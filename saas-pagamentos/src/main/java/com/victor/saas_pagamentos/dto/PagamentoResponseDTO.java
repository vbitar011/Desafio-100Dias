package com.victor.saas_pagamentos.dto;

import java.time.LocalDate;

public record PagamentoResponseDTO(
        Long id,
        Double valor,
        LocalDate dataVencimento,
        String status,
        Long assinaturaId
) {}