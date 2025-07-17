-- Phonebook Database Prepopulation Script

-- Insert sample divisions
INSERT INTO division (id, name, is_active) VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'Information Technology', true),
    ('550e8400-e29b-41d4-a716-446655440002', 'Human Resources', true),
    ('550e8400-e29b-41d4-a716-446655440003', 'Finance', true),
    ('550e8400-e29b-41d4-a716-446655440004', 'Marketing', true);

-- Insert sample departments
INSERT INTO department (id, name, division_id, is_active, created_at, updated_at) VALUES
    ('660e8400-e29b-41d4-a716-446655440001', 'Software Development', '550e8400-e29b-41d4-a716-446655440001', true, NOW(), NOW()),
    ('660e8400-e29b-41d4-a716-446655440002', 'DevOps', '550e8400-e29b-41d4-a716-446655440001', true, NOW(), NOW()),
    ('660e8400-e29b-41d4-a716-446655440003', 'Recruitment', '550e8400-e29b-41d4-a716-446655440002', true, NOW(), NOW()),
    ('660e8400-e29b-41d4-a716-446655440004', 'Accounting', '550e8400-e29b-41d4-a716-446655440003', true, NOW(), NOW()),
    ('660e8400-e29b-41d4-a716-446655440005', 'Digital Marketing', '550e8400-e29b-41d4-a716-446655440004', true, NOW(), NOW());

-- Insert sample employees
INSERT INTO employee (id, name, position, phone, email, department_id, created_at, updated_at, is_active) VALUES
    ('770e8400-e29b-41d4-a716-446655440001', 'John Smith', 'Senior Software Engineer', '+1-555-0101', 'john.smith@company.com', '660e8400-e29b-41d4-a716-446655440001', NOW(), NOW(), true),
    ('770e8400-e29b-41d4-a716-446655440002', 'Sarah Johnson', 'DevOps Engineer', '+1-555-0102', 'sarah.johnson@company.com', '660e8400-e29b-41d4-a716-446655440002', NOW(), NOW(), true),
    ('770e8400-e29b-41d4-a716-446655440003', 'Michael Chen', 'HR Manager', '+1-555-0103', 'michael.chen@company.com', '660e8400-e29b-41d4-a716-446655440003', NOW(), NOW(), true),
    ('770e8400-e29b-41d4-a716-446655440004', 'Emily Davis', 'Senior Accountant', '+1-555-0104', 'emily.davis@company.com', '660e8400-e29b-41d4-a716-446655440004', NOW(), NOW(), true),
    ('770e8400-e29b-41d4-a716-446655440005', 'David Wilson', 'Marketing Specialist', '+1-555-0105', 'david.wilson@company.com', '660e8400-e29b-41d4-a716-446655440005', NOW(), NOW(), true);

-- Insert sample roles
INSERT INTO ROLES(id, name)
VALUES ('ca8b19ff-8dd6-498e-9a70-a7d604a366de', 'ADMIN');
INSERT INTO ROLES(id, name)
VALUES ('e30d3967-5df2-4b11-b10c-14d9ab08d9ed', 'MANAGER');
INSERT INTO ROLES(id, name)
VALUES ('d42f058d-1900-4b5b-b418-214368b521a6', 'USER');

-- Insert sample users
INSERT INTO USERS(id, username, password, is_active)
VALUES ('65c48484-5093-4537-ae23-e1f8ac41d823', 'admin', '$2a$10$Uu42IGt5cKkwnf.iCJexBuYz0VUMv36dYaB4qZKCjPn72zFthwEa.',
        true);
INSERT INTO USERS(id, username, password, is_active)
VALUES ('6dbe15cf-37f4-4636-ad21-b3905e85e488', 'manager',
        '$2a$10$mzf7MjbeRTyo5I3lqFEucOYPSCgmqQMjWxPDT/LKnsfnOB5OEGxoi', true);
INSERT INTO USERS(id, username, password, is_active)
VALUES ('256570f3-81f1-4e6c-b354-5c7f49462128', 'user', '$2a$10$9m1bqVsDYyx2DocBA/Y2DOu818pUjbUTTW2mumbEuzNG.7Uq9KDqa',
        true);

-- Insert sample user_roles
INSERT INTO USER_ROLES(user_id, role_id)
VALUES ('65c48484-5093-4537-ae23-e1f8ac41d823', 'ca8b19ff-8dd6-498e-9a70-a7d604a366de');
INSERT INTO USER_ROLES(user_id, role_id)
VALUES ('6dbe15cf-37f4-4636-ad21-b3905e85e488', 'e30d3967-5df2-4b11-b10c-14d9ab08d9ed');
INSERT INTO USER_ROLES(user_id, role_id)
VALUES ('256570f3-81f1-4e6c-b354-5c7f49462128', 'd42f058d-1900-4b5b-b418-214368b521a6');
