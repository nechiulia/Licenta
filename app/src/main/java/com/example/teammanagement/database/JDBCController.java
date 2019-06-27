package com.example.teammanagement.database;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCController implements JConstants {

    private static JDBCController instance = null;
    private static Connection connection = null;

    private JDBCController() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static Connection getConnection() {
        return connection;
    }

    public static synchronized JDBCController getInstance(){
        if(instance == null){
            instance= new JDBCController();
        }
        return instance;
    }

    public Connection openConnection() {
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
