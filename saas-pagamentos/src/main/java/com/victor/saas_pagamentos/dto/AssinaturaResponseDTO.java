package com.victor.saas_pagamentos.dto;

import java.time.LocalDate;

public record AssinaturaResponseDTO(
        Long id,
        Long clienteId,
        Long planoId,
        LocalDate dataInicio,
        String status
) {}