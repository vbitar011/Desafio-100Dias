package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.model.Plano;
import com.victor.saas_pagamentos.repository.PlanoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/planos")
public class PlanoController {

    private final PlanoRepository repository;

    //Injetando dependência via construtor
    public PlanoController(PlanoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Plano criarPlano(@RequestBody Plano novoPlano) {
        //O método save() grava no banco de dados e retorna o objeto com o ID preenchido
        return repository.save(novoPlano);
    }

    @GetMapping
    public List<Plano> listarPlanos() {
        //O método findAll() faz um "SELECT * FROM PLANO" e retorna a lista
        return repository.findAll();
    }
}
