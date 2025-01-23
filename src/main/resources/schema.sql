CREATE TABLE IF NOT EXISTS TASK_HISTORY (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ad_id VARCHAR(255),
    message TEXT,
    probability VARCHAR(50),
    reward INT,
    success BOOLEAN,
    reputation_people DOUBLE,
    reputation_state DOUBLE,
    reputation_underworld DOUBLE,
    dragon_level INT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
