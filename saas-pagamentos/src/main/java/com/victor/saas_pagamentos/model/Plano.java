package com.victor.saas_pagamentos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Entity
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //A chave primária, gerada automaticamente pelo banco.

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotNull
    @Positive
    @Column(precision = 12, scale = 2)
    private BigDecimal valor;

    //Construtores
    public Plano() {}

    public Plano(String nome, BigDecimal valor) {
        this.nome = nome;
        this.valor = valor;
    }

    public Long getId() { return id; }
    //Não criei setId porque o banco de dados controla isso.

    //Getters e Setters para o Spring Boot converter de/para JSON
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public void setId(Long id) { this.id = id; }
}
