# Taxi Trip Service

**Taxi Trip Service** — это REST API для управления данными поездок на такси, включая возможность фильтрации поездок,
экспорта данных в Excel и работы с избранными поездками.

## Оглавление

1. [Технологии](#технологии)
2. [Требования](#требования)
3. [Установка](#установка)
4. [Запуск приложения](#запуск-приложения)
5. [Тестирование](#тестирование)
6. [Автор](#автор)

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
   git clone https://github.com/banizelia/taxi-trip-service.git
   ```

2. Перейдите в директорию проекта:
   ```bash
   cd taxi-trip-service
   ```

3. Установите зависимости:
   ```bash
   mvn clean install
   ```

4. Создайте базу данных PostgreSQL:

   ```sql
   CREATE DATABASE taxi-trip-service-db;
   ```

5. Настройте параметры подключения к базе данных в файле `application.properties` (или `application.yml`):
   ```properties
   # Database settings
   spring.datasource.url=jdbc:postgresql://localhost:5432/taxi_trip_service
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password
   spring.jpa.hibernate.ddl-auto=update

   # Flyway settings
   spring.flyway.enabled=true
   spring.flyway.baseline-on-migrate=true
   spring.flyway.locations=classpath:db/migration

   # Flyway configuration
   spring.flyway.url=${spring.datasource.url}
   spring.flyway.user=${spring.datasource.username}
   spring.flyway.password=${spring.datasource.password}
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

3. Документация swagger будет доступна по адресу:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

## Тестирование

Для запуска тестов выполните:

```bash
mvn test
```

## Автор

- [banizelia](https://github.com/banizelia)
