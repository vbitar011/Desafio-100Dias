package com.victor.saas_pagamentos.repository;

import com.victor.saas_pagamentos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    //Método necessário para o Spring Security buscar o utilizador no banco
    User findByEmail(String email);
}