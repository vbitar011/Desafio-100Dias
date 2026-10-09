package com.victor.saas_pagamentos.dto;

import java.time.LocalDate;

public record AssinaturaResponseDTO(
        Long id,
        Long clienteId, //Em vez do objeto Cliente inteiro, só o ID
        Long planoId,   //Em vez do objeto Plano inteiro, só o ID
        LocalDate dataInicio,
        String status
) {}