import psycopg2
from io import StringIO
from tqdm import tqdm

NUM_ROWS = 1_000_000  # Количество строк для вставки за один запуск
BATCH_SIZE = 10_000  # Размер партии вставки
sparsify = True


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

        # Шаг 1: Найти максимальный trip_id в таблице
        cursor.execute("SELECT MAX(trip_id) FROM favorite_trips;")
        result = cursor.fetchone()
        max_trip_id = result[0] if result[0] is not None else 0
        count = max_trip_id + 1
        print(f"Последний trip_id: {max_trip_id}. Начинаем вставку с trip_id = {count}")

        # Шаг 2: Вставка данных в батчах с использованием tqdm для прогресса
        with tqdm(range(0, NUM_ROWS, BATCH_SIZE), desc="Вставка данных", unit="батч") as pbar:
            for batch_start in pbar:
                buffer = StringIO()

                for _ in range(BATCH_SIZE):
                    trip_id = count

                    if sparsify:
                        position = count * 10000000
                    else:
                        position = count

                    version = 1
                    row = f"{trip_id}\t{position}\t{version}\n"
                    buffer.write(row)
                    count += 1

                buffer.seek(0)
                cursor.copy_from(
                    buffer,
                    'favorite_trips',
                    sep='\t',
                    null='\\N',
                    columns=(
                        'trip_id',
                        'position',
                        'version'
                    )
                )
                conn.commit()

                # Обновление дополнительной информации в прогресс-баре
                pbar.set_postfix({'Следующий trip_id': count})

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
