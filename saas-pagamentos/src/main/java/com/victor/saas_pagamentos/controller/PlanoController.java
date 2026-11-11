package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.dto.PlanoRequestDTO;
import com.victor.saas_pagamentos.dto.PlanoResponseDTO;
import com.victor.saas_pagamentos.model.Plano;
import com.victor.saas_pagamentos.repository.PlanoRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planos")
public class PlanoController {

    private final PlanoRepository repository;

    public PlanoController(PlanoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<PlanoResponseDTO> criarPlano(@RequestBody @Valid PlanoRequestDTO dto) {
        Plano plano = new Plano();
        plano.setNome(dto.nome());
        plano.setValor(dto.valor());

        Plano salvo = repository.save(plano);

        return ResponseEntity.ok(new PlanoResponseDTO(salvo.getId(), salvo.getNome(), salvo.getValor()));
    }

    @GetMapping
    public ResponseEntity<Page<PlanoResponseDTO>> listarPlanos(@PageableDefault(size = 10) Pageable pageable) {
        Page<Plano> planos = repository.findAll(pageable);
        Page<PlanoResponseDTO> page = planos.map(p -> new PlanoResponseDTO(p.getId(), p.getNome(), p.getValor()));
        return ResponseEntity.ok(page);
    }
}