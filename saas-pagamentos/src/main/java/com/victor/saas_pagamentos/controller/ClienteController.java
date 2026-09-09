package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.model.Cliente;
import com.victor.saas_pagamentos.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Cliente criarCliente(@Valid @RequestBody Cliente novoCliente) {
        return repository.save(novoCliente);
    }

    @GetMapping
    public List<Cliente> listarClientes() {
        return repository.findAll();
    }
}