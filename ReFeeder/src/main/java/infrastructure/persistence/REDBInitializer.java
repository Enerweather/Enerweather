package infrastructure.persistence;

import infrastructure.persistence.DBConnection;

import java.sql.Connection;
import java.sql.Statement;

public class REDBInitializer {
    public static void createRETable (){
        String sql = """
                CREATE TABLE IF NOT EXISTS re_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                indicator TEXT,
                value REAL,
                percentage REAL,
                unit TEXT,
                timestamp TEXT,
                geo_name TEXT,
                geo_id INTEGER);
                """;
        try(Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
    public static void clearRETable() {
        String sql = "DELETE FROM re_data";
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Error clearing RE table: " + e.getMessage());
        }
    }

}
