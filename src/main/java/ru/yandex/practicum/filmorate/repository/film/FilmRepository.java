package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmRepository {
    Film create(Film film);

    Film update(Film newFilm);

    Collection<Film> getAllFilms();

    Film getFilmById(Long filmId);

    void deleteFilmById(Long filmId);

    void addLike(Long filmId, Long userId);

    Collection<Film> getPopularFilms(Integer count);

    void removeLike(Long filmId, Long userId);

    boolean checkFilmExists(Long filmId);
}
