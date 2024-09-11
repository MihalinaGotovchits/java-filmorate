package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.film.JdbcFilmRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcFilmRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcFilmRepository integration tests")
class JdbcFilmRepositoryTest {
    private static final long TEST_FILM_ID = 1L;
    private static final int COUNT_OF_ELEMENTS = 3;
    private static final long TEST_USER_ID = 1L;

    private final JdbcFilmRepository jdbcFilmRepository;

    static Film getTestFilm() {
        Film film = Film.builder()
                .id(TEST_FILM_ID)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.parse("1960-03-21"))
                .duration(100)
                .mpa(new Mpa(1, "G"))
                .build();

        film.getGenres().add(new Genre(1, "Комедия"));
        film.getGenres().add(new Genre(2, "Драма"));

        return film;
    }

    static Film getTestNewFilm() {
        Film newFilm = Film.builder()
                .name("name2")
                .description("description2")
                .releaseDate(LocalDate.parse("2000-03-22"))
                .duration(997)
                .mpa(new Mpa(2, "PG"))
                .build();

        newFilm.getGenres().add(new Genre(3, "Мультфильм"));
        newFilm.getGenres().add(new Genre(4, "Триллер"));

        return newFilm;
    }

    @Test
    void create() {
        Film createdFilm = jdbcFilmRepository.create(getTestNewFilm());

        assertThat(jdbcFilmRepository.getFilmById(createdFilm.getId()))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(getTestNewFilm());
    }

    @Test
    void update() {
        Film updateFilm = getTestNewFilm();
        updateFilm.setId(TEST_FILM_ID);

        assertThat(jdbcFilmRepository.getFilmById(updateFilm.getId()))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isNotEqualTo(updateFilm);

        jdbcFilmRepository.update(updateFilm);

        assertThat(jdbcFilmRepository.getFilmById(updateFilm.getId()))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestNewFilm());
    }

    @Test
    void getAll() {
        assertThat(jdbcFilmRepository.getAllFilms())
                .hasSize((int) COUNT_OF_ELEMENTS);
    }

    @Test
    void get() {
        Film film = jdbcFilmRepository.getFilmById(TEST_FILM_ID);

        assertThat(film)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestFilm());
    }

    @Test
    void addLike() {
        jdbcFilmRepository.addLike(TEST_FILM_ID, TEST_USER_ID);

        assertThat(jdbcFilmRepository.getPopularFilms(1))
                .singleElement()
                .isEqualToComparingOnlyGivenFields(Film.builder().id(TEST_FILM_ID), "id");

    }

    @Test
    void getMostPopular() {
        jdbcFilmRepository.addLike((long) COUNT_OF_ELEMENTS, TEST_USER_ID);
        jdbcFilmRepository.addLike((long) COUNT_OF_ELEMENTS, TEST_USER_ID + 1);

        jdbcFilmRepository.addLike(TEST_FILM_ID, TEST_USER_ID);

        assertThat(jdbcFilmRepository.getPopularFilms(2))
                .containsExactly((Film) jdbcFilmRepository.getPopularFilms(COUNT_OF_ELEMENTS),
                        jdbcFilmRepository.getFilmById(TEST_FILM_ID));
    }

    @Test
    void removeLike() {
        jdbcFilmRepository.addLike(TEST_FILM_ID, TEST_USER_ID);
        jdbcFilmRepository.addLike(TEST_FILM_ID, TEST_USER_ID + 1);

        jdbcFilmRepository.addLike((long) COUNT_OF_ELEMENTS, TEST_USER_ID);

        assertThat(jdbcFilmRepository.getPopularFilms(1))
                .singleElement()
                .isEqualToComparingOnlyGivenFields(Film.builder().id(TEST_FILM_ID), "id");

        jdbcFilmRepository.removeLike(TEST_FILM_ID, TEST_USER_ID);
        jdbcFilmRepository.removeLike(TEST_FILM_ID, TEST_USER_ID + 1);

        assertThat(jdbcFilmRepository.getPopularFilms(1))
                .singleElement()
                .isEqualToComparingOnlyGivenFields(Film.builder().id((long) COUNT_OF_ELEMENTS), "id");
    }

    @Test
    void delete() {
        assertThat(jdbcFilmRepository.checkFilmExists(TEST_FILM_ID)).isTrue();
        jdbcFilmRepository.deleteFilmById(TEST_FILM_ID);
        assertThat(jdbcFilmRepository.checkFilmExists(TEST_FILM_ID)).isFalse();
    }

    @Test
    void checkFilmExists() {
        assertThat(jdbcFilmRepository.checkFilmExists(TEST_FILM_ID)).isTrue();
        assertThat(jdbcFilmRepository.checkFilmExists((long) (COUNT_OF_ELEMENTS + 1))).isFalse();
    }
}