package com.victor.saas_pagamentos.model;

public class Plano {
    private String nome;
    private double valor;

    //Construtores
    public Plano() {}

    public Plano(String nome, double valor) {
        this.nome = nome;
        this.valor = valor;
    }

    //Getters e Setters para o Spring Boot converter de/para JSON
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
}
