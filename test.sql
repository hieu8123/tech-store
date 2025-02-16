UPDATE users
SET role = 'ADMIN'
WHERE id = UUID_TO_BIN('6d7a68b2-7b9e-4e82-a1d5-3761254421d1');


SELECT * FROM users WHERE id = UUID_TO_BIN('6d7a68b2-7b9e-4e82-a1d5-3761254421d1');

select * from brands
select * from categories