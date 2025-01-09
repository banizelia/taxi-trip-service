import psycopg2
import random
from datetime import datetime, timedelta
from io import StringIO
from tqdm import tqdm

NUM_ROWS = 2_000_000  # Количество строк для вставки
BATCH_SIZE = 10_000  # Размер партии вставки


def random_pickup_datetime():
    start = datetime(2016, 1, 1, 0, 0, 0)
    end = datetime(2016, 1, 31, 23, 59, 59)
    delta = end - start
    random_seconds = random.randint(0, int(delta.total_seconds()))
    return start + timedelta(seconds=random_seconds)


def random_store_and_fwd_flag():
    return random.choice(['Y', 'N'])


def random_nullable(field_type):
    if random.random() < 0.1:
        return '\\N'
    if field_type == 'float':
        return f"{round(random.uniform(0.0, 20.0), 2)}"
    return ''


def main():
    conn = None
    cursor = None
    try:
        # Подключение к базе данных
        conn = psycopg2.connect(
            host="localhost",
            port=5433,
            database="taxi-trip-service-db",
            user="postgres",
            password="12345678"
        )
        cursor = conn.cursor()
        conn.autocommit = False  # Управление транзакциями вручную

        # Шаг 1: Найти максимальный id в таблице trips
        cursor.execute("SELECT MAX(id) FROM trips;")
        result = cursor.fetchone()
        max_id = result[0] if result[0] is not None else 0
        count = max_id + 1
        print(f"Последний id: {max_id}. Начинаем вставку с id = {count}")

        # Обновить последовательность, чтобы избежать конфликтов
        update_sequence_query = f"""
            SELECT setval(
                pg_get_serial_sequence('trips', 'id'),
                {count},
                false
            );
        """
        cursor.execute(update_sequence_query)
        conn.commit()
        print(f"Последовательность 'trips_id_seq' обновлена до {count}.")

        # Шаг 2: Вставка данных в батчах с использованием tqdm для прогресса
        with tqdm(range(0, NUM_ROWS, BATCH_SIZE), desc="Вставка данных", unit="batch") as pbar:
            for batch_start in pbar:
                buffer = StringIO()
                for _ in range(BATCH_SIZE):
                    trip_id = count
                    vendor_id = random.randint(1, 2)
                    pickup_dt = random_pickup_datetime()
                    dropoff_dt = pickup_dt + timedelta(minutes=random.randint(1, 120))
                    passenger_count = random.randint(1, 6)
                    trip_distance = round(random.uniform(0.5, 20.0), 2)
                    rate_code_id = random.randint(1, 6)
                    store_and_fwd_flag = random_store_and_fwd_flag()
                    pickup_location_id = random.randint(1, 300)
                    dropoff_location_id = random.randint(1, 300)
                    payment_type = random.randint(1, 5)
                    fare_amount = round(random.uniform(5.0, 100.0), 2)
                    extra = round(random.uniform(0.0, 5.0), 2)
                    mta_tax = 0.5  # Фиксированное значение
                    tip_amount = round(random.uniform(0.0, 20.0), 2)
                    tolls_amount = round(random.uniform(0.0, 15.0), 2)
                    improvement_surcharge = 0.3  # Фиксированное значение
                    total_amount = round(
                        fare_amount + extra + mta_tax + tip_amount + tolls_amount + improvement_surcharge, 2
                    )
                    congestion_surcharge = random_nullable('float')
                    airport_fee = random_nullable('float')

                    row = (
                        f"{trip_id}\t{vendor_id}\t{pickup_dt}\t{dropoff_dt}\t{passenger_count}\t"
                        f"{trip_distance}\t{rate_code_id}\t{store_and_fwd_flag}\t"
                        f"{pickup_location_id}\t{dropoff_location_id}\t{payment_type}\t"
                        f"{fare_amount}\t{extra}\t{mta_tax}\t{tip_amount}\t{tolls_amount}\t"
                        f"{improvement_surcharge}\t{total_amount}\t{congestion_surcharge}\t{airport_fee}\n"
                    )
                    buffer.write(row)
                    count += 1

                buffer.seek(0)
                cursor.copy_from(
                    buffer,
                    'trips',
                    sep='\t',
                    null='\\N',
                    columns=(
                        'id',
                        'vendor_id',
                        'pickup_datetime',
                        'dropoff_datetime',
                        'passenger_count',
                        'trip_distance',
                        'rate_code_id',
                        'store_and_fwd_flag',
                        'pickup_location_id',
                        'dropoff_location_id',
                        'payment_type',
                        'fare_amount',
                        'extra',
                        'mta_tax',
                        'tip_amount',
                        'tolls_amount',
                        'improvement_surcharge',
                        'total_amount',
                        'congestion_surcharge',
                        'airport_fee'
                    )
                )
                conn.commit()

                # Обновление дополнительной информации в прогресс-баре
                pbar.set_postfix({'Следующий id': count})

        print("Вставка данных завершена успешно.")

    except Exception as e:
        print(f"Произошла ошибка: {e}")
        if conn:
            conn.rollback()
    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()


if __name__ == "__main__":
    main()
