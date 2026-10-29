CREATE TABLE cliente (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL
);

CREATE TABLE plano (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       nome VARCHAR(255) NOT NULL,
                       valor DOUBLE NOT NULL
);

CREATE TABLE assinatura (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            cliente_id BIGINT NOT NULL,
                            plano_id BIGINT NOT NULL,
                            data_inicio DATE NOT NULL,
                            status VARCHAR(50) NOT NULL,
                            CONSTRAINT fk_assinatura_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
                            CONSTRAINT fk_assinatura_plano FOREIGN KEY (plano_id) REFERENCES plano(id)
);

CREATE TABLE pagamento (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           assinatura_id BIGINT NOT NULL,
                           valor DOUBLE NOT NULL,
                           data_vencimento DATE NOT NULL,
                           status VARCHAR(50) NOT NULL,
                           CONSTRAINT fk_pagamento_assinatura FOREIGN KEY (assinatura_id) REFERENCES assinatura(id)
);

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);