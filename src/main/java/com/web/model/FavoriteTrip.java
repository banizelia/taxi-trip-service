package com.web.model;

import com.web.util.AllowedField;
import jakarta.persistence.*;

@Entity
@Table(name = "favorite_trips")
public class FavoriteTrip {

    @Version
    private Long version;

    @OneToOne(mappedBy = "favoriteTrip", fetch = FetchType.LAZY)
    private Trip trip;

    @Id
    @Column(name = "trip_id")
    @AllowedField
    private Long tripId;

    @Column(name = "position")
    @AllowedField
    private Long position;

    public FavoriteTrip() {
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
