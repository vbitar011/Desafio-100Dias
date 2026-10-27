package com.victor.saas_pagamentos.infra.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.victor.saas_pagamentos.dto.ErroValidacaoDTO;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroResponseDTO(LocalDateTime timestamp, Integer status, String erro, String mensagem, String caminho, List<ErroValidacaoDTO> campos) {
    public ErroResponseDTO(LocalDateTime timestamp, Integer status, String erro, String mensagem, String caminho) {
        this(timestamp, status, erro, mensagem, caminho, null);
    }
}