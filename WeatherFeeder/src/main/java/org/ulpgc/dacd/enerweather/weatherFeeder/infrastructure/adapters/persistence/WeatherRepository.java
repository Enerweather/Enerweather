package org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.adapters.persistence;

import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.port.WeatherRepositoryPort;
import org.ulpgc.dacd.enerweather.weatherFeeder.application.domain.model.Weather;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Optional;

public class WeatherRepository implements WeatherRepositoryPort {
    @Override
    public void save(Weather data){
        String sql = "INSERT INTO weather_data (wind_speed, description, city_name, timestamp)" +
                "VALUES (?,?,?,?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDouble(1, data.getWindSpeed());
            ps.setString(2, data.getDescription());
            ps.setString(3, data.getCityName());
            ps.setString(4, data.getDate());

            ps.executeUpdate();
        } catch (Exception e){
            System.out.println("Error" + e.getMessage());
        }

    }
    @Override
    public Optional<Weather> findLatest(String city) {
        String sql = """
                SELECT wind_speed, description, city_name, timestamp 
                FROM   weather_data
                WHERE  city_name = ?
                ORDER BY timestamp DESC
                LIMIT 1
                """;
        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, city);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Weather d = new Weather(
                            rs.getDouble("wind_speed"),
                            rs.getString("description"),
                            rs.getString("city_name")
                            );
                    return Optional.of(d);
                }
            }
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
        return Optional.empty();
    }
}