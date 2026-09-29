package reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BookingHistory {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Booking History");

        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(15, 15));

        // Title
        JLabel titleLabel = new JLabel(
                "BOOKING HISTORY",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 10, 10)
        );

        // Table columns
        String[] columns = {
                "PNR",
                "Passenger Name",
                "Train Number",
                "Class Type",
                "Journey Date",
                "Source",
                "Destination"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0);

        JTable table = new JTable(model);

        // Table settings
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        table.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        // Scroll pane
        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        );

        // Back button
        JButton backButton =
                new JButton("Back to Reservation");

        backButton.addActionListener(e -> {

            frame.dispose();

            Reservation.main(null);
        });

        // Bottom panel
        JPanel bottomPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        10,
                        15
                )
        );

        bottomPanel.add(backButton);

        // Add components
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Fetch bookings
        String url = "jdbc:sqlite:reservation.db";

        try {

            Connection con =
                    DriverManager.getConnection(url);

            Statement stmt =
                    con.createStatement();

            String sql =
                    "SELECT * FROM bookings";

            ResultSet rs =
                    stmt.executeQuery(sql);

            while (rs.next()) {

                model.addRow(new Object[]{

                        rs.getInt("pnr"),

                        rs.getString("passenger_name"),

                        rs.getInt("train_number"),

                        rs.getString("class_type"),

                        rs.getString("journey_date"),

                        rs.getString("source"),

                        rs.getString("destination")
                });
            }

            con.close();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Unable to load bookings!\n"
                            + ex.getMessage()
            );
        }

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}