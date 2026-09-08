package com.victor.saas_pagamentos.repository;

import com.victor.saas_pagamentos.model.Plano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanoRepository extends JpaRepository<Plano, Long> {
    //Ao estender JpaRepository, ganho métodos como save(), findAll(), findById().
}