CREATE TABLE IF NOT EXISTS tb_user(
    ID BIGSERIAL PRIMARY KEY,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    cpf varchar(15) UNIQUE,
    cnpj varchar(25) UNIQUE,
    email varchar(50) UNIQUE NOT NULL,
    type varchar(15) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_transfer(
    ID BIGSERIAL PRIMARY KEY,
    value DECIMAL NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL
);