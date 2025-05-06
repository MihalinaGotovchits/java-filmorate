package ru.yandex.practicum.filmorate.repository.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaRepository {
    Mpa getMpa(Integer mpaId);

    Collection<Mpa> getAllMpa();

    boolean checkMpaExists(Integer mpaId);
}
