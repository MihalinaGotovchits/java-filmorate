package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode()
public class User {
    private Long id;
    @NotBlank(message = "E-mail не может быть пустым.")
    @Email(message = "E-mail должен содержать '@'.")
    private String email;
    @NotBlank(message = "Логин не может быть пустымю")
    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent(message = "Дата рождения не может быть в будущем. ")
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
    private Set<Long> films = new HashSet<>();
}