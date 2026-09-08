package com.victor.saas_pagamentos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //A chave primária, gerada automaticamente pelo banco.

    @NotBlank(message = "O nome do plano é obrigatório.")
    private String nome;

    @Positive(message = "O valor do plano deve ser maior que zero.")
    private double valor;

    //Construtores
    public Plano() {}

    public Plano(String nome, double valor) {
        this.nome = nome;
        this.valor = valor;
    }

    public Long getId() { return id; }
    //Não criei setId porque o banco de dados controla isso.

    //Getters e Setters para o Spring Boot converter de/para JSON
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
}
