CREATE TABLE public.tbl_service_client (
	id uuid NOT NULL,
	client_id varchar(255) NULL,
	client_secret varchar(255) NULL,
	roles varchar(255) NULL,
	CONSTRAINT tbl_service_client_pkey PRIMARY KEY (id)
);