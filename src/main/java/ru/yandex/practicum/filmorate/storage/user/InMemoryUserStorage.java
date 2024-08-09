package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        user.setId(getNextId());
        log.debug(user.toString());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.error(newUser.toString());
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getName().isEmpty() || newUser.getName().isBlank()) {
                log.info(newUser.toString(), " Пользователю присвоено имя {}", newUser.getLogin());
                newUser.setName(newUser.getLogin());
            }

            oldUser.setName(newUser.getName());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setBirthday(newUser.getBirthday());
            oldUser.setLogin(newUser.getLogin());
            log.info("Данные пользователя {} обновлены", oldUser.getName());
            return oldUser;
        }
        log.error(newUser.toString());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public void deleteUserById(Long id) {
        users.remove(id);
    }


    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    /**
     * добавление друга
     */
    @Override
    public void addFriend(Long userId, Long friendId) {
        users.get(userId).getFriends().add(friendId);
        users.get(friendId).getFriends().add(userId);
    }

    /**
     * удаление друга
     */
    @Override
    public void deleteFriend(Long userId, Long friendId) {
        users.get(userId).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(userId);
    }

    /**
     * список друзей конкретного пользователя
     */
    @Override
    public Collection<User> getUserFriends(Long userId) {
        Set<Long> userFriendsId = Optional.ofNullable(users.get(userId))
                .map(User::getFriends)
                .orElse(Collections.emptySet());
        return userFriendsId.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    /**
     * список общих друзей
     */
    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        Set<Long> firstUserFriendsId = users.get(userId).getFriends();
        Set<Long> secondUserFriendsId = users.get(otherId).getFriends();
        firstUserFriendsId.retainAll(secondUserFriendsId);
        return firstUserFriendsId.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(Long userId, Long filmId) {
        users.get(userId).getFilms().add(filmId);
    }

    @Override
    public void deleteLike(Long userId, Long filmId) {
        users.get(userId).getFilms().remove(filmId);
    }

    @Override
    public Long getNextId() {
        Long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
