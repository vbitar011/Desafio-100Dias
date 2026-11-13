package com.victor.saas_pagamentos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SaasPagamentosApplicationTests {
	@Test
	void contextLoads() {
		// Se a aplicação chegar aqui, o H2 subiu sem pedir o MySQL ou o .env
	}
}