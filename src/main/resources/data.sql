INSERT INTO app_author (id, first_name, last_name, date_of_birth)
VALUES (1, 'Jack', 'London', '1876-01-12'),
       (2, 'Victor', 'Hugo', '1802-02-26'),
       (3, 'Ernest', 'Hemingway', '1899-07-21'),
       (4, 'Jane', 'Austen', '1775-12-16');

INSERT INTO app_book (id, title, isbn, publication_date, price, author_id)
VALUES (1, 'Martin Eden', '9781948132589', '2013-10-24', 12.99, 1),
       (2, 'Les Miserables', '9780451419439', '2015-07-24', 9.99, 2),
       (3, 'The Old Man and the Sea', '9780684801223', '1952-09-01', 12.99, 3),
       (4, 'Pride and Prejudice', '9780141439518', '1813-01-28', 9.99, 4);

INSERT INTO app_user (id, username, password, email, role)
VALUES (1, 'admin', 'admin', 'admin@example.com', 'ROLE_ADMIN'),
       (2, 'user', 'user', 'user@example.com', 'ROLE_USER');
