package reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Cancellation {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Cancel Reservation");

        frame.setSize(650, 600);
        frame.setLayout(new BorderLayout(15, 15));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // PNR
        JLabel pnrLabel = new JLabel("Enter PNR:");
        JTextField pnrField = new JTextField();

        JButton fetchButton = new JButton("Fetch");

        // Booking details
        JLabel passengerLabel = new JLabel("Passenger Name:");
        JLabel passengerValue = new JLabel("");

        JLabel trainLabel = new JLabel("Train Number:");
        JLabel trainValue = new JLabel("");

        JLabel classLabel = new JLabel("Class Type:");
        JLabel classValue = new JLabel("");

        JLabel dateLabel = new JLabel("Journey Date:");
        JLabel dateValue = new JLabel("");

        JLabel sourceLabel = new JLabel("Source:");
        JLabel sourceValue = new JLabel("");

        JLabel destinationLabel = new JLabel("Destination:");
        JLabel destinationValue = new JLabel("");

        JButton cancelButton = new JButton("Cancel Booking");
        JButton backButton = new JButton("Back to Reservation");
        cancelButton.setEnabled(false);


        // Fetch Booking
        fetchButton.addActionListener(e -> {

            String pnr = pnrField.getText();

            if (pnr.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter PNR!"
                );
                return;
            }

            String url = "jdbc:sqlite:reservation.db";

            String sql = "SELECT * FROM bookings WHERE pnr = ?";

            try {

                Connection con = DriverManager.getConnection(url);

                PreparedStatement pst = con.prepareStatement(sql);

                pst.setInt(1, Integer.parseInt(pnr));

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    passengerValue.setText(
                            rs.getString("passenger_name")
                    );

                    trainValue.setText(
                            rs.getString("train_number")
                    );

                    classValue.setText(
                            rs.getString("class_type")
                    );

                    dateValue.setText(
                            rs.getString("journey_date")
                    );

                    sourceValue.setText(
                            rs.getString("source")
                    );

                    destinationValue.setText(
                            rs.getString("destination")
                    );

                    cancelButton.setEnabled(true);

                } else {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Booking Not Found!"
                    );

                    cancelButton.setEnabled(false);
                }

                con.close();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Invalid PNR!"
                );
            }
        });
        
        backButton.addActionListener(e -> {
            frame.dispose();
            Reservation.main(null);
        });


        // Cancel Booking
        cancelButton.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to cancel this booking?",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                String pnr = pnrField.getText();

                String url = "jdbc:sqlite:reservation.db";

                String sql = "DELETE FROM bookings WHERE pnr = ?";

                try {

                    Connection con = DriverManager.getConnection(url);

                    PreparedStatement pst =
                            con.prepareStatement(sql);

                    pst.setInt(1, Integer.parseInt(pnr));

                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(
                            frame,
                            "Booking Cancelled Successfully!"
                    );

                    // Clear details
                    passengerValue.setText("");
                    trainValue.setText("");
                    classValue.setText("");
                    dateValue.setText("");
                    sourceValue.setText("");
                    destinationValue.setText("");

                    cancelButton.setEnabled(false);

                    con.close();

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Cancellation Failed!"
                    );
                }
            }
        });


     // Main Panel
        JPanel mainPanel = new JPanel(new GridLayout(8, 2, 15, 15));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 40, 10, 40)
        );

        // PNR section
        mainPanel.add(pnrLabel);
        mainPanel.add(pnrField);

        mainPanel.add(new JLabel(""));
        mainPanel.add(fetchButton);

        // Booking details
        mainPanel.add(passengerLabel);
        mainPanel.add(passengerValue);

        mainPanel.add(trainLabel);
        mainPanel.add(trainValue);

        mainPanel.add(classLabel);
        mainPanel.add(classValue);

        mainPanel.add(dateLabel);
        mainPanel.add(dateValue);

        mainPanel.add(sourceLabel);
        mainPanel.add(sourceValue);

        mainPanel.add(destinationLabel);
        mainPanel.add(destinationValue);


        // Title
        JLabel titleLabel = new JLabel(
                "CANCEL RESERVATION",
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
                new FlowLayout(FlowLayout.CENTER, 20, 15)
        );

        buttonPanel.add(cancelButton);
        buttonPanel.add(backButton);


        // Add panels
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
