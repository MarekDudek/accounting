CREATE SEQUENCE IF NOT EXISTS public.operations_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.operations_id_seq OWNER TO accountant;

CREATE TABLE IF NOT EXISTS public.operations
(
    id              bigint                                         NOT NULL DEFAULT nextval('operations_id_seq'::regclass),
    exec_date       date                                           NOT NULL,
    order_date      date                                           NOT NULL,
    type            character varying COLLATE pg_catalog."default" NOT NULL,
    description     character varying COLLATE pg_catalog."default" NOT NULL,
    amount          numeric(19, 2)                                 NOT NULL,
    amount_currency character(3) COLLATE pg_catalog."default"      NOT NULL,
    ending_balance  numeric(19, 2)                                 NOT NULL
) WITH (OIDS = FALSE)
  TABLESPACE pg_default;

ALTER SEQUENCE public.operations_id_seq OWNED BY operations.id;

ALTER TABLE IF EXISTS public.operations
    OWNER to accountant;