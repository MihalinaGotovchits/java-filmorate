package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film newFilm);

    Collection<Film> getAllFilms();

    Long getNextId();

    Film getFilmById(Long filmId);

    void deleteFilmById(Long filmId);

    Collection<Film> getPopularFilms(Integer sizeOfList);

}
