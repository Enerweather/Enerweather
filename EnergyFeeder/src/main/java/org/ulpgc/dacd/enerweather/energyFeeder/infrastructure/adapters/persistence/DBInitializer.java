package org.ulpgc.dacd.enerweather.energyFeeder.infrastructure.adapters.persistence;

import java.sql.Connection;
import java.sql.Statement;

public class DBInitializer {
    public static void createEnergyTable(){
        String sql = """
                CREATE TABLE IF NOT EXISTS re_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                indicator TEXT,
                value REAL,
                timestamp TEXT);
                """;
        try(Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
    public static void clearEnergyTable() {
        String sql = "DELETE FROM re_data";
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Error clearing RE table: " + e.getMessage());
        }
    }

}
