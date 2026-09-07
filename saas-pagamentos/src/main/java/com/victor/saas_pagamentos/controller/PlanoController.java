package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.model.Plano;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/planos")
public class PlanoController {

    //Lista temporária na memória ("banco de dados")
    private List<Plano> bancoDePlanos = new ArrayList<>();

    @PostMapping
    public String criarPlano(@RequestBody Plano novoPlano) {
        bancoDePlanos.add(novoPlano);
        return "✅ Plano '" + novoPlano.getNome() + "' criado com sucesso no valor de R$ " + novoPlano.getValor();
    }

    @GetMapping
    public List<Plano> listarPlanos() {
        return bancoDePlanos;
    }
}
