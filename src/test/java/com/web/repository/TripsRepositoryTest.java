package com.web.repository;

import com.web.model.Trip;
import com.web.repository.TripsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TripsRepositoryTest {

    @Autowired
    private TripsRepository tripsRepository;

    @Test
    void testFilter() {
        // Данные для фильтрации
        Timestamp startDateTime = Timestamp.valueOf("2016-01-01 00:00:00");
        Timestamp endDateTime = Timestamp.valueOf("2016-01-31 23:59:59");
        Double minWindSpeed = 0.0;
        Double maxWindSpeed = 10.0;

        Pageable pageable = PageRequest.of(0, 10);

        // Вызов метода фильтрации
        List<Trip> trips = tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);

        // Проверка того, что метод вернул результаты (если данные существуют в базе данных)
        assertNotNull(trips);
        assertTrue(trips.size() <= 10); // Ограничение по количеству записей на страницу
    }
}
