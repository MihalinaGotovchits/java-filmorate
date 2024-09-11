package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.JdbcUserRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcUserRepository.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcUserRepository integration tests")
class JdbcUserRepositoryTest {
    private static final long TEST_USER_ID = 1L;
    private static final long COUNT_OF_ELEMENTS = 2L;

    private final JdbcUserRepository jdbcUserRepository;

    static User getTestUser() {
        return User.builder()
                .id(TEST_USER_ID)
                .email("email@email.ru")
                .login("user")
                .name("name")
                .birthday(LocalDate.parse("2000-03-22"))
                .build();
    }

    static User getTestNewUser() {
        return User.builder()
                .email("emailNew@email.ru")
                .login("user2")
                .name("name2")
                .birthday(LocalDate.parse("1995-10-09"))
                .build();
    }

    @Test
    void create() {
        User createdUser = jdbcUserRepository.create(getTestNewUser());

        assertThat(jdbcUserRepository.getUserById(createdUser.getId()))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(getTestNewUser());
    }

    @Test
    void update() {
        User updateUser = getTestNewUser().toBuilder()
                .id(TEST_USER_ID)
                .build();

        assertThat(jdbcUserRepository.getUserById(updateUser.getId()))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isNotEqualTo(updateUser);

        jdbcUserRepository.update(updateUser);

        assertThat(jdbcUserRepository.getUserById(updateUser.getId()))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestNewUser());
    }

    @Test
    void get() {
        User user = jdbcUserRepository.getUserById(TEST_USER_ID);

        assertThat(user)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestUser());
    }

    @Test
    void getAll() {
        assertThat(jdbcUserRepository.getAllUsers())
                .hasSize((int) COUNT_OF_ELEMENTS);
    }

    @Test
    void addFriend() {
        jdbcUserRepository.addFriend(TEST_USER_ID, COUNT_OF_ELEMENTS);

        assertThat(jdbcUserRepository.getUserFriends(TEST_USER_ID))
                .singleElement()
                .isEqualToComparingOnlyGivenFields(User.builder().id(COUNT_OF_ELEMENTS), "id");

        assertThat(jdbcUserRepository.getUserFriends(COUNT_OF_ELEMENTS)).hasSize(0);
    }

    @Test
    @DisplayName("getFriends() returns actual friend list.")
    void getFriends() {
        jdbcUserRepository.addFriend(TEST_USER_ID, COUNT_OF_ELEMENTS);

        assertThat(jdbcUserRepository.getUserFriends(TEST_USER_ID))
                .singleElement()
                .isEqualToComparingOnlyGivenFields(User.builder().id(COUNT_OF_ELEMENTS), "id");
    }

    @Test
    void getMutualFriends() {
        User friend = jdbcUserRepository.create(getTestNewUser());

        jdbcUserRepository.addFriend(TEST_USER_ID, friend.getId());
        jdbcUserRepository.addFriend(COUNT_OF_ELEMENTS, friend.getId());

        assertThat(jdbcUserRepository.getCommonFriends(TEST_USER_ID, COUNT_OF_ELEMENTS))
                .singleElement()
                .isEqualTo(friend);
    }

    @Test
    void removeFriend() {
        jdbcUserRepository.addFriend(TEST_USER_ID, COUNT_OF_ELEMENTS);
        assertThat(jdbcUserRepository.getUserFriends(TEST_USER_ID)).hasSize(1);
        jdbcUserRepository.deleteFriend(TEST_USER_ID, COUNT_OF_ELEMENTS);
        assertThat(jdbcUserRepository.getUserFriends(TEST_USER_ID)).hasSize(0);
    }

    @Test
    void delete() {
        assertThat(jdbcUserRepository.checkUserExists(TEST_USER_ID)).isTrue();
        jdbcUserRepository.deleteUserById(TEST_USER_ID);
        assertThat(jdbcUserRepository.checkUserExists(TEST_USER_ID)).isFalse();
    }

    @Test
    void checkUserExists() {
        assertThat(jdbcUserRepository.checkUserExists(TEST_USER_ID)).isTrue();
        assertThat(jdbcUserRepository.checkUserExists(COUNT_OF_ELEMENTS + 1)).isFalse();
    }

    @Test
    void checkUserExistsByEmail() {
        assertThat(jdbcUserRepository.checkUserExistsByEmail(getTestUser().toBuilder().id(999L).build())).isTrue();
        assertThat(jdbcUserRepository.checkUserExistsByEmail(getTestUser().toBuilder().id(null).build())).isTrue();

        assertThat(jdbcUserRepository.checkUserExistsByEmail(getTestUser())).isFalse();
        assertThat(jdbcUserRepository.checkUserExistsByEmail(getTestNewUser())).isFalse();
    }
}