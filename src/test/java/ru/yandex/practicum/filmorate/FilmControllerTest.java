package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.LineLengthException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    @Test
    public void testCreateFilm() {
        Film film = Film.builder()
                .name("Film")
                .description("AllAboutFilm")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();
        Film createdFilm = inMemoryFilmStorage.create(film);
        assertEquals(film, createdFilm);
    }

    @Test
    public void testCreateFilmInvalidName() {
        Film film = Film.builder()
                .name(" ")
                .description("AllAboutFilm")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();
        try {
            inMemoryFilmStorage.create(film);
            fail("Expected ConditionsNotMetException");
        } catch (ConditionsNotMetException e) {
            assertEquals("Название фильма не может быть пустым", e.getMessage());
        }
    }

    @Test
    public void testCreateFilmInvalidDescription() {
        Film film = Film.builder()
                .name("Film")
                .description("AllAboutFilmvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv" +
                        "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv" +
                        "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv" +
                        "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();
        try {
            inMemoryFilmStorage.create(film);
            fail("Expected LineLengthException");
        } catch (LineLengthException e) {
            assertEquals("Описание фильма не должно превышать 200 символов", e.getMessage());
        }
    }

    @Test
    public void testUpdateFilmValidFilm() {
        Film film = Film.builder()
                .id(1L)
                .name("Film")
                .description("AllAboutFilm")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();
        Film film2 = Film.builder()
                .id(1L)
                .name("Film2")
                .description("AllAboutFilm")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();
        inMemoryFilmStorage.create(film);
        Film updatedFilm = inMemoryFilmStorage.update(film);
        assertEquals(film, updatedFilm);
        assertEquals(film.getName(), updatedFilm.getName());
        assertEquals(film.getDescription(), updatedFilm.getDescription());
        assertEquals(film.getReleaseDate(), updatedFilm.getReleaseDate());
        assertEquals(film.getDuration(), updatedFilm.getDuration());
    }

    @Test
    public void testUpdateFilmInvalidId() {
        Film film = Film.builder()
                .name("Film")
                .description("AllAboutFilm")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();
        try {
            inMemoryFilmStorage.update(film);
            fail("Expected ConditionsNotMetException");
        } catch (ConditionsNotMetException e) {
            assertEquals("Id должен быть указан", e.getMessage());
        }
    }

    @Test
    public void testUpdateFilmNotFound() {
        Film film = Film.builder()
                .id(6L)
                .name("Film")
                .description("AllAboutFilm")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();
        try {
            inMemoryFilmStorage.update(film);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            assertEquals("Пост с id = " + film.getId() + " не найден", e.getMessage());
        }
    }

    @Test
    public void testFindAllFilms() {
        Film film1 = Film.builder()
                .name("Film")
                .description("AllAboutFilm")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();
        Film film2 = Film.builder()
                .name("Film2")
                .description("AllAboutFilm2")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();
        inMemoryFilmStorage.create(film1);
        inMemoryFilmStorage.create(film2);
        Collection<Film> films = inMemoryFilmStorage.getAllFilms();
        assertEquals(2, films.size());
        assertTrue(films.contains(film1));
        assertTrue(films.contains(film2));
    }
}
