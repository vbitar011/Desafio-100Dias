package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.dto.ClienteRequestDTO;
import com.victor.saas_pagamentos.dto.ClienteResponseDTO;
import com.victor.saas_pagamentos.model.Cliente;
import com.victor.saas_pagamentos.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ClienteResponseDTO> criarCliente(@RequestBody @Valid ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());

        Cliente salvo = repository.save(cliente);

        return ResponseEntity.ok(new ClienteResponseDTO(salvo.getId(), salvo.getNome(), salvo.getEmail()));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarClientes() {
        List<ClienteResponseDTO> clientes = repository.findAll().stream()
                .map(c -> new ClienteResponseDTO(c.getId(), c.getNome(), c.getEmail()))
                .toList();

        return ResponseEntity.ok(clientes);
    }
}