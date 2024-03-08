CREATE TABLE IF NOT EXISTS audit_trail (
    id             UUID         NOT NULL DEFAULT gen_random_uuid(),
    insertedAt     TIMESTAMP    NOT NULL DEFAULT now(),
    createdAt      TIMESTAMP    NOT NULL,
    event          VARCHAR(256),
    eventData      TEXT
    CONSTRAINT PKEY_AUDIT_TRAIL PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_eventdata ON audit_trail(eventData);
CREATE INDEX IF NOT EXISTS idx_event ON audit_trail(event);
