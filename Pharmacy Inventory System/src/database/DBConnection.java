package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {

        Connection con = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/pharmacy_db",
                    "root",
                    ""   // password (leave empty if none)
            );

            System.out.println("Connected Successfully!");

        } catch (Exception e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }

        return con;
    }
}