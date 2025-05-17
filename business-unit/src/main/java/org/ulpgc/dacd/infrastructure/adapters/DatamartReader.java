package org.ulpgc.dacd.infrastructure.adapters;

import org.ulpgc.dacd.application.domain.model.EnergyData;
import org.ulpgc.dacd.application.domain.model.WeatherData;
import org.ulpgc.dacd.infrastructure.port.DatamartQueryPort;


import java.sql.*;
import java.util.Optional;

public class DatamartReader implements DatamartQueryPort {
    private static final String DB_URL = "jdbc:sqlite:data.db";
    @Override
    public Optional<WeatherData> getLatestWeatherData(String city) {
        String query = """
                SELECT temperature, humidity, pressure, wind_speed, description, city_name
                FROM weather_data
                WHERE city_name = ?
                ORDER BY timestamp DESC
                LIMIT 1
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                WeatherData data = new WeatherData();
                data.setTemperature(rs.getDouble("temperature"));
                data.setHumidity(rs.getInt("humidity"));
                data.setPressure(rs.getInt("pressure"));
                data.setWindSpeed(rs.getDouble("wind_speed"));
                data.setDescription(rs.getString("description"));
                data.setCityName(rs.getString("city_name"));
                return Optional.of(data);
            }

        } catch (SQLException e) {
            System.err.println("Weather query failed: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<EnergyData> getLatestEnergy(String indicator) {
        String query = """
                SELECT indicator, value, percentage, unit, timestamp, geo_name, geo_id
                FROM re_data
                WHERE indicator = ?
                ORDER BY timestamp DESC
                LIMIT 1
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, indicator);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new EnergyData(
                        rs.getString("indicator"),
                        rs.getDouble("value"),
                        rs.getDouble("percentage"),
                        rs.getString("unit"),
                        rs.getString("timestamp"),
                        rs.getString("geo_name"),
                        rs.getInt("geo_id")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Energy query failed: " + e.getMessage());
        }

        return Optional.empty();
    }
}
