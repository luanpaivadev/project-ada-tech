CREATE TABLE public.tbl_customer (
	id uuid NOT NULL,
	email varchar(255) NULL,
	"name" varchar(255) NULL,
	"password" varchar(255) NULL,
	roles varchar(255) NULL,
	CONSTRAINT tbl_customer_pkey PRIMARY KEY (id)
);