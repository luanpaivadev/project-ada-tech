INSERT INTO public.tbl_products (id, available_quantity, description, "name", price)
VALUES
  (gen_random_uuid(), 100, 'Smartphone com tela AMOLED 6.5", 128GB de armazenamento', 'Smartphone X100', 1899.90),
  (gen_random_uuid(), 50, 'Notebook ultrafino com processador i7, 16GB RAM e SSD 512GB', 'Notebook Pro 15', 5599.00),
  (gen_random_uuid(), 200, 'Fone de ouvido Bluetooth com cancelamento de ruído ativo', 'Fone Bluetooth ANC', 499.99),
  (gen_random_uuid(), 300, 'Mouse gamer com 7 botões programáveis e RGB', 'Mouse Gamer RGB', 199.90),
  (gen_random_uuid(), 150, 'Cadeira ergonômica para escritório com ajuste lombar', 'Cadeira Ergonômica', 899.00),
  (gen_random_uuid(), 75, 'Monitor LED 27" Full HD com taxa de atualização 144Hz', 'Monitor Gamer 27', 1299.50),
  (gen_random_uuid(), 500, 'Teclado mecânico com switches azuis e iluminação RGB', 'Teclado Mecânico RGB', 350.00),
  (gen_random_uuid(), 1000, 'Carregador rápido USB-C 30W compatível com diversos aparelhos', 'Carregador Turbo 30W', 149.90),
  (gen_random_uuid(), 80, 'Tablet com tela 10.1", 64GB de armazenamento e 4GB RAM', 'Tablet T10', 999.99),
  (gen_random_uuid(), 20, 'Smart TV 55" 4K UHD com sistema operacional Android TV', 'Smart TV 55 4K', 2999.00);
