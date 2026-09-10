package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.model.Assinatura;
import com.victor.saas_pagamentos.repository.AssinaturaRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assinaturas")
public class AssinaturaController {

    private final AssinaturaRepository repository;

    public AssinaturaController(AssinaturaRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Assinatura criarAssinatura(@Valid @RequestBody Assinatura novaAssinatura) {
        return repository.save(novaAssinatura);
    }

    @GetMapping
    public List<Assinatura> listarAssinaturas() {
        return repository.findAll();
    }
}