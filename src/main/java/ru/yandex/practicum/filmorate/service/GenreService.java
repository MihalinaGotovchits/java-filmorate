package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public Collection<Genre> getAllGenre() {
        return genreRepository.getAllGenre();
    }

    public Genre getGenre(int genreId) {
        if (!genreRepository.checkGenreExists(genreId)) {
            throw new NotFoundException("Жанр с id " + genreId + "не найден");
        }
        return genreRepository.getGenre(genreId);
    }
}
