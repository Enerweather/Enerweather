# 🌤️ Enerweather

Enerweather is a Java-based application that analyzes the relationship between weather conditions and renewable energy production across major Spanish cities. It integrates real-time and historical data and presents actionable insights through an interactive CLI.

---

## Project Goal

To analyze the relationship between weather (cloudiness and wind speed) and renewable energy production (solar and wind) in major Spanish cities by combining real-time and historical data sources.

---

## System Architecture

The architecture follows clean modular principles and is inspired by the Lambda pattern. It consists of:

- **WeatherFeeder** – Fetches weather data from OpenWeatherMap and publishes to `weather` topic.
- **EnergyFeeder** – Retrieves renewable energy data from REE and publishes to `energy` topic.
- **EventStoreBuilder** – Subscribes to both topics and stores `.events` files by date.
- **BusinessUnit** – Consumes real-time messages.
Rebuilds a historical datamart.
Displays data and recommendations through a CLI.

![Final Architecture Diagram](images/Arquitectura.jpg)

---

## Modules

### WeatherFeeder

- Fetches current weather for 29 Spanish cities via OpenWeatherMap API.
- Extracts `windSpeed`, `description`, `cityName`.
- Publishes to `weather` topic on ActiveMQ.

### EnergyFeeder

- Connects to REE API.
- Captures `solarEnergy` and `windEnergy`.
- Publishes to `energy` topic.

### EventStoreBuilder

- Subscribes to both `weather` and `energy`.
- Saves events as JSON lines in `.events` files:

eventstore/{topic}/{source}/{YYYYMMDD}.events


### BusinessUnit

- Subscribes to real-time and reads from `eventstore/`.
- Builds a CSV-based datamart organized by date and type.
- Includes a CLI with options to:
  - View stored data by date/type.
  - Rebuild the entire datamart from events.
  - Generate recommendations for top cities in solar or wind energy.
  - Quit application.

---

## Class Diagrams


### EventStoreBuilder
![Event Store Builder Diagram](images/EventStoreBuilder.jpg)


### WeatherFeeder
![Weather Feeder Diagram](images/WeatherFeeder.png)


### EnergyFeeder
![Energy Feeder Diagram](images/EnergyFeeder.png)


### BusinessUnit
![Business Unit Diagram](images/BusinessUnit.jpg)
---

## Technologies Used

* Java 21
* Apache ActiveMQ
* Maven
* SQLite JDBC
* Gson (JSON)
* CSVWriter

---

## 🚀 Getting Started

### Prerequisites

* Java 21
* Apache ActiveMQ (tcp://localhost:61616)
* Maven

### Setup

```bash
git clone https://github.com/enerweather/enerweather.git
```

### Run ActiveMQ
```bash
# From ActiveMQ installation
./bin/activemq start
```

Visit http://localhost:8161/ (login: admin / admin).

### Run modules
Run each individual module, first EventStoreBuilder, then both feeders, and finally the BusinessUnit

### Interact with the console
Once inside the BusinessUnit module, use the interactive CLI:
1. View Data
  - Choose between weather or energy.
  - Select a date.
  - Filter and browse the corresponding dataset.
2. Recommendations
  - View top 3 cities by solar or wind energy levels.
3. Quit
  - Exit the application.



---

## Design Principles

* Modular architecture – Each module is independent and focused on a single responsibility.
* Event-driven – Modules communicate via ActiveMQ topics.
* Hexagonal architecture – Use of ports and adapters.
* Historical replay – Events can be replayed from .events files to rebuild the datamart.

---

## Sample Event Structure

### Weather

```json
{
  "timestamp": "2025-05-18T13:00:00Z",
  "cityName": "Madrid",
  "description": "overcast clouds",
  "windSpeed": 4.2
}
```

### Energy

```json
{
  "timestamp": "2025-05-18T13:00:00Z",
  "indicator": "solarEnergy",
  "value": 2300.5
}
```

---

## 📄 Authors

* **Leonoor Antje Barton**
* **Joel Ojeda Santana**


---
