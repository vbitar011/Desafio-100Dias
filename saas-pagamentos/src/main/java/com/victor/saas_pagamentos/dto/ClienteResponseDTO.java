package com.victor.saas_pagamentos.dto;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String email
) {}