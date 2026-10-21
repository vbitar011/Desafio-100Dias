package com.victor.saas_pagamentos.dto;

import java.math.BigDecimal;

public record PlanoResponseDTO(Long id, String nome, BigDecimal valor) {}