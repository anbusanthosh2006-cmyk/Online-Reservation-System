package reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Online Reservation System");

        frame.setSize(500, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel(
                "ONLINE RESERVATION SYSTEM",
                SwingConstants.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Login");

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.ipady = 8;
        panel.add(loginButton, gbc);

        // Login action
        loginButton.addActionListener(e -> {

            String username = usernameField.getText().trim();
            String password =
                    new String(passwordField.getPassword());

            // Empty field validation
            if (username.isEmpty() || password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter username and password!"
                );

                return;
            }

            String url = "jdbc:sqlite:reservation.db";

            String sql =
                    "SELECT * FROM users " +
                    "WHERE username = ? AND password = ?";

            try {

                Connection con =
                        DriverManager.getConnection(url);

                PreparedStatement pst =
                        con.prepareStatement(sql);

                pst.setString(1, username);
                pst.setString(2, password);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Login Successful!"
                    );

                    frame.dispose();

                    Reservation.main(null);

                } else {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Invalid Username or Password!"
                    );
                }

                con.close();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Database Error!\n" + ex.getMessage()
                );
            }
        });

        frame.add(panel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}