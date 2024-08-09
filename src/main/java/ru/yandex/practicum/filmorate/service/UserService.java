package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        log.info(String.format("Добавлен пользователь %s", user));
        return userStorage.create(user);
    }

    public User updateUser(User newUser) {
        getUserById(newUser.getId());
        log.info(String.format("Обновлен пользователь %s", newUser));
        return userStorage.update(newUser);
    }

    public User getUserById(Long id) {
        final User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с Id %s не найден.", id));
        }
        return userStorage.getUserById(id);
    }

    public void deleteUserById(Long id) {
        final User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с Id %s не найден.", id));
        }
        userStorage.deleteUserById(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(Long userId, Long friendId) {
        final User user = getUserById(userId);
        final User friend = getUserById(friendId);
        if (user != null && friend != null) {
            userStorage.addFriend(userId, friendId);
            log.info("Пользователь {} добавил в друзья {}", user.getName(), friend.getName());
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        final User user = getUserById(userId);
        final User friend = getUserById(friendId);
        if (user != null && friend != null) {
            userStorage.deleteFriend(userId, friendId);
            log.info("Пользователь {} удалил из друзей {}", user.getName(), friend.getName());
        }
    }

    public Collection<User> getUserFriends(Long userId) {
        final User user = getUserById(userId);
        if (user != null) {
            return userStorage.getUserFriends(userId);
        }
        return List.of();
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        final User user = getUserById(userId);
        final User otherUser = getUserById(otherId);
        if (user != null && otherUser != null) {
            return userStorage.getCommonFriends(userId, otherId);
        }
        return List.of();
    }

    public void addLike(Long filmId, Long userId) {
        userStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        userStorage.deleteLike(filmId, userId);
    }
}
