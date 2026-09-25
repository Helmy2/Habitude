CREATE TABLE habits (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    description TEXT,
    frequency_type VARCHAR(20) NOT NULL,
    target_count INT NOT NULL,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_habits_archived_created ON habits (archived, created_at DESC);
