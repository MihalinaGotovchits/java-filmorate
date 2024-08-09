package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private User user;
    private final InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @Test
    public void testInvalidEmail() {
        user = User.builder()
                .id(2L)
                .email("yandex.bezsobac")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(2001, 12, 13))
                .build();
        try {
            inMemoryUserStorage.create(user);
            fail("Expected ConditionsNotMetException");
        } catch (ConditionsNotMetException e) {
            assertEquals("E-mail не может быть пустым и должен содержать символ '@'", e.getMessage());
        }
    }

    @Test
    public void testInvalidEmailIsEmpty() {
        user = User.builder()
                .id(1L)
                .email(" ")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(2001, 12, 13))
                .build();
        try {
            inMemoryUserStorage.create(user);
            fail("Expected ConditionsNotMetException");
        } catch (ConditionsNotMetException e) {
            assertEquals("E-mail не может быть пустым и должен содержать символ '@'", e.getMessage());
        }
    }

    @Test
    public void testCreateUser() {
        User user = User.builder()
                .id(1L)
                .email("Mi@yandex")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(2001, 12, 13))
                .build();
        User createdUser = inMemoryUserStorage.create(user);
        assertEquals(user, createdUser);
    }

    @Test
    public void testInvalidLogin() {
        User user = User.builder()
                .id(1L)
                .email("Mi@yandex")
                .login(" Mi  sg ")
                .name("Name")
                .birthday(LocalDate.of(2001, 12, 13))
                .build();
        try {
            inMemoryUserStorage.create(user);
            fail("Expected ConditionsNotMetException");
        } catch (ConditionsNotMetException e) {
            assertEquals("Логин не может содержать пробелы и быть пустым", e.getMessage());
        }
    }

    @Test
    public void testLoginIsEmpty() {
        User user = User.builder()
                .id(1L)
                .email("Mi@yandex")
                .login(" ")
                .name("Name")
                .birthday(LocalDate.of(2001, 12, 13))
                .build();
        try {
            inMemoryUserStorage.create(user);
            fail("Expected ConditionsNotMetException");
        } catch (ConditionsNotMetException e) {
            assertEquals("Логин не может содержать пробелы и быть пустым", e.getMessage());
        }
    }

    @Test
    public void testUpdateUserValidUser() {
        User user = User.builder()
                .id(1L)
                .email("Mi@yandex")
                .login("Miha")
                .name("Name")
                .birthday(LocalDate.of(2001, 12, 13))
                .build();
        inMemoryUserStorage.create(user);
        User updatedUser = inMemoryUserStorage.update(user);
        assertEquals(user, updatedUser);
        assertEquals(user.getName(), updatedUser.getName());
        assertEquals(user.getEmail(), updatedUser.getEmail());
        assertEquals(user.getBirthday(), updatedUser.getBirthday());
    }

    @Test
    public void testUpdateUserInvalidId() {
        User user = User.builder()
                .id(1L)
                .email("Mi@yandex")
                .login("Miha")
                .name("Name")
                .birthday(LocalDate.of(2001, 12, 13))
                .build();
        user.setId(null);
        try {
            inMemoryUserStorage.update(user);
            fail("Expected ConditionsNotMetException");
        } catch (ConditionsNotMetException e) {
            assertEquals("Id должен быть указан", e.getMessage());
        }
    }

    @Test
    public void testFindAllUsers() {
        user = User.builder()
                .id(1L)
                .email("Mi@yandex")
                .login("Miha")
                .name("Name")
                .birthday(LocalDate.of(2001, 12, 13))
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("Li@yandex")
                .login("Lina")
                .name("Name")
                .birthday(LocalDate.of(2001, 12, 13))
                .build();
        inMemoryUserStorage.create(user);
        inMemoryUserStorage.create(user2);
        Collection<User> users = inMemoryUserStorage.getAllUsers();
        assertEquals(2, users.size());
        assertTrue(users.contains(user));
        assertTrue(users.contains(user2));
    }

}
