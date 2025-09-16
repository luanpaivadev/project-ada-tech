CREATE TABLE public.tbl_products (
	id uuid NOT NULL,
	available_quantity int4 NULL,
	description varchar(255) NULL,
	"name" varchar(255) NULL,
	price numeric(38, 2) NULL,
	CONSTRAINT tbl_products_pkey PRIMARY KEY (id)
);