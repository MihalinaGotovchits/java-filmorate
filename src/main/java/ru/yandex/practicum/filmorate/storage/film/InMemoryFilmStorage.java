package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.LineLengthException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error(film.toString());
            throw new ConditionsNotMetException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error(film.toString());
            throw new LineLengthException("Описание фильма не должно превышать 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error(film.toString());
            throw new ConditionsNotMetException("Дата выхода фильма не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.error(film.toString());
            throw new ConditionsNotMetException("Продолжительность фильма не может быть меньше 0");
        }
        film.setId(getNextId());
        log.trace("Фильму {} присвоен ID № {}", film.getName(), film.getId());
        films.put(film.getId(), film);
        log.info("Фильм {} сохранен", film.getName());
        return film;
    }


    @Override
    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            log.error(newFilm.toString());
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (newFilm.getName() == null || newFilm.getName().isBlank()) {
                log.error(newFilm.toString());
                throw new ConditionsNotMetException("Название фильма не может быть пустым");
            }
            if (newFilm.getDescription().length() > 200) {
                log.error(newFilm.toString());
                throw new LineLengthException("Описание фильма не должно превышать 200 символов");
            }
            if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.error(newFilm.toString());
                throw new ConditionsNotMetException("Дата выхода фильма не может быть раньше 28 декабря 1895 года");
            }
            if (newFilm.getDuration() < 0) {
                log.error(newFilm.toString());
                throw new ConditionsNotMetException("Продолжительность фильма не может быть меньше 0");
            }
            // если фильм найдена и все условия соблюдены, обновляем её содержимое
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Данные фильма {} обновлены", oldFilm.getName());
            return oldFilm;
        }
        log.error(newFilm.toString());
        throw new NotFoundException("Пост с id = " + newFilm.getId() + " не найден");
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film getFilmById(Long filmId) {
        return films.get(filmId);
    }

    @Override
    public void deleteFilmById(Long filmId) {
        films.remove(filmId);
    }


    @Override
    public Collection<Film> getPopularFilms(Integer sizeOfList) {
        return films.values().stream()
                .sorted(Comparator.comparing(Film::getGrade).reversed())
                .limit(sizeOfList)
                .collect(Collectors.toList());
    }

    @Override
    public Long getNextId() {
        Long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
