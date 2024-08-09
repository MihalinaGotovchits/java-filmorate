package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User create(User user);

    User update(User newUser);

    Collection<User> getAllUsers();

    User getUserById(Long id);

    void deleteUserById(Long id);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    Collection<User> getUserFriends(Long userId);

    Collection<User> getCommonFriends(Long userId, Long otherId);

    void addLike(Long userId, Long filmId);

    void deleteLike(Long userId, Long filmId);

    Long getNextId();
}
