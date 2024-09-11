package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserRepository {

    User create(User user);

    User getUserById(Long userId);

    Collection<User> getAllUsers();

    User update(User user);

    void deleteUserById(Long userId);

    void addFriend(Long userId, Long friendId);

    Collection<User> getUserFriends(Long userId);

    Collection<User> getCommonFriends(Long firstUserId, Long secondUserId);

    void deleteFriend(Long userId, Long friendId);

    boolean checkUserExists(Long userId);

    boolean checkUserExistsByEmail(User user);
}
