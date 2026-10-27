package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.dto.PagamentoRequestDTO;
import com.victor.saas_pagamentos.dto.PagamentoResponseDTO;
import com.victor.saas_pagamentos.model.Pagamento;
import com.victor.saas_pagamentos.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @GetMapping
    public ResponseEntity<List<PagamentoResponseDTO>> listarPagamentos() {
        List<PagamentoResponseDTO> lista = pagamentoService.listar().stream()
                .map(this::mapearParaDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> criarPagamento(@RequestBody @Valid PagamentoRequestDTO dto) {
        //Passamos apenas o ID da assinatura que vem no DTO
        Pagamento pagamento = pagamentoService.gerarPagamento(dto.assinaturaId());
        return ResponseEntity.ok(mapearParaDTO(pagamento));
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<PagamentoResponseDTO> pagarFatura(@PathVariable Long id) {
        //Chamamos o método correto: pagar()
        Pagamento pagamento = pagamentoService.pagar(id);
        return ResponseEntity.ok(mapearParaDTO(pagamento));
    }

    private PagamentoResponseDTO mapearParaDTO(Pagamento pagamento) {
        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getAssinatura().getId(),
                pagamento.getValor(),
                pagamento.getDataVencimento(),
                pagamento.getStatus().name()
        );
    }
}