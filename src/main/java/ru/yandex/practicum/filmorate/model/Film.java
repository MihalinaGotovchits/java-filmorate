package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Mpa mpa;
    private final Set<Genre> genres = new HashSet<>();
    private final Set<Long> likes = new HashSet<>();

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("film_id", id);
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate.toString());
        values.put("duration", duration);
        values.put("rating_id", mpa == null ? null : mpa.getId());
        return values;
    }
}
