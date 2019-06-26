package com.example.teammanagement.database;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCController implements JConstants {

    private static JDBCController jdbcController;
    private static Connection connection = null;

    public JDBCController() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static synchronized Connection openConnection() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(JConstants.CONNECTION_URL);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return connection;
    }
}
