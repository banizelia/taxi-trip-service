package com.web.service.favorite.managment.common;

import com.web.model.FavoriteTrip;
import com.web.model.enums.FavoriteTripEnum;
import com.web.repository.FavoriteTripRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;



@Service
@NoArgsConstructor
@AllArgsConstructor
public class SparsifierService {
    private static final long POSITION_GAP = FavoriteTripEnum.POSITION_GAP.getValue();

    private static final long INITIAL_POSITION = FavoriteTripEnum.INITIAL_POSITION.getValue();
    private static final int BATCH_SIZE = 10_000;

    private FavoriteTripRepository favoriteTripRepository;

    @Transactional
    public void sparsify() {
        List<FavoriteTrip> trips = favoriteTripRepository.findAllByOrderByPositionAsc();

        if (!trips.isEmpty()) {
            AtomicLong currentPosition = new AtomicLong(FavoriteTripEnum.INITIAL_POSITION.getValue());

            trips.forEach(trip -> {
                trip.setPosition(currentPosition.get());
                currentPosition.addAndGet(POSITION_GAP);
            });

            favoriteTripRepository.saveAll(trips);
        }
    }

}
