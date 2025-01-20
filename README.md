# Taxi Trip Service

Проект представляет собой Spring Boot-приложение для управления поездками на такси, включающее функционал избранных поездок, экспорт в Excel, работу с погодными данными и т.д.

---

## Содержание

1. [Технологии и библиотеки](#технологии-и-библиотеки)
2. [Структура проекта](#структура-проекта)
3. [Подготовка окружения](#подготовка-окружения)
4. [Настройка базы данных](#настройка-базы-данных)
5. [Скрипты для вставки тестовых данных](#скрипты-для-вставки-тестовых-данных)
6. [Запуск приложения](#запуск-приложения)
7. [Использование API](#использование-api)
8. [Swagger (OpenAPI) документация](#swagger-openapi-документация)
9. [Основные эндпоинты](#основные-эндпоинты)
10. [Формат конфигурации](#формат-конфигурации)
11. [Обработка ошибок](#обработка-ошибок)
12. [Лицензия и авторы](#лицензия-и-авторы)

---

## Технологии и библиотеки

- **Java 17+** (проект использует Jakarta packages, Spring Boot 3.x)
- **Spring Boot** — каркас приложения
- **Spring Data JPA** — доступ к данным
- **Hibernate** — ORM
- **Flyway** — миграции базы данных
- **PostgreSQL** — СУБД
- **Lombok** — избавляет от шаблонного кода (геттеры/сеттеры/конструкторы)
- **MapStruct** — маппинг сущностей
- **FastExcel** — библиотека для экспорта в Excel (XLSX)
- **Swagger (OpenAPI)** — документация и тестирование REST API
- **tqdm** (Python) — прогресс-бар для скриптов вставки данных
- **psycopg2** (Python) — драйвер для подключения к PostgreSQL из Python

---

## Структура проекта

Основные пакеты и файлы:

- `com.banizelia.taxi`
  - **Application.java** — точка входа в Spring Boot-приложение
  - **config/** — Spring и глобальные конфигурации (таймауты, порты, TimeZone, свойства для экспорта и т.п.)
  - **advice/** — глобальные обработчики исключений (ExceptionHandler)
  - **error/** — собственные классы исключений (PositionOverflowException, TripNotFoundException и др.)
  - **favorite/** — работа с избранными поездками (FavoriteTrip)
    - `model/` — сущности и DTO
    - `repository/` — репозиторий для FavoriteTrip
    - `service/` — бизнес-логика, включая перерасчёт позиций (sparsify)
  - **trip/** — работа с поездками (Trip)
    - `model/` — сущности и DTO
    - `repository/` — репозиторий для Trip
    - `service/` — сервисы фильтрации, экспорта, скачивания (DownloadTripService, FilterTripService и т.д.)
    - `export/` — логика экспорта (TripExcelExporter и т.д.)
    - `controller/` — REST-контроллер (TripController)
  - **weather/** — данные о погоде (Weather)
  - **util/** — утилитные классы (ApiErrorResponse, интерфейсы экспорта и др.)
- `resources/`
  - **application.yml** — основная конфигурация (Spring Boot, Flyway, PostgreSQL и др.)
  - **db/migration** — файлы миграций Flyway
    - `populate/` — Python-скрипты для массовой вставки тестовых данных (`insert_trips.py` и `insert_fav_trips.py`) и `requirements.txt`

---

## Подготовка окружения

1. **Java 17+**  
   Убедитесь, что установлена нужная версия JDK (рекомендуется 17 или выше).

2. **PostgreSQL**  
   Установите PostgreSQL 14+ (или совместимую версию), настройте пользователя и базу данных.

3. **Python 3.10+** (не обязательно, но нужно для запуска тестовых скриптов вставки данных)
   - Установите зависимости `pip install -r requirements.txt` (см. раздел [Скрипты для вставки тестовых данных](#скрипты-для-вставки-тестовых-данных)).

---

## Настройка базы данных

В файле [`application.yml`](src/main/resources/application.yml) по умолчанию указаны параметры для подключения к базе данных PostgreSQL:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/taxi-trip-service-db
    username: postgres
    password: 12345678
  ...
```

- `url: jdbc:postgresql://localhost:5433/taxi-trip-service-db`  
  Поменяйте при необходимости хост/порт/имя базы на свои значения.
- `username: postgres`, `password: 12345678`  
  Пользователь и пароль для подключения.

### Миграции

Проект настроен на Flyway, который автоматически применяет миграции из папки `db/migration` при старте, если указано:

```yaml
spring:
  flyway:
    enabled: true
    baseline-on-migrate: true
    locations: classpath:db/migration
```

Миграции будут выполнены автоматически при запуске приложения. Если нужно выполнить вручную, вы можете использовать Maven/Gradle-плагины Flyway, но обычно автозапуска достаточно.

---

## Скрипты для вставки тестовых данных

Чтобы вставить большое количество тестовых строк в таблицы **trips** и **favorite_trips**, в проекте есть Python-скрипты:
- [`insert_trips.py`](src/main/resources/db/migration/populate/insert_trips.py)
- [`insert_fav_trips.py`](src/main/resources/db/migration/populate/insert_fav_trips.py)

Они не выполняются автоматически Flyway, так как предназначены для выборочного массового наполнения. Скрипты используют:

- **psycopg2** — для подключения к PostgreSQL
- **tqdm** — для визуального прогресс-бара в консоли

### Установка Python-зависимостей

```bash
cd src/main/resources/db/migration/populate
pip install -r requirements.txt
```

### Запуск скриптов

```bash
python insert_trips.py
python insert_fav_trips.py
```

Параметры подключения к БД внутри самих скриптов (хост, порт, база, пользователь, пароль) можно отредактировать при необходимости.

---

## Запуск приложения

### Из IDE (IntelliJ IDEA, VS Code, Eclipse)

1. Откройте проект как Maven-проект (предположительно, что используется Maven).
2. Соберите проект (Lifecycle → `clean`, `install`).
3. Запустите класс **Application** или выполните в терминале:
   ```bash
   mvn spring-boot:run
   ```
   Либо запустите напрямую из меню IDE: Run → Application

### Из командной строки (Maven)

```bash
mvn clean package
java -jar target/taxi-trip-service-0.0.1-SNAPSHOT.jar
```

Параметры (порт, таймзона, timeout и т.д.) при необходимости переопределяются через аргументы `-D` или переменные окружения (Spring Boot Property Binding).

> По умолчанию приложение стартует на порту `7771` (см. `server-port: 7771` в `application.yml`).

---

## Использование API

После запуска ваше приложение будет доступно (по умолчанию) на адресе:
```
http://localhost:7771
```

---

## Swagger (OpenAPI) документация

В проекте настроено `@OpenAPIDefinition`, поэтому при запуске доступен swagger-ui. Обычно открывается по пути:
```
http://localhost:7771/swagger-ui/index.html
```
(или `/swagger-ui.html`, в зависимости от версии SpringDoc).

Там вы найдёте описание всех эндпоинтов и сможете их протестировать.

---

## Основные эндпоинты

Контроллеры — это классы в пакете `trip.controller`:

### `TripController` (уровень `api/v1/trips`)

- **GET** `/api/v1/trips`  
  Фильтрация поездок с параметрами:  
  - Пример: `?isFavorite=true&pickupDateTimeFrom=2010-01-01T00:00:00.000&pickupDateTimeTo=2010-12-31T23:59:59.999`
  - Поддерживается сортировка (Spring Data Pageable), включая сортировку по вложенным полям (`favoriteTrip.position`).

- **POST** `/api/v1/trips/{tripId}/favorite`  
  Добавить поездку в избранные.

- **DELETE** `/api/v1/trips/{tripId}/favorite`  
  Удалить поездку из избранных.

- **PUT** `/api/v1/trips/{tripId}/favorite/drag-and-drop?newPosition=...`  
  Меняет позицию в списке избранных (drag-and-drop).

- **GET** `/api/v1/trips/download`  
  Экспорт поездок в Excel (XLSX). Параметры фильтрации те же, что и для `filterTrips`.

Пример получения Excel:
```
GET http://localhost:7771/api/v1/trips/download?filename=trips_export
```
Вернёт файл `trips_export_YYYY-MM-DD_HH-mm-ss.xlsx` со списком поездок.

---

## Формат конфигурации

Главный файл — [`application.yml`](src/main/resources/application.yml).  
Ключевые настройки:

- **server-port**: Порт сервера (по умолч. 7771).
- **async-timeout-millis**: Таймаут асинхронных запросов (по умолч. 5400000 = 90 минут).
- **application-timezone**: Таймзона приложения (по умолч. GMT). При невалидном значении выбросит `InvalidTimeZoneException`.

### Конфигурация экспорта Excel

```yaml
excel-export:
  sheet-prefix: "trips_"
  max-rows-per-sheet: 1000000
  batch-size: 100000
```
- **sheet-prefix** — префикс для названий листов в Excel-файле
- **max-rows-per-sheet** — сколько строк максимум в одном листе (Excel обычно ограничивает ~1 048 576)
- **batch-size** — размер батча при записи

### Конфигурация списка избранных

```yaml
favorite-trip-list:
  position-gap: 10000000
  initial-position: 10000000
  min-gap: 100
  rebalance-threshold: 0.8
  batch-size: 10000
```
- **position-gap** — при вставках между элементами будет шаг
- **initial-position** — стартовое значение позиции в избранных
- **min-gap** — минимальный зазор при пересчёте
- **rebalance-threshold** — при достижении процента от `Long.MAX_VALUE` происходит «разрежение» (sparsify)
- **batch-size** — размер батча при массовом `sparsify`

---

## Обработка ошибок

В проекте много кастомных исключений и **ControllerAdvice** (глобальные обработчики):

- **GlobalExceptionHandler** — обрабатывает `OptimisticLockException`, `MethodArgumentNotValidException` и `PathElementException`.
- **ExportExceptionHandler** — обрабатывает `ExportException`.
- **InitializationExceptionHandler** — проблемы при инициализации (таймзона и т.п.).
- **PositionExceptionHandler** — ошибки, связанные с позициями в избранном.
- **TripExceptionHandler** — ошибки, связанные с поездками (не найден, уже в избранном и т.д.).

Каждый хендлер возвращает `ApiErrorResponse` (JSON), пример:
```json
{
  "status": 404,
  "error": "Trip Not Found",
  "message": "Such trip doesn't exist: 9999"
}
```

---

## Лицензия и авторы

Данный проект создан и поддерживается [banizelia](https://github.com/banizelia)