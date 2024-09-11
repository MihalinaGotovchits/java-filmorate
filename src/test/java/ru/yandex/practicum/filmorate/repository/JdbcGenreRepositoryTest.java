package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.JdbcGenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import({JdbcGenreRepository.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcGenreRepositoryTest {
    private static final int TEST_GENRE_ID = 1;
    private static final int COUNT_OF_ELEMENTS = 6;

    private final JdbcGenreRepository jdbcGenreRepository;

    static Genre getTestGenre() {
        return Genre.builder()
                .id(TEST_GENRE_ID)
                .name("Комедия")
                .build();
    }

    @Test
    void get() {
        Genre genre = jdbcGenreRepository.getGenre(TEST_GENRE_ID);

        assertThat(genre)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestGenre());
    }

    @Test
    void getAll() {
        assertThat(jdbcGenreRepository.getAllGenre())
                .hasSize(COUNT_OF_ELEMENTS);
    }

    @Test
    void checkGenreExists() {
        assertThat(jdbcGenreRepository.checkGenreExists(TEST_GENRE_ID)).isTrue();
        assertThat(jdbcGenreRepository.checkGenreExists(COUNT_OF_ELEMENTS + 1)).isFalse();
    }

    @Test
    void checkGenresExist() {
        assertThat(jdbcGenreRepository.checkGenresExist(List.of())).isTrue();
        assertThat(jdbcGenreRepository.checkGenresExist(List.of(TEST_GENRE_ID, COUNT_OF_ELEMENTS))).isTrue();
        assertThat(jdbcGenreRepository.checkGenresExist(List.of(TEST_GENRE_ID, COUNT_OF_ELEMENTS + 1))).isFalse();
    }
}