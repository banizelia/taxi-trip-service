package com.web.repository;

import com.web.model.Trip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TripsRepositoryTest {

    @Autowired
    private TripsRepository tripsRepository;

    @Test
    void testFilter() {
        // Данные для фильтрации
        LocalDateTime startDateTime = LocalDateTime.of(2016, 1, 1, 0,0,0);
        LocalDateTime endDateTime = LocalDateTime.of(2016, 1, 31, 23,59,59);
        Double minWindSpeed = 0.0;
        Double maxWindSpeed = 10.0;

        Pageable pageable = PageRequest.of(0, 10);

        // Вызов метода фильтрации
        Page<Trip> trips = tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);

        // Проверка того, что метод вернул результаты (если данные существуют в базе данных)
        assertNotNull(trips);
        assertTrue(trips.getSize() <= 10); // Ограничение по количеству записей на страницу
    }
}
