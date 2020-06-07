CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

--
-- Legacy mocks imported from mongodb database
--
CREATE TABLE mocks_v2
(
    id character varying NOT NULL,

    content bytea,

    status integer NOT NULL,
    charset character varying NOT NULL,
    content_type character varying NOT NULL,
    headers jsonb,

    last_access_at timestamp with time zone,
    total_access bigint NOT NULL,

    CONSTRAINT mocks_v2_pkey PRIMARY KEY (id)
);

--
-- New storage
--
CREATE TABLE mocks_v3
(
    id UUID NOT NULL DEFAULT uuid_generate_v4 (),

    name character varying,
    content bytea,
    hash_content character varying,

    status integer NOT NULL,
    charset character varying NOT NULL,
    content_type character varying NOT NULL,
    headers jsonb,

    secret_token character varying NOT NULL,
    hash_ip character varying NOT NULL,

    created_at timestamp with time zone,
    last_access_at timestamp with time zone,
    expire_at timestamp with time zone,

    total_access bigint NOT NULL,

    CONSTRAINT mocks_v3_pkey PRIMARY KEY (id)
);