package com.victor.saas_pagamentos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Assinatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O cliente é obrigatório.")
    @ManyToOne //Muitas assinaturas podem pertencer a um cliente
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @NotNull(message = "O plano é obrigatório.")
    @ManyToOne //Muitas assinaturas podem estar ligadas a um plano
    @JoinColumn(name = "plano_id")
    private Plano plano;

    //Dados de controle da assinatura
    private LocalDate dataInicio = LocalDate.now();
    private String status = "ATIVA";

    public Assinatura() {}

    public Assinatura(Cliente cliente, Plano plano) {
        this.cliente = cliente;
        this.plano = plano;
    }

    //Getters e Setters
    public Long getId() { return id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Plano getPlano() { return plano; }
    public void setPlano(Plano plano) { this.plano = plano; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}