package com.victor.saas_pagamentos.infra.exception;

import java.util.List;

public record ErroResponseDTO(String mensagem, List<String> campos) {}