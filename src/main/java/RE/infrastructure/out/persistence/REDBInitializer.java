package RE.infrastructure.out.persistence;

import Weather.infrastructure.out.persistence.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
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
}
