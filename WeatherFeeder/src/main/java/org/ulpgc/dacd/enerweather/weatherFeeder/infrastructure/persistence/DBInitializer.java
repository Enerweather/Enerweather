package org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.persistence;

import java.sql.Connection;
import java.sql.Statement;

public class DBInitializer {
    public static void createWeatherTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS weather_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                temperature REAL,
                humidity INTEGER,
                pressure INTEGER,
                wind_speed REAL,
                description TEXT,
                city_name TEXT,
                timestamp TEXT
                );""";


        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
        }
    }
    public static void clearWeatherTable() {
        String sql = "DELETE FROM weather_data";
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Error clearing weather table: " + e.getMessage());
        }
    }

}
