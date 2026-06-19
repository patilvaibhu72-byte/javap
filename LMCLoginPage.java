import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LMCLoginPage extends JFrame implements ActionListener {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblStatus;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3307/lcmcdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "yourpassword";

    public LMCLoginPage() {
        setTitle("Latur Municipal Corporation - Leave Management System");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // Left panel (illustration / info)
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(230, 240, 255));
        leftPanel.setLayout(new BorderLayout());

        JLabel lblCity = new JLabel("<html><center><h2>Latur Municipal Corporation</h2>"
                + "<p>Leave Management System</p><br>"
                + "<img src='https://cdn-icons-png.flaticon.com/512/684/684908.png' width='120'><br>"
                + "<p>Efficient Leave Management for Municipal Employees</p></center></html>",
                SwingConstants.CENTER);
        leftPanel.add(lblCity, BorderLayout.CENTER);

        // Right panel (login form)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);

        JLabel lblTitle = new JLabel("Employee Login");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBounds(100, 50, 300, 30);
        rightPanel.add(lblTitle);

        JLabel lblEmail = new JLabel("Employee ID / Email:");
        lblEmail.setBounds(100, 120, 200, 25);
        rightPanel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(100, 145, 250, 30);
        rightPanel.add(txtEmail);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(100, 190, 200, 25);
        rightPanel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(100, 215, 250, 30);
        rightPanel.add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(100, 270, 250, 35);
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(this);
        rightPanel.add(btnLogin);

        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setBounds(100, 320, 250, 25);
        lblStatus.setForeground(Color.RED);
        rightPanel.add(lblStatus);

        add(leftPanel);
        add(rightPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Please fill all fields!");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            String query = "SELECT * FROM employees WHERE email=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                lblStatus.setForeground(new Color(0, 153, 0));
                lblStatus.setText("Login successful!");
                JOptionPane.showMessageDialog(this, "Welcome " + rs.getString("name"));
            } else {
                lblStatus.setForeground(Color.RED);
                lblStatus.setText("Invalid credentials!");
            }

            conn.close();
        } catch (Exception ex) {
            lblStatus.setText("Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LMCLoginPage().setVisible(true));
    }
}
