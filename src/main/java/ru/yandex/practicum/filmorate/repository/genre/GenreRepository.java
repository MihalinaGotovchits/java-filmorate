package ru.yandex.practicum.filmorate.repository.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreRepository {
    Genre getGenre(Integer genreId);

    Collection<Genre> getAllGenre();

    boolean checkGenreExists(Integer genreId);

    boolean checkGenresExist(List<Integer> genreIds);
}
