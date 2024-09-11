package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.time.LocalDate;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        checkUser(user);
        if (userRepository.checkUserExistsByEmail(user)) {
            throw new ConditionsNotMetException("Поле e-mael должно бфть заполнено");
        }

        User createdUser = userRepository.create(user);
        log.info(String.format("Добавлен пользователь %s", user));
        return createdUser;
    }

    public User updateUser(User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id пользователя не может быть пустым " + newUser);
        }
        if (!userRepository.checkUserExists(newUser.getId())) {
            throw new NotFoundException("Пользователь не найден " + newUser);
        }
        checkUser(newUser);
        if (userRepository.checkUserExistsByEmail(newUser)) {
            throw new ConditionsNotMetException("Пользователь с таким e-mail уже сужествует " + newUser.getEmail());
        }
        User updetedUser = userRepository.update(newUser);
        log.info(String.format("Обновлен пользователь %s", newUser));
        return updetedUser;
    }

    public User getUserById(Long id) {
        if (!userRepository.checkUserExists(id)) {
            throw new NotFoundException(String.format("Пользователь с Id %s не найден.", id));
        }
        return userRepository.getUserById(id);
    }

    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new ConditionsNotMetException("Пользователь не может добавить в друзья сам себя " + userId);
        }
        if (!userRepository.checkUserExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + "не найден");
        }
        if (!userRepository.checkUserExists(friendId)) {
            throw new NotFoundException("Друг с id " + userId + "не найден");
        }
        userRepository.addFriend(userId, friendId);
        log.info("Пользователь с id {} добавил в друзья пользователя с id {}", userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (!userRepository.checkUserExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + "не найден");
        }
        if (!userRepository.checkUserExists(friendId)) {
            throw new NotFoundException("Друг с id " + userId + "мне найден");
        }
        userRepository.deleteFriend(userId, friendId);
        log.info("Пользователь с id {} удалил из друзей пользователя с id {}", userId, friendId);
    }

    public Collection<User> getUserFriends(Long userId) {
        if (!userRepository.checkUserExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + "мне найден");
        }
        return userRepository.getUserFriends(userId);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        if (!userRepository.checkUserExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (!userRepository.checkUserExists(otherId)) {
            throw new NotFoundException("Пользователь с id " + otherId + " не найден");
        }
        return userRepository.getCommonFriends(userId, otherId);
    }

    private void checkUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ConditionsNotMetException("email пользователя не может быть пустым и должен содержать @: " + user);
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ConditionsNotMetException("Логин Пользователя не может быть пустым: " + user);
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ConditionsNotMetException("День рождения Пользователя не может быть пустым или в будущем: " + user);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
