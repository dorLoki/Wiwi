import random
import csv
from datetime import datetime, timedelta

def generate_random_times(start, end):
    """Generiert eine zufällige Start- und Endzeit innerhalb der gegebenen Stunden."""
    format = '%H:%M'
    begin = datetime.strptime(start, format)
    end = datetime.strptime(end, format)
    delta = end - begin
    begin_offset = random.randint(0, delta.seconds // 2)  # Zufällige Startzeit in der ersten Hälfte
    end_offset = random.randint(delta.seconds // 2, delta.seconds)  # Zufällige Endzeit in der zweiten Hälfte
    new_begin = begin + timedelta(seconds=begin_offset)
    new_end = begin + timedelta(seconds=end_offset)
    return new_begin.strftime(format), new_end.strftime(format)

def generate_dates(year):
    """Generiert ein zufälliges Datum im gegebenen Jahr."""
    start_date = datetime(year, 1, 1)
    end_date = datetime(year, 12, 31)
    delta = end_date - start_date
    random_days = random.randint(1, delta.days)
    random_date = start_date + timedelta(days=random_days)
    return random_date.strftime('%d.%m.%Y')

def create_csv():
    with open('machine_operations.csv', 'w', newline='') as file:
        writer = csv.writer(file, delimiter=';')
        writer.writerow(['Maschine', 'Datum', 'Beginn der Fertigung', 'Ende der Fertigung'])
        for machine_id in range(1, 21):  # Maschinen 1 bis 20
            for _ in range(20):  # 20 Einträge pro Maschine
                year = random.choice([2023, 2024])
                date = generate_dates(year)
                start_time, end_time = generate_random_times('07:00', '17:00')
                writer.writerow([machine_id, date, start_time, end_time])

# Skript ausführen
create_csv()
