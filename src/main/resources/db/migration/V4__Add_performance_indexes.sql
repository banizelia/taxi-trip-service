-- V4__Add_performance_indexes.sql

-- Индекс для оптимизации поиска по диапазону дат
CREATE INDEX IF NOT EXISTS idx_trips_pickup_datetime
    ON public.trips USING btree
    (pickup_datetime);

-- Композитный индекс для оптимизации JOIN и фильтрации по дате
CREATE INDEX IF NOT EXISTS idx_trips_pickup_date_weather_id
    ON public.trips USING btree
    (pickup_date, id);

-- Индекс для оптимизации поиска по скорости ветра
CREATE INDEX IF NOT EXISTS idx_weather_wind_speed
    ON public.weather_observations USING btree
    (average_wind_speed);

-- Композитный индекс для оптимизации запросов с фильтрацией по дате и скорости ветра
CREATE INDEX IF NOT EXISTS idx_weather_date_wind_speed
    ON public.weather_observations USING btree
    (date, average_wind_speed);
