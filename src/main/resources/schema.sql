-- Phonebook Database Schema

-- Create division table
CREATE TABLE IF NOT EXISTS division
(
    id        UUID PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    is_active BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Create department table
CREATE TABLE IF NOT EXISTS department
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    division_id UUID,
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    FOREIGN KEY (division_id) REFERENCES division (id)
);

-- Create employee table
CREATE TABLE IF NOT EXISTS employee
(
    id            UUID PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    position      VARCHAR(255) NOT NULL,
    phone         VARCHAR(50)  NOT NULL,
    email         VARCHAR(255),
    department_id UUID,
    created_at    TIMESTAMP    NOT NULL,
    updated_at    TIMESTAMP    NOT NULL,
    is_active     BOOLEAN      NOT NULL DEFAULT TRUE,
    FOREIGN KEY (department_id) REFERENCES department (id)
);

-- Create roles table
CREATE TABLE IF NOT EXISTS roles
(
    id   UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Create users table
CREATE TABLE IF NOT EXISTS users
(
    id        UUID PRIMARY KEY,
    username  VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    is_active BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Create user_roles join table
CREATE TABLE IF NOT EXISTS user_roles
(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);
