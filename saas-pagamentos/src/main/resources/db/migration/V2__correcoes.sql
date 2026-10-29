-- Corrige imprecisões financeiras
ALTER TABLE plano MODIFY COLUMN valor DECIMAL(12,2) NOT NULL;
ALTER TABLE pagamento MODIFY COLUMN valor DECIMAL(12,2) NOT NULL;

-- Adiciona suporte a Optimistic Locking
ALTER TABLE pagamento ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

-- Impede duplicação de contas e clientes
ALTER TABLE cliente ADD CONSTRAINT uk_cliente_email UNIQUE (email);
ALTER TABLE users ADD CONSTRAINT uk_users_email UNIQUE (email);

-- Vincula o User ao Cliente para o RBAC (Ownership)
ALTER TABLE users ADD COLUMN cliente_id BIGINT;
ALTER TABLE users ADD CONSTRAINT fk_users_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id);