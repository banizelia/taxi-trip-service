# Taxi Trip Service

**Taxi Trip Service** — это REST API для управления данными поездок на такси, включая возможность фильтрации поездок, экспорта данных в Excel и работы с избранными поездками.

## Оглавление
1. [Технологии](#технологии)
2. [Требования](#требования)
3. [Установка](#установка)
4. [Запуск приложения](#запуск-приложения)
5. [API](#api)
6. [Тестирование](#тестирование)
7. [Автор](#автор)

## Технологии

Проект использует следующие технологии:
- **Java 21**
- **Spring Boot**
- **PostgreSQL**
- **Maven**
- **Hibernate (JPA)** для работы с базой данных
- **Apache POI** для экспорта данных в Excel
- **JUnit 5** и **Spring Test** для написания тестов

## Требования

Для запуска проекта необходимо:
- Java 21
- Maven 3.x
- PostgreSQL 12+

## Установка

1. Клонируйте репозиторий проекта:
   ```bash
   git clone https://github.com/ваш_репозиторий/название_проекта.git
   ```

2. Перейдите в директорию проекта:
   ```bash
   cd название_проекта
   ```

3. Установите зависимости:
   ```bash
   mvn clean install
   ```

4. Создайте базу данных PostgreSQL:

   ```sql
   CREATE DATABASE taxi_trip_service;
   ```

5. Настройте параметры подключения к базе данных в файле `application.properties` (или `application.yml`):
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/taxi_trip_service
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

## Запуск приложения

1. Соберите и запустите приложение с помощью Maven:
   ```bash
   mvn spring-boot:run
   ```

2. Приложение будет доступно по адресу:
   ```
   http://localhost:8080
   ```

## API

### 1. Фильтрация поездок
**GET** `/trips/filter`

**Параметры запроса:**
- `startDateTime` — начальная дата и время поездки (формат: `YYYY-MM-DD HH:MM:SS`)
- `endDateTime` — конечная дата и время поездки
- `minWindSpeed` — минимальная скорость ветра
- `maxWindSpeed` — максимальная скорость ветра
- `direction` — направление сортировки (asc/desc)
- `sortBy` — поле для сортировки (по умолчанию: `id`)
- `page` — номер страницы (по умолчанию: 0)
- `pageSize` — размер страницы (по умолчанию: 500)

**Пример запроса:**
```http
GET http://localhost:8080/trips/filter?startDateTime=2016-01-01 00:00:00&endDateTime=2016-01-31 23:59:59&minWindSpeed=0&maxWindSpeed=50&direction=asc&sortBy=id
```

### 2. Экспорт поездок в Excel
**GET** `/trips/download`

**Описание:** Экспорт всех поездок в формате Excel.

**Пример запроса:**
```http
GET http://localhost:8080/trips/download
```

### 3. Управление избранными поездками
- **PUT** `/favorite-trips` — добавление поездки в избранное
  **Параметры запроса:**
  `tripId` — id поездки, которую надо добавить в избранное
  
- **PUT** `/favorite-trips/drag-and-drop` — удаление поездки из избранного
  **Параметры запроса:**
  `tripId` — id поездки, которую надо переместить
  `newPosition` — позиция, на которую надо переместить поездку

- **DELETE** `/favorite-trips/` — удаление поездки из избранного
  **Параметры запроса:**
  `tripId` — id поездки, которую надо переместить
  
- **GET** `/favorite-trips` — получение всех избранных поездок

## Тестирование

Для запуска тестов выполните:
```bash
mvn test
```

## Автор

- [banizelia](https://github.com/banizelia)
