package com.victor.saas_pagamentos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //A chave primária, gerada automaticamente pelo banco.

    private String nome;
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
