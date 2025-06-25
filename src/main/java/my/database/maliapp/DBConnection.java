package my.database.maliapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/malidb";

    public static Connection connect(String username, String password) throws SQLException {
        return DriverManager.getConnection(DB_URL, username, password);
    }
}
