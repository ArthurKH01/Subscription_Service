CREATE SEQUENCE IF NOT EXISTS subscriptions_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS invoices_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS retryable_task_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE invoices
(
    id                           BIGINT NOT NULL DEFAULT nextval('invoices_seq'),
    user_id                      BIGINT NOT NULL,
    invoice_date                 DATE,
    price                        DECIMAL(19, 4),
    type                         VARCHAR(255),
    subscription_activation_date DATE,
    subscription_id              BIGINT,
    CONSTRAINT pk_invoices PRIMARY KEY (id),
    CONSTRAINT fk_invoices_on_subscription FOREIGN KEY (subscription_id) REFERENCES subscriptions (id)
);

CREATE INDEX IF NOT EXISTS idx_invoices_sub_date ON invoices (subscription_id, invoice_date);
CREATE INDEX IF NOT EXISTS idx_invoices_user_date_desc ON invoices (user_id, invoice_date DESC);

CREATE TABLE retryable_task
(
    id         BIGINT NOT NULL DEFAULT nextval('retryable_task_seq'),
    payload    JSONB,
    type       VARCHAR(255),
    status     VARCHAR(255),
    retry_time TIMESTAMP WITH TIME ZONE,
    CONSTRAINT pk_retryable_task PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_retry_task_type_status_time ON retryable_task (type, status, retry_time);

CREATE TABLE subscriptions
(
    id                BIGINT       NOT NULL DEFAULT nextval('subscriptions_seq'),
    user_id           BIGINT       NOT NULL,
    type              VARCHAR(255) NOT NULL,
    status            VARCHAR(255) NOT NULL,
    activation_date   DATE,
    next_invoice_date DATE,
    CONSTRAINT pk_subscriptions PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_subs_user_status_type ON subscriptions (user_id, status, type);
CREATE INDEX IF NOT EXISTS idx_subs_status_next_date ON subscriptions (status, next_invoice_date);