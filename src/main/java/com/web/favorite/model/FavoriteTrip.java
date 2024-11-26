package com.web.favorite.model;

import com.web.trip.model.Trip;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "favorite_trips")
public class FavoriteTrip {

    /**
     * Record version for optimistic locking
     */
    @Version
    private Long version;

    @OneToOne(mappedBy = "favoriteTrip", fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_id")
    private Long tripId;

    @Column(name = "position")
    private Long position;
}