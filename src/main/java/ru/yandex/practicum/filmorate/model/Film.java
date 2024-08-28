package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Film {
    private Long id;
    @NotNull(message = "Введите название фильма")
    @NotBlank(message = "Введите название фильма.")
    private String name;
    @NotNull(message =  "Описание фильма не может быть пустым")
    @NotBlank(message =  "Описание фильма не может быть пустым")
    @Size(max = 200, message = "Описание не может превышать 200 символов.")
    private String description;
    @NotNull(message =  "Дата фильма не может быть пустой")
    private LocalDate releaseDate;
    @NotNull
    @Positive(message = "Продолжительность фильма не может быть меньше 0.")
    private int duration;
    @Min(value = 0)
    private int grade;
    private String genre;
    private String rating;
}
