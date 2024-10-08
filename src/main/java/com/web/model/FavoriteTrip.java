package com.web.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

/**
 * Модель избранной поездки, содержащая информацию о поездке, позиции в списке избранного и версии записи.
 */
@Entity
@Table(name = "favorite_trips")
public class FavoriteTrip {

    /**
     * Версия записи для реализации оптимистичной блокировки.
     */
    @Version
    private Long version;

    /**
     * Связь с моделью поездки, отображающая поездку, которая находится в избранном.
     */
    @OneToOne(mappedBy = "favoriteTrip", fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    /**
     * Идентификатор избранной поездки (основной ключ).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор поездки, которая находится в избранном.
     */
    @Column(name = "trip_id")
    private Long tripId;

    /**
     * Позиция поездки в списке избранного.
     */
    @Column(name = "position")
    private Long position;

    public FavoriteTrip() {
    }

    // Getters and setters

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
