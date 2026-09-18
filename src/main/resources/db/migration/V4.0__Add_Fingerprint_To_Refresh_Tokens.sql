ALTER TABLE refresh_tokens
    ADD COLUMN user_agent VARCHAR(500),
    ADD COLUMN ip_address VARCHAR(45);
