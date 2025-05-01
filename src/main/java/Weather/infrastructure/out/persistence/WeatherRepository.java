package Weather.infrastructure.out.persistence;

import Weather.domain.model.WeatherData;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class WeatherRepository {
    public void save(WeatherData data){
        String sql = "INSERT INTO weather_data (temperature, humidity, pressure, wind_speed, description, city_name, timestamp)" +
                "VALUES (?,?,?,?,?,?, datetime('now'))";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setDouble(1, data.getTemperature());
            pstmt.setInt(2, data.getHumidity());
            pstmt.setInt(3, data.getPressure());
            pstmt.setDouble(4, data.getWindSpeed());
            pstmt.setString(5, data.getDescription());
            pstmt.setString(6, data.getCityName());

            pstmt.executeUpdate();
        }catch (Exception e){
            System.out.println("Error" + e.getMessage());
        }

    }
}
