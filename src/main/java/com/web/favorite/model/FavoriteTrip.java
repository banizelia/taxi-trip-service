package com.web.favorite.model;

import com.web.trip.model.Trip;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model of a favorite trip, containing information about the trip, its position in the favorites list, and the record version.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "favorite_trips")
public class FavoriteTrip {

    /**
     * Record version for optimistic locking implementation.
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