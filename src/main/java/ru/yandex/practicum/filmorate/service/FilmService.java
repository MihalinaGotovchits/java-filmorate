package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    public final Film create(Film film) {
        checkFilmConstraints(film);
        if (film.getMpa() != null && !mpaRepository.checkMpaExists(film.getMpa().getId())) {
            throw new ConditionsNotMetException("Рейтин фильма не найден " + film.getMpa().getId());
        }
        if (!genreRepository.checkGenresExist(
                film.getGenres().stream()
                        .map(Genre::getId)
                        .collect(Collectors.toList()))
        ) {
            throw new ConditionsNotMetException("Жанры не найдены " + film.getGenres());
        }
        Film createdFilm = filmRepository.create(film);
        log.info("Фильм {} создан", createdFilm);
        return createdFilm;
    }

    public Film updateFilm(Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ConditionsNotMetException("Id фильма не может быть пустым " + newFilm);
        }
        if (filmRepository.getFilmById(newFilm.getId()) != null) {
            checkFilmConstraints(newFilm);
            if (newFilm.getMpa() != null && !mpaRepository.checkMpaExists(newFilm.getMpa().getId())) {
                throw new ConditionsNotMetException("Рейтин фильма не найден " + newFilm.getMpa().getId());
            }
            if (!genreRepository.checkGenresExist(
                    newFilm.getGenres().stream()
                            .map(Genre::getId)
                            .collect(Collectors.toList()))
            ) {
                throw new ConditionsNotMetException("Жанры не найдены " + newFilm.getGenres());
            }
            Film updateFilm = filmRepository.update(newFilm);
            log.info("Фильм {} обновлен", newFilm);
            return updateFilm;
        }
        throw new NotFoundException("Фильм не найден " + newFilm);
    }

    public Collection<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }

    public Film getFilmById(Long filmId) {
        if (!filmRepository.checkFilmExists(filmId)) {
            throw new NotFoundException("Фильм c id: " + filmId + " не найден");
        }
        return filmRepository.getFilmById(filmId);
    }

    public void addLike(Long filmId, Long userId) {
        if (!filmRepository.checkFilmExists(filmId)) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (!userRepository.checkUserExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + "не найден");
        }
        filmRepository.addLike(filmId, userId);
        log.info("Пользователь с id {} поставил лайк фильму с id{} ", userId, filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        if (!filmRepository.checkFilmExists(filmId)) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (!userRepository.checkUserExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        filmRepository.removeLike(filmId, userId);
        log.info("Пользователь с id {} удалил лайк у фильма с id{} ", userId, filmId);
    }


    public Collection<Film> getPopularFilm(Integer sizeOfList) {
        return filmRepository.getPopularFilms(sizeOfList);
    }

    private void checkFilmConstraints(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ConditionsNotMetException("Название фильма не может быть пустым : " + film);
        }

        if (film.getDescription() == null || film.getDescription().length() > 200) {
            throw new ConditionsNotMetException("Описание фильма не может быть длиннее 200 знаков: " + film);
        }

        if (film.getReleaseDate() == null || !film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
            throw new ConditionsNotMetException("Дата выхода фильма не может быть раньше 1895-12-28: " + film);
        }

        if (film.getDuration() == null || film.getDuration() <= 0) {
            throw new ConditionsNotMetException("Продолжительность фильма не может быть меньше или равной нулю: " + film);
        }
    }
}
