INSERT INTO mpa_rating(rating_id, name)
VALUES
(1, 'G'),
(2, 'PG'),
(3, 'PG-13'),
(4, 'R'),
(5, 'NC-17');

INSERT INTO genres(genre_id, name)
VALUES
(1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');

INSERT INTO users(email, login, name, birthday)
VALUES
('yandex@email.ru', 'user', 'name', '2000-03-22'),
('emailNew@email.ru', 'user2', 'name2', '1995-10-09');

INSERT INTO films(name, description, release_date, duration, rating_id)
VALUES
('name', 'description', '1960-03-21', 100, 1),
('name2', 'description2', '2000-03-22', 997, 3),
('name3', 'description3', '1995-01-01', 777, 2);

INSERT INTO film_genres(film_id, genre_id)
VALUES
(cast(1 as bigint), 1),
(cast(1 as bigint), 2);

INSERT INTO film_genres(film_id, genre_id)
VALUES
(cast(3 as bigint), 6);