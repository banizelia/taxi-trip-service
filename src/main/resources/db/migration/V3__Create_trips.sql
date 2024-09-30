-- Table: public.trips

-- DROP TABLE IF EXISTS public.trips;

CREATE TABLE IF NOT EXISTS public.trips
(
    id bigint,
    vendor_id character varying(255) COLLATE pg_catalog."default",
    pickup_datetime timestamp without time zone,
    dropoff_datetime timestamp without time zone,
    passenger_count integer,
    trip_distance double precision,
    rate_code_id character varying(255) COLLATE pg_catalog."default",
    store_and_fwd_flag character varying(255) COLLATE pg_catalog."default",
    pickup_location_id integer,
    dropoff_location_id integer,
    payment_type character varying(255) COLLATE pg_catalog."default",
    fare_amount double precision,
    extra double precision,
    mta_tax double precision,
    tip_amount double precision,
    tolls_amount double precision,
    improvement_surcharge double precision,
    total_amount double precision,
    congestion_surcharge double precision,
    airport_fee double precision,
    pickup_date date,
    CONSTRAINT fk_weather_observations FOREIGN KEY (pickup_date)
        REFERENCES public.weather_observations (date) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.trips
    OWNER to postgres;

COPY favorite_trips 
FROM '*\trips.csv.csv' DELIMITER ',' CSV HEADER;

-- Лучше заменить COPY на SQL-запрос с INSERT, если возможно, либо использовать стратегию для загрузки данных вне Flyway миграций.

INSERT INTO public.trips
(id, vendor_id, pickup_datetime, dropoff_datetime, passenger_count, trip_distance, rate_code_id, store_and_fwd_flag, pickup_location_id, dropoff_location_id, payment_type, fare_amount, extra, mta_tax, tip_amount, tolls_amount, improvement_surcharge, total_amount, congestion_surcharge, airport_fee, pickup_date)
VALUES
(1, '2', '2016-01-28 15:25:04', '2016-01-28 15:52:05', 5, 2.06, '1', 'N', 239, 263, '1', 16.5, 0, 0.5, 3, 0, 0.3, 20.3, NULL, NULL, '2016-01-28'),
(2, '2', '2016-01-28 15:31:49', '2016-01-28 15:38:42', 2, 0.84, '1', 'N', 186, 230, '1', 6.5, 0, 0.5, 1.46, 0, 0.3, 8.76, NULL, NULL, '2016-01-28'),
(3, '2', '2016-01-28 15:54:52', '2016-01-28 16:31:23', 1, 10.54, '1', 'N', 230, 138, '1', 32.5, 0, 0.5, 5, 5.54, 0.3, 43.84, NULL, NULL, '2016-01-28'),
(4, '2', '2016-01-28 15:04:08', '2016-01-28 15:15:39', 1, 0.95, '1', 'N', 48, 161, '1', 8.5, 0, 0.5, 1.86, 0, 0.3, 11.16, NULL, NULL, '2016-01-28'),
(5, '2', '2016-01-28 15:16:26', '2016-01-28 15:19:53', 1, 0.5, '1', 'N', 161, 161, '1', 4, 0, 0.5, 0.96, 0, 0.3, 5.76, NULL, NULL, '2016-01-28'),
(6, '2', '2016-01-28 15:20:36', '2016-01-28 15:38:35', 1, 2.45, '1', 'N', 161, 114, '1', 12.5, 0, 0.5, 2.66, 0, 0.3, 15.96, NULL, NULL, '2016-01-28'),
(7, '2', '2016-01-28 15:39:35', '2016-01-28 15:47:26', 1, 0.99, '1', 'N', 114, 234, '2', 6.5, 0, 0.5, 0, 0, 0.3, 7.3, NULL, NULL, '2016-01-28'),
(8, '2', '2016-01-28 15:48:46', '2016-01-28 16:10:55', 1, 1.93, '1', 'N', 234, 48, '1', 14.5, 0, 0.5, 3.06, 0, 0.3, 18.36, NULL, NULL, '2016-01-28'),
(9, '2', '2016-01-28 15:10:29', '2016-01-28 15:29:09', 5, 2, '1', 'N', 163, 239, '2', 12.5, 0, 0.5, 0, 0, 0.3, 13.3, NULL, NULL, '2016-01-28'),
(10, '2', '2016-01-28 15:37:56', '2016-01-28 15:47:22', 5, 1.53, '1', 'N', 239, 166, '1', 8.5, 0, 0.5, 2.79, 0, 0.3, 12.09, NULL, NULL, '2016-01-28');
