-- V4__Add_performance_indexes.sql

CREATE INDEX IF NOT EXISTS idx_trips_pickup_datetime
    ON public.trips (pickup_datetime);

CREATE INDEX IF NOT EXISTS idx_trips_pickup_date_weather_id
    ON public.trips (pickup_date, id);

CREATE INDEX IF NOT EXISTS idx_weather_wind_speed
    ON public.weather_observations (average_wind_speed);

CREATE INDEX IF NOT EXISTS idx_weather_date_wind_speed
    ON public.weather_observations (date, average_wind_speed);

CREATE INDEX IF NOT EXISTS idx_weather_observations_date
    ON public.weather_observations (date ASC NULLS LAST);

CREATE INDEX IF NOT EXISTS idx_trips_pickup_date ON public.trips (pickup_date);

CREATE INDEX IF NOT EXISTS idx_favorite_trip_position
    ON public.favorite_trips (position);