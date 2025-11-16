-- Inserir admin inicial senha 12345
INSERT INTO users (user_id, username, password, role) VALUES
(1, 'admin', '$2a$14$tRJyacmi6TlHeHezK8raJefKl7BmMp9Inm0RmeVyL6ebxHfpJ9GS2', 'ADMIN')
ON CONFLICT (user_id) DO NOTHING;

-- Atualizar a sequence para começar do ID 2
SELECT setval('users_user_id_seq', (SELECT MAX(user_id) FROM users));