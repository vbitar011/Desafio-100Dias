package com.victor.saas_pagamentos.dto;

import java.time.LocalDate;

public record PagamentoResponseDTO(
        Long id,
        java.math.@jakarta.validation.constraints.NotNull(message = "O valor do pagamento é obrigatório.") @jakarta.validation.constraints.Positive(message = "O valor da cobrança não pode ser negativo ou zero.") BigDecimal valor,
        LocalDate dataVencimento,
        com.victor.saas_pagamentos.model.StatusPagamento status,
        Long assinaturaId
) {}