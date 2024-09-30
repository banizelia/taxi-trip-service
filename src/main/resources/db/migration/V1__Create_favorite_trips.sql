-- Version: V1__Create_favorite_trips.sql

CREATE TABLE IF NOT EXISTS public.favorite_trips
(
    trip_id bigint NOT NULL,
    position bigint,
    version bigint,
    CONSTRAINT favorite_trips_pkey PRIMARY KEY (trip_id)
);

ALTER TABLE IF EXISTS public.favorite_trips
    OWNER TO postgres;

CREATE UNIQUE INDEX IF NOT EXISTS idx_favorite_trips_trip_id
    ON public.favorite_trips USING btree
    (trip_id ASC NULLS LAST);



-- Лучше заменить COPY на SQL-запрос с INSERT, если возможно, либо использовать стратегию для загрузки данных вне Flyway миграций.
INSERT INTO public.favorite_trips (trip_id, position, version) VALUES
(1, 1, 0),
(2, 2, 0),
(3, 3, 0);