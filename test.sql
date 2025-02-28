

EXPLAIN SELECT * FROM users ;

SELECT * from products

SELECT * FROM refresh_tokens

SELECT *
FROM payments
         INNER JOIN orders ON payments.order_id = orders.id
WHERE orders.id = UUID_TO_BIN('673a555d-024b-4525-ba16-691737056d27');


SElect * from payments