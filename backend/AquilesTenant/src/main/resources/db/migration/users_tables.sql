CREATE TABLE IF NOT EXISTS users (
    id bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    dni varchar(50) DEFAULT NULL,
    email varchar(100) NOT NULL,
    is_active bit(1) NOT NULL,
    lastname varchar(150) DEFAULT NULL,
    name varchar(150) NOT NULL,
    password varchar(255) NOT NULL,
    phone varchar(20) DEFAULT NULL,
    regional_id smallint DEFAULT NULL,
    UNIQUE KEY UK_6dotkott2kjsp8vw4d0m25fb7 (email),
    UNIQUE KEY UK_6aphui3g30h49muho4c91n0yl (dni)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS roles (
    id bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    rol enum('TENANT_OWNER', 'USERS_ADMIN', 'CUSTOMER', 'COMMERCIAL_ADVISOR', 'REGIONAL_DIRECTOR') NOT NULL,
    tenant_id varchar(50) DEFAULT NULL,
    user_id bigint DEFAULT NULL,
    KEY FK97mxvrajhkq19dmvboprimeg1 (user_id),
    CONSTRAINT FK97mxvrajhkq19dmvboprimeg1 FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;