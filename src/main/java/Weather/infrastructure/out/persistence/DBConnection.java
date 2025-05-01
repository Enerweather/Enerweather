package Weather.infrastructure.out.persistence;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DBConnection {
    private static final String URL = "jdbc:sqlite:data.db";
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
