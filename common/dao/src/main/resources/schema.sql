CREATE TABLE IF NOT EXISTS transports (
    id SERIAL NOT NULL PRIMARY KEY,
    stop_id VARCHAR(50) NOT NULL,
    transport_name VARCHAR(10) NOT NULL,
    common_name VARCHAR(100) NOT NULL,

    UNIQUE (stop_id, transport_name)
);

CREATE TABLE IF NOT EXISTS schedule (
    id SERIAL NOT NULL PRIMARY KEY,
    ts TIMESTAMP NOT NULL DEFAULT now(),
    complete BOOLEAN NOT NULL DEFAULT false,
    user_id BIGINT NOT NULL,
    transport_id INTEGER,
    delay INTEGER NOT NULL,

    FOREIGN KEY (transport_id) REFERENCES transports (id) ON DELETE CASCADE
);