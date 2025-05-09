package org.ulpgc.dacd.infrastructure.persistence;

import org.ulpgc.dacd.application.port.WeatherRepositoryPort;
import org.ulpgc.dacd.domain.model.Weather;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class WeatherRepository implements WeatherRepositoryPort {
    @Override
    public void save(Weather data){
        String sql = "INSERT INTO weather_data (temperature, humidity, pressure, wind_speed, description, city_name, timestamp)" +
                "VALUES (?,?,?,?,?,?, datetime('now'))";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDouble(1, data.getTemperature());
            ps.setInt(2, data.getHumidity());
            ps.setInt(3, data.getPressure());
            ps.setDouble(4, data.getWindSpeed());
            ps.setString(5, data.getDescription());
            ps.setString(6, data.getCityName());

            ps.executeUpdate();
        }catch (Exception e){
            System.out.println("Error" + e.getMessage());
        }

    }
    @Override
    public Optional<Weather> findLatest(String city) {
        String sql = """
                SELECT temperature, humidity, pressure, wind_speed, description, city_name
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
                    Weather d = new Weather();
                    d.setTemperature(rs.getDouble("temperature"));
                    d.setHumidity(rs.getInt("humidity"));
                    d.setPressure(rs.getInt("pressure"));
                    d.setWindSpeed(rs.getDouble("wind_speed"));
                    d.setDescription(rs.getString("description"));
                    d.setCityName(rs.getString("city_name"));
                    return Optional.of(d);
                }
            }
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
        return Optional.empty();
    }
}