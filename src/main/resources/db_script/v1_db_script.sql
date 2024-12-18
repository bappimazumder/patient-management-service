CREATE SEQUENCE IF NOT EXISTS patient_info_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE IF NOT EXISTS patient_info
(
    id bigint NOT NULL DEFAULT nextval('patient_info_id_seq'::regclass),
    address character varying(255) COLLATE pg_catalog."default",
    age real,
    code character varying(255) COLLATE pg_catalog."default",
    contact_no character varying(255) COLLATE pg_catalog."default",
    created_by bigint,
    create_date timestamp(6) without time zone,
    full_name character varying(255) COLLATE pg_catalog."default",
    gender character varying(255) COLLATE pg_catalog."default",
    patient_type character varying(255) COLLATE pg_catalog."default",
    updated_by bigint,
    update_date timestamp(6) without time zone,
    active_status boolean,
    CONSTRAINT patient_info_pkey PRIMARY KEY (id),
    CONSTRAINT uk_bs0hbqmywv2ho068qq5nb0x7j UNIQUE (code),
    CONSTRAINT patient_info_gender_check CHECK (gender::text = ANY (ARRAY['MALE'::character varying, 'FEMALE'::character varying, 'OTHERS'::character varying]::text[])),
    CONSTRAINT patient_info_patient_type_check CHECK (patient_type::text = ANY (ARRAY['EMERGENCY'::character varying, 'NON_EMERGENCY'::character varying]::text[]))
);