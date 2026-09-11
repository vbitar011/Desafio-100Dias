package com.victor.saas_pagamentos.dto;

import java.time.LocalDate;

public record AssinaturaResponseDTO(
        Long id,
        String nomeCliente,
        String nomePlano,
        LocalDate dataInicio,
        String status
) {}