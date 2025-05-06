package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mpa.JdbcMpaRepository;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import({JdbcMpaRepository.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcMpaRepositoryTest {
    private static final int TEST_MPA_ID = 1;
    private static final int COUNT_OF_ELEMENTS = 5;

    private final JdbcMpaRepository jdbcMpaRepository;

    static Mpa getTestMpa() {
        return Mpa.builder()
                .id(TEST_MPA_ID)
                .name("G")
                .build();
    }

    @Test
    void checkMpaExists() {
        assertThat(jdbcMpaRepository.checkMpaExists(TEST_MPA_ID)).isTrue();
        assertThat(jdbcMpaRepository.checkMpaExists(COUNT_OF_ELEMENTS + 1)).isFalse();
    }

    @Test
    void get() {
        Mpa mpa = jdbcMpaRepository.getMpa(TEST_MPA_ID);

        assertThat(mpa)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestMpa());
    }

    @Test
    void getAll() {
        assertThat(jdbcMpaRepository.getAllMpa())
                .hasSize(COUNT_OF_ELEMENTS);
    }
}