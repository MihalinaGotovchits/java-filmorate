package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRepository mpaRepository;

    public Collection<Mpa> getAll() {
        return mpaRepository.getAllMpa();
    }

    public Mpa getMpa(int mpaId) {
        if (!mpaRepository.checkMpaExists(mpaId)) {
            throw new NotFoundException("Рейтинг не найден");
        }
        return mpaRepository.getMpa(mpaId);
    }
}