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
                SELECT wind_speed, description, city_name
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
                double windSpeed = rs.getDouble("wind_speed");
                String description = rs.getString("description");
                String cityName = rs.getString("city_name");
                WeatherData data = new WeatherData(windSpeed, description, cityName);
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
                SELECT indicator, value, timestamp
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
                        rs.getString("timestamp")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Energy query failed: " + e.getMessage());
        }

        return Optional.empty();
    }
}
