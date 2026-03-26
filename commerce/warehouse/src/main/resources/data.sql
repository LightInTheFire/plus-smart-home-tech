TRUNCATE TABLE addresses;
INSERT INTO addresses (country, city, street, house, flat)
SELECT addr, addr, addr, addr, addr
FROM (SELECT addr
      FROM unnest(ARRAY ['ADDRESS_1','ADDRESS_2']) AS addr
      ORDER BY random()
      LIMIT 1) t;
