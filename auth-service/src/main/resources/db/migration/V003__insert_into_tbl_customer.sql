INSERT INTO public.tbl_customer
(id, email, "name", "password", roles)
VALUES('389d8a40-4d9b-4b90-8ace-029f04222915'::uuid, 'johndoe@email.com', 'John Doe', '$2a$12$x7HCrvgYtkwScKzv9whXW.aCQU2F9eGsxF0fxX.6MhHVPx1YJQil.', 'ROLE_EMPLOYEE');

INSERT INTO public.tbl_customer
(id, email, "name", "password", roles)
VALUES('3f3afd64-8a39-4c1d-8ff8-ab13d7b2309e'::uuid, 'janedoe@email.com', 'Jane Doe', '$2a$12$x7HCrvgYtkwScKzv9whXW.aCQU2F9eGsxF0fxX.6MhHVPx1YJQil.', 'ROLE_USER');