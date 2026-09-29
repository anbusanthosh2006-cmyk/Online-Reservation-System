package reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    public static void main(String[] args) {

        String url = "jdbc:sqlite:reservation.db";

        try {
            Connection con = DriverManager.getConnection(url);

            Statement stmt = con.createStatement();

            // Users table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL)"
            );

            // Trains table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS trains (" +
                "train_number INTEGER PRIMARY KEY," +
                "train_name TEXT NOT NULL)"
            );

            // Bookings table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS bookings (" +
                "pnr INTEGER PRIMARY KEY AUTOINCREMENT," +
                "passenger_name TEXT NOT NULL," +
                "train_number INTEGER NOT NULL," +
                "class_type TEXT NOT NULL," +
                "journey_date TEXT NOT NULL," +
                "source TEXT NOT NULL," +
                "destination TEXT NOT NULL)"
            );
            stmt.executeUpdate(
            	    "INSERT OR IGNORE INTO users (username, password) " +
            	    "VALUES ('admin', '1234')"
            	);
         // Add sample trains
            stmt.executeUpdate(
                "INSERT OR IGNORE INTO trains (train_number, train_name) " +
                "VALUES (12621, 'Tamil Nadu Express')"
            );

            stmt.executeUpdate(
                "INSERT OR IGNORE INTO trains (train_number, train_name) " +
                "VALUES (12627, 'Karnataka Express')"
            );

            stmt.executeUpdate(
                "INSERT OR IGNORE INTO trains (train_number, train_name) " +
                "VALUES (12007, 'Shatabdi Express')"
            );
            System.out.println("Database and Tables Created Successfully!");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}