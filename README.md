# java-filmorate
Учебный проект, представляющий из себя бэкенд для сервиса, предлагающий топ фильмов для просмотра, который формируется на основании предпочтений пользователя и оценок его друзей.

## Реализация проекта:
1. Реализация моделей film и user, хранения в памяти приложения, аннотаций, валидации,  кастомной валидации, логирования, тестов на валидацию. Вся логика  - в контроллерах.  
**Стек: Java 11, Spring Boot, Maven, REST, Lombok, Postman.**
2. Совершенствование архитектуры - вынесение хранения и бизес-логики в отдельные слои. Внедрение зависимостей. Реализация моделей likes и добавления в друзья. Работа с Optional, переменными пути, параметрами запроса. Создание ExceptionHandler.  
3. Реализация моделей genre и rating, хранения в БД, DAO и мапперов, CRUD-операций с использованием JdbcTemplate, интеграционное тестирование.  
**Стек: БД H2, JdbcTemplate, Junit5.**

## Функциональность

### 1.Фильмы

- Добавление фильма: *POST /films*
  Тело запроса:

```
{
  "name": "Kill Bill",
  "description": "And the bride came",
  "releaseDate": "2003-09-29",
  "duration": 111,
  "mpa": { "id": 4},
  "genres": [ {"id": 4},{"id": 6} ],
  "directors" : [ {"id": 2} ]
}
```

Тело ответа:

```
{
    "id": 1,
    "name": "Kill Bill",
    "description": "And the bride came",
    "releaseDate": "2003-09-29",
    "duration": 111,
    "mpa": {
        "id": 4,
        "name": "R"
    },
    "genres": [
        {
            "id": 4,
            "name": "Триллер"
        },
        {
            "id": 6,
            "name": "Боевик"
        }
    ],
    "directors": [
        {
            "id": 1,
            "name": "Quentin Tarantino"
        }
    ],
    "userIdsWhoLiked": null
}
```

- Обновление фильма: *PUT /films*
- Удаление фильма по id: *DELETE /films/{id}*
- Получение фильма по id:  *GET /films/{id}*
- Получение всех фильмов:  *GET /films*

### 2.Пользователи

- Добавление пользователя: *POST /users*
- Обновление пользователя: *PUT /users*
- Удаление пользователя по id: *DELETE /users/{id}*
- Получение всех пользователей:  *GET /users*

### 3.Друзья

- Добавить в друзья: *PUT /users/{id}/friends/{friendId}*
- Удалить из друзей: *DELETE /users/{id}/friends/{friendId}*
- Общие друзья: *GET /users/{id}/friends/common/{otherId}*

### 4.Лайки

- Добавить лайк фильму: *PUT /films/{id}/like/{userId}*
- Удалить лайк у фильма: *DELETE /films/{id}/like/{userId}*
- Получить топ фильмов: *GET /films/popular?count={count}*

### 5. Рекомендации

- Получить фильмы для пользователя по id: *GET /users/{userId}/recommendations*

### 6. Поиск

- Получить фильмы по подстроке: *GET /films/search?query={query}=title,director*
- Получить общие с другом фильмы: *GET /films/common?userId={userId}&friendId={friendId}*
- Получить популярные фильмы по жанру и году *GET /films/popular?count={limit}&genreId={genreId}&year={year}*

### 7. Отзывы

- Добавить отзыв: *POST /reviews*

Тело запроса

```
{
  "content": "This film is soo bad.",
  "isPositive": false,
  "userId": 1,
  "filmId": 1
}
```

- Обновить отзыв *PUT /reviews*

  Тело запроса

```
{
  "reviewId": 1,
  "content": "This film is not too bad.",
  "isPositive": true,
  "userId": 2,
  "filmId": 2,
  "useful": 10
}
```

- Удалить отзыв *PUT /reviews/{reviewId}*
- Пользователь ставит лайк отзыву: *PUT /reviews/{id}/like/{userId}*
- Пользователь ставит дизлайк отзыву: *PUT /reviews/{id}/dislike/{userId}*
- Пользователь удаляет лайк/дизлайк отзыву: *DELETE /reviews/{id}/like/{userId}*
- Пользователь удаляет дизлайк отзыву: *DELETE /reviews/{id}/dislike/{userId}*

### 8. Лента событий

- Получить ленту событий пользователя: *GET /users/{id}/feed*

  Пример ответа:

```
[
    {
        "timestamp": 123344556,
        "userId": 1,
        "eventType": "LIKE", // одно из значениий LIKE, REVIEW или FRIEND
			  "operation": "REMOVE", // одно из значениий REMOVE, ADD, UPDATE
        "eventId": 3, //primary key
        "entityId": 5   // идентификатор сущности, с которой произошло событие
    }
]
```

### 9. Жанры

- Получить все жанры: *GET /genres*
- Получить жанр по id: *GET /genres/{id}*

### 10. Рейтинги

- Получить все рейтинги: *GET /mpa*
- Получить рейтинг по id: *GET /mpa/{id}*

### 11. Режиссеры

- Создание режиссёра: *POST /directors*

  Тело запроса:
```
{
"name": "Quentin Tarantino"
}

```
- Изменение режиссёра: *PUT /directors*
- Получение режиссёра по id: *GET /directors/{id}*
- Список всех режиссёров: *GET /directors*
- Удаление режиссёра *DELETE /directors/{id}*

## 4 спринт: Групповой проект в команде 4 человек по [ссылке](https://github.com/EyesHead/java-filmorate)
В ходе работы были отточены навыки командной работы над проектом:  
анализ и планирование, проектирование, разработка, ревью, тестирование и стабилизация, отладка и передача заказчику, презентация, выступление.  
Реализованы такие функциональности, как отзывы, поиск, общие фильмы, рекомендации, лента событий, популярные фильмы, фильмы по режиссёрам, удаление фильмов и пользователей.

### Запуск
Для запуска необходим установленный Docker.
1. Скачать архив на компьютер.
```
git@github.com:EyesHead/java-filmorate.git
```
2. Перейдите в директорию с файлом *compose.yml*
3. В терминале введите команду:
```
docker compose up -d
```
