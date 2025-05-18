# 🌤️ Enerweather

Enerweather is a Java-based application that collects, processes, and stores both renewable energy and weather data for analysis of energy production patterns in relation to weather conditions in Spain.

---

## Project Goal

To analyze the relationship between weather (cloudiness and wind speed) and renewable energy production (solar and wind) in major Spanish cities by combining real-time and historical data sources.

---

## System Architecture

The architecture follows clean modular principles and is inspired by the Lambda pattern. It consists of:

- **WeatherFeeder** – Fetches weather data from OpenWeatherMap and publishes to `weather` topic.
- **EnergyFeeder** – Retrieves renewable energy data from REE and publishes to `energy` topic.
- **EventStoreBuilder** – Subscribes to both topics and stores `.events` files by date.
- **BusinessUnit** – Consumes historical and real-time events to generate insights and store them in a datamart.

> [Final Architecture Diagram](images/Arquitectura.jpg)

---

## Modules

### WeatherFeeder

- Fetches current weather for 29 Spanish cities.
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
- Builds a datamart as CSV files.
- Generates per-city energy distribution based on population.

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
* Apache ActiveMQ (localhost:61616)
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

### Run modules
Run each individual module, first EventStoreBuilder, then both feeders, and finally the BusinessUnit

### Interact with the console
Use the menu options to interact with the data:
- View Data: Browse and view data stored in the datamart
- Rebuild Datamart: Rebuild the datamart from the eventstore
- Quit: Exit the application


When viewing data:

- First select a data type (weather or energy)
- Then select a date to view data from
- Apply filters if needed

---

## Design Principles

* Modular architecture
* Event-driven with decoupling via ActiveMQ
* Clean separation of concerns
* Replay of historical data (via event store)

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
