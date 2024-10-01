-- Version: V1__Create_weather_observations.sql

CREATE TABLE IF NOT EXISTS public.weather_observations
(
    station_id character varying(255) COLLATE pg_catalog."default",
    station_name character varying(255) COLLATE pg_catalog."default",
    date date,
    average_wind_speed double precision,
    precipitation double precision,
    snow_depth double precision,
    snowfall double precision,
    max_temperature bigint,
    min_temperature bigint,
    weather_id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    CONSTRAINT weather_observations_pkey PRIMARY KEY (weather_id)
);

ALTER TABLE IF EXISTS public.weather_observations
    OWNER TO postgres;

CREATE INDEX IF NOT EXISTS idx_weather_observations_date
    ON public.weather_observations USING btree
    (date ASC NULLS LAST);

-- Лучше заменить COPY на SQL-запрос с INSERT, если возможно, либо использовать стратегию для загрузки данных вне Flyway миграций.

INSERT INTO public.weather_observations
(station_id, station_name, date, average_wind_speed, precipitation, snow_depth, snowfall, max_temperature, min_temperature, weather_id)
VALUES
('USW00094728', 'NY CITY CENTRAL PARK, NY US', '2016-01-27', 6.71, 0, 0, 9.1, 47, 34, 2583),
('USW00094728', 'NY CITY CENTRAL PARK, NY US', '2016-01-28', 4.47, 0, 0, 5.9, 42, 32, 2584),
('USW00094728', 'NY CITY CENTRAL PARK, NY US', '2016-01-29', 7.83, 0, 0, 5.9, 41, 30, 2585);
