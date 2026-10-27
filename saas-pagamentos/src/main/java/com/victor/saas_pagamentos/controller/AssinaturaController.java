package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.dto.AssinaturaRequestDTO;
import com.victor.saas_pagamentos.dto.AssinaturaResponseDTO;
import com.victor.saas_pagamentos.model.Assinatura;
import com.victor.saas_pagamentos.service.AssinaturaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assinaturas")
public class AssinaturaController {

    private final AssinaturaService assinaturaService;

    public AssinaturaController(AssinaturaService assinaturaService) {
        this.assinaturaService = assinaturaService;
    }

    @PostMapping
    public ResponseEntity<AssinaturaResponseDTO> criarAssinatura(@RequestBody @Valid AssinaturaRequestDTO dto) {
        Assinatura salva = assinaturaService.criar(dto);
        return ResponseEntity.ok(new AssinaturaResponseDTO(salva.getId(), salva.getCliente().getId(), salva.getPlano().getId(), salva.getDataInicio(), salva.getStatus().name()));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AssinaturaResponseDTO> cancelarAssinatura(@PathVariable Long id) {
        Assinatura cancelada = assinaturaService.cancelar(id);
        return ResponseEntity.ok(new AssinaturaResponseDTO(cancelada.getId(), cancelada.getCliente().getId(), cancelada.getPlano().getId(), cancelada.getDataInicio(), cancelada.getStatus().name()));
    }

    @GetMapping
    public ResponseEntity<List<AssinaturaResponseDTO>> listarAssinaturas() {
        List<AssinaturaResponseDTO> lista = assinaturaService.listar().stream()
                .map(a -> new AssinaturaResponseDTO(a.getId(), a.getCliente().getId(), a.getPlano().getId(), a.getDataInicio(), a.getStatus().name()))
                .toList();
        return ResponseEntity.ok(lista);
    }
}