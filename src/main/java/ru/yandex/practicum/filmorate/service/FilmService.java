package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film create(Film film) {
        log.info(String.format("Добавлен фильм %s", film));
        return filmStorage.create(film);
    }

    public Film updateFilm(Film newFilm) {
        getFilmById(newFilm.getId());
        log.info(String.format("Обновлен фильм %s", newFilm));
        return filmStorage.update(newFilm);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long filmId) {
        final Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new NotFoundException(String.format("Фильм с Id %s не найден", filmId));
        }
        return film;
    }

    public void addLike(Long filmId, Long userId) {
        final User user = userService.getUserById(userId);
        final Film film = getFilmById(filmId);
        if (user != null && film != null) {
            userService.addLike(filmId, userId);
            film.setGrade(film.getGrade() + 1);
            updateFilm(film);
            log.info("Пользователь {} поставил лайк фильму {} ", user, film);
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        final User user = userService.getUserById(userId);
        final Film film = getFilmById(filmId);
        if (user != null && film != null) {
            userService.deleteLike(filmId, userId);
            film.setGrade(film.getGrade() - 1);
            updateFilm(film);
            log.info("Пользователь {} удалил лайк у фильма {} ", user, film);
        }
    }

    public Collection<Film> getPopularFilm(Integer sizeOfList) {
        return filmStorage.getPopularFilms(sizeOfList);
    }
}
