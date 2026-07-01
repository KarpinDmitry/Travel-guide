# Travel Guide

REST API путеводителя по городу. Позволяет просматривать достопримечательности в радиусе от пользователя, оставлять отзывы и оценки.

## Архитектура

Микросервисное приложение на Spring Boot / Spring Cloud:

```
eureka-server       — реестр сервисов (порт 8761)
attractions-service — управление достопримечательностями (порт 8082)
reviews-service     — отзывы и оценки (порт 8081)
common-dto          — общие DTO и Feign-клиент (библиотека)
```

Сервисы взаимодействуют через **Spring Cloud OpenFeign** с балансировкой нагрузки через Eureka. Каждый сервис использует собственную базу данных PostgreSQL (паттерн Database per Service).

## Стек

- Java 21, Spring Boot 4.1.0, Spring Cloud 2025.1.2
- Spring Data JPA, Hibernate, PostgreSQL 15
- Flyway (миграции схемы БД)
- Spring Cloud Netflix Eureka, OpenFeign
- Docker Compose
- JUnit 5, Mockito, Testcontainers

## Запуск

**Требования:** Docker, Docker Compose

```bash
mvn package -DskipTests
docker-compose up --build
```

Eureka Dashboard: http://localhost:8761

> При первом запуске Flyway автоматически создаёт схему БД в обоих сервисах.

## API

### attractions-service · `localhost:8082`

| Метод | Путь | Описание |
|-------|------|----------|
| `POST` | `/attraction` | Создать достопримечательность |
| `PATCH` | `/attraction` | Обновить достопримечательность |
| `GET` | `/attraction/{id}` | Получить по id |
| `GET` | `/attraction` | Найти в радиусе (с фильтрами) |

**Создать достопримечательность**
```http
POST /attraction
Content-Type: application/json

{
  "name": "Эрмитаж",
  "category": "MUSEUM",
  "city": "Санкт-Петербург",
  "latitude": 59.9398,
  "longitude": 30.3146,
  "description": "Главный музей России"
}
```

Категории: `MUSEUM` `PARK` `MONUMENT` `TEMPLE` `GALLERY` `THEATER` `RESTAURANT` `HOTEL` `BEACH` `MARKET`

**Найти в радиусе**
```http
GET /attraction?latitude=59.93&longitude=30.31&radius=5
GET /attraction?latitude=59.93&longitude=30.31&radius=5&category=MUSEUM&minRating=4.0&limit=5&sort=RATING
```

| Параметр | Тип | Обязательный | По умолчанию | Описание |
|----------|-----|:---:|:---:|---------|
| `latitude` | double | да | — | Широта пользователя (-90..90) |
| `longitude` | double | да | — | Долгота пользователя (-180..180) |
| `radius` | double | да | — | Радиус поиска, км |
| `category` | enum | нет | — | Фильтр по категории |
| `minRating` | float | нет | — | Минимальная средняя оценка |
| `limit` | int | нет | 10 | Максимальное кол-во результатов |
| `sort` | enum | нет | `DISTANCE` | Сортировка: `DISTANCE` или `RATING` |

**Обновить достопримечательность**
```http
PATCH /attraction
Content-Type: application/json

{
  "id": 1,
  "name": "Государственный Эрмитаж",
  "description": "Один из крупнейших музеев мира"
}
```

Все поля кроме `id` необязательны.

---

### reviews-service · `localhost:8081`

| Метод | Путь | Описание |
|-------|------|----------|
| `POST` | `/reviews` | Создать отзыв |
| `GET` | `/reviews?attractionId={id}` | Отзывы по достопримечательности |
| `DELETE` | `/reviews/{id}` | Удалить отзыв |

**Создать отзыв**
```http
POST /reviews
Content-Type: application/json

{
  "attractionId": 1,
  "author": "Иван Петров",
  "rating": 5,
  "text": "Потрясающее место!"
}
```

`rating` — от 1 до 5, `text` необязателен.

После создания или удаления отзыва сервис автоматически пересчитывает и обновляет `averageRating` у достопримечательности.

## Структура проекта

```
Travel-guide/
├── common-dto/                  # Общие DTO, Feign-клиент AttractionClient
├── eureka-server/               # Spring Cloud Eureka Server
├── attractions-service/
│   ├── src/main/
│   │   ├── java/.../
│   │   │   ├── controller/      # AttractionController
│   │   │   ├── service/         # AttractionService
│   │   │   ├── repository/      # AttractionRepository (native SQL)
│   │   │   ├── entity/          # Attraction, AttractionCategory, AttractionSort
│   │   │   ├── dto/             # CreateAttractionDto, UpdateAttractionDto, ResponseAttractionDto
│   │   │   ├── mapper/          # AttractionMapper
│   │   │   └── exception/       # EntityNotFoundException, GlobalExceptionHandler
│   │   └── resources/
│   │       └── db/migration/    # V1 таблица, V2 функция distance_km, V3 поле average_rating
│   └── src/test/                # AttractionServiceTest (8 тестов)
├── reviews-service/
│   ├── src/main/
│   │   ├── java/.../
│   │   │   ├── controller/      # ReviewController
│   │   │   ├── service/         # ReviewService
│   │   │   ├── repository/      # ReviewRepository
│   │   │   ├── entity/          # Review
│   │   │   ├── dto/             # CreateReviewDto, ResponseReviewDto
│   │   │   ├── mapper/          # ReviewMapper
│   │   │   └── exception/       # ReviewNotFoundException, GlobalExceptionHandler
│   │   └── resources/
│   │       └── db/migration/    # V1 таблица reviews
│   └── src/test/                # ReviewServiceTest (5 тестов)
└── docker-compose.yml
```
