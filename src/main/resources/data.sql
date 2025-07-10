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
