```markdown
# 🌤️ Enerweather

Enerweather is a Java-based application that collects, processes, and stores both renewable energy and weather data for analysis of energy production patterns in relation to weather conditions in Spain.

---

## Project Goal

To analyze the relationship between weather (cloudiness and wind speed) and renewable energy production (solar and wind) in major Spanish cities by combining real-time and historical data sources.

---

## Value Proposition

Enerweather provides:

- Insight into how **weather conditions** affect **energy production**.
- Per-city estimations of solar and wind energy production based on **population-adjusted distribution**.
- A clean, event-driven architecture for scalable, real-time analytics.

---

## System Architecture

The architecture follows clean modular principles and is inspired by the Lambda pattern. It consists of:

- **WeatherFeeder** – Fetches weather data from OpenWeatherMap and publishes to `weather` topic.
- **EnergyFeeder** – Retrieves renewable energy data from REE and publishes to `energy` topic.
- **EventStoreBuilder** – Subscribes to both topics and stores `.events` files by date.
- **BusinessUnit** – Consumes historical and real-time events to generate insights and store them in a datamart.

> [Final Architecture Diagram](docs/final.png)

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
```

eventstore/{topic}/{source}/{YYYYMMDD}.events

````

### BusinessUnit

- Subscribes to real-time and reads from `eventstore/`.
- Builds a datamart as CSV files.
- Generates per-city energy distribution based on population.

---

## Class Diagrams

```markdown
### EventStoreBuilder
![Event Store Builder Diagram](docs/builder.png)
````
```markdown
### WeatherFeeder
![Weather Feeder Diagram](docs/WeatherFeeder.png)
````
```markdown
### EnergyFeeder
![Energy Feeder Diagram](docs/EnergyFeeder.png)
````


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
git clone https://github.com/your-org/enerweather.git
cd enerweather
mvn clean package
```

### Run ActiveMQ

```bash
# From ActiveMQ installation
./bin/activemq start
```

---

## Running Modules

Each module is packaged independently:

```bash
# Start feeders (in separate terminals)
java -jar weather-feeder.jar
java -jar energy-feeder.jar

# Start event store builder
java -jar event-store-builder.jar

# Run analysis
java -jar business-unit.jar
```

---

## Output Example

After running, BusinessUnit will generate a CSV:

```
datamart/energy_distribution.csv
```

Example contents:

```csv
City,Population,Solar(MW),Wind(MW)
Madrid,3416771,150.73,320.45
Barcelona,1702547,75.34,160.05
Valencia,825948,36.55,77.68
...
```

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
