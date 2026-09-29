package reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Reservation {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Train Reservation");
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout(15, 15));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel passengerLabel = new JLabel("Passenger Name:");
        JTextField passengerField = new JTextField();

        JLabel trainNumberLabel = new JLabel("Train Number:");
        JTextField trainNumberField = new JTextField();

        JLabel trainNameLabel = new JLabel("Train Name:");
        JTextField trainNameField = new JTextField();
        trainNameField.setEditable(false);

        JLabel classLabel = new JLabel("Class Type:");
        JComboBox<String> classBox = new JComboBox<>(
                new String[]{"AC", "Sleeper", "Second Sitting"}
        );

        JLabel dateLabel = new JLabel("Journey Date:");
        JTextField dateField = new JTextField();

        JLabel sourceLabel = new JLabel("Source:");
        JTextField sourceField = new JTextField();

        JLabel destinationLabel = new JLabel("Destination:");
        JTextField destinationField = new JTextField();

        JButton bookButton = new JButton("Book Ticket");
        JButton cancelButton = new JButton("Cancel Reservation");
        JButton logoutButton = new JButton("Logout");
        JButton viewButton = new JButton("View Bookings");


        // Train Number -> Train Name
        trainNumberField.addActionListener(e -> {

            String trainNumber = trainNumberField.getText();

            String url = "jdbc:sqlite:reservation.db";

            String sql = "SELECT train_name FROM trains WHERE train_number = ?";

            try {

                Connection con = DriverManager.getConnection(url);

                PreparedStatement pst = con.prepareStatement(sql);

                pst.setInt(1, Integer.parseInt(trainNumber));

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    trainNameField.setText(
                            rs.getString("train_name")
                    );

                } else {

                    trainNameField.setText("invalid Train Number");
                }

                con.close();

            } catch (Exception ex) {

                trainNameField.setText("Invalid Train Number");
            }
        });


        // Book Ticket
        bookButton.addActionListener(e -> {
        	

            String passengerName = passengerField.getText();
            String trainNumber = trainNumberField.getText();
            String classType = classBox.getSelectedItem().toString();
            String journeyDate = dateField.getText();
            String source = sourceField.getText();
            String destination = destinationField.getText();
             


            // Validation
            if (passengerName.isEmpty() ||
                trainNumber.isEmpty() ||
                journeyDate.isEmpty() ||
                source.isEmpty() ||
                destination.isEmpty()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Please fill all required fields!"
                );

                return;
            }
            
            try {
                Integer.parseInt(trainNumber);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Train Number must contain numbers only!"
                );
                return;
            }
            
            try {
                java.time.LocalDate date =
                        java.time.LocalDate.parse(
                                journeyDate,
                                java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        );

                if (date.isBefore(java.time.LocalDate.now())) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Journey date cannot be in the past!"
                    );
                    return;
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Invalid date! Use DD-MM-YYYY"
                );
                return;
            }

            String url = "jdbc:sqlite:reservation.db";

            String sql = "INSERT INTO bookings " +
                    "(passenger_name, train_number, class_type, " +
                    "journey_date, source, destination) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";


            try {

                Connection con = DriverManager.getConnection(url);

                PreparedStatement pst = con.prepareStatement(
                        sql,
                        java.sql.Statement.RETURN_GENERATED_KEYS
                );

                pst.setString(1, passengerName);
                pst.setInt(2, Integer.parseInt(trainNumber));
                pst.setString(3, classType);
                pst.setString(4, journeyDate);
                pst.setString(5, source);
                pst.setString(6, destination);

                pst.executeUpdate();


                // Get generated PNR
                ResultSet rs = pst.getGeneratedKeys();

                if (rs.next()) {

                    int pnr = rs.getInt(1);

                    JOptionPane.showMessageDialog(
                            frame,
                            "Booking Successful!\n\n" +
                            "Your PNR: " + pnr
                    );
                }
                passengerField.setText("");
                trainNumberField.setText("");
                trainNameField.setText("");
                dateField.setText("");
                sourceField.setText("");
                destinationField.setText("");
                classBox.setSelectedIndex(0);

                con.close();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Booking Failed!\n" + ex.getMessage()
                );
            }
        });
     // Cancel Reservation
        cancelButton.addActionListener(e -> {
            frame.dispose();
            Cancellation.main(null);
        });
        
        logoutButton.addActionListener(e -> {
            frame.dispose();
            Login.main(null);
        });
        
        viewButton.addActionListener(e -> {
            frame.dispose();
            BookingHistory.main(null);
        });
        

     // Main Panel
        JPanel mainPanel = new JPanel(new GridLayout(7, 2, 15, 15));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 40, 15, 40)
        );

        // Add form fields
        mainPanel.add(passengerLabel);
        mainPanel.add(passengerField);

        mainPanel.add(trainNumberLabel);
        mainPanel.add(trainNumberField);

        mainPanel.add(trainNameLabel);
        mainPanel.add(trainNameField);

        mainPanel.add(classLabel);
        mainPanel.add(classBox);

        mainPanel.add(dateLabel);
        mainPanel.add(dateField);

        mainPanel.add(sourceLabel);
        mainPanel.add(sourceField);

        mainPanel.add(destinationLabel);
        mainPanel.add(destinationField);


        // Title
        JLabel titleLabel = new JLabel(
                "ONLINE TRAIN RESERVATION",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 10, 10)
        );


        // Button Panel
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 15)
        );

        buttonPanel.add(bookButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(logoutButton);


        // Add panels to frame
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}