package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            log.error(user.toString());
            throw new ConditionsNotMetException("E-mail не может быть пустым и должен содержать символ '@'");
        }
        if (user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error(user.toString());
            throw new ConditionsNotMetException("Логин не может содержать пробелы и быть пустым");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error(user.toString());
            throw new ConditionsNotMetException("Дата рождения не может быть в будующем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info(user.toString());
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        log.trace("Пользователю {} присвоен Id № {}", user.getName(), user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен", user.getName());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.error(newUser.toString());
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() == null || newUser.getEmail().isBlank() || (!newUser.getEmail().contains("@"))) {
                log.error(newUser.toString());
                throw new ConditionsNotMetException("E-mail не может быть пустым и содержать знак '@'");
            }
            if (newUser.getLogin().isEmpty() || newUser.getLogin().isBlank() || newUser.getLogin().contains(" ")) {
                log.error(newUser.toString());
                throw new ConditionsNotMetException("Логин не может содержать пробелы и быть пустым");
            }
            if (newUser.getBirthday().isAfter(LocalDate.now())) {
                log.error(newUser.toString());
                throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
            }
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

    @GetMapping
    public Collection<User> findAllFilms() {
        return users.values();
    }

    /**
     * генерация ID
     */
    private Long getNextId() {
        Long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
