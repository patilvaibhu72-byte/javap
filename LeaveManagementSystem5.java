import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LeaveManagementSystem5 extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private JTextField empIdField;
    private JTextField empNameField;
    private JComboBox<String> leaveTypeBox;
    private JTextField fromDateField;
    private JTextField toDateField;
    private JTextField durationField;
    private JTextArea reasonArea;

    private JTable pendingTable;
    private DefaultTableModel tableModel;

    String URL = "jdbc:mysql://localhost:3306/leave_management";
    String USER = "root";
    String PASSWORD = "";

    public LeaveManagementSystem5() {

        setTitle("Leave Management System");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(70, 110, 255));
        sidebar.setPreferredSize(new Dimension(180, 600));
        sidebar.setLayout(new GridLayout(8, 1, 5, 10));

        JLabel title = new JLabel("LEAVE MANAGEMENT  ", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        
       

        JButton dashboardBtn = new JButton("Dashboard");
        JButton applyBtn = new JButton("Apply Leave");
        JButton pendingBtn = new JButton("Pending Leave");
        JButton logoutBtn = new JButton("Logout");

        sidebar.add(title);
        sidebar.add(dashboardBtn);
        sidebar.add(applyBtn);
        sidebar.add(pendingBtn);
        sidebar.add(logoutBtn);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        JPanel dashboardPanel = createDashboardPanel();
        JPanel applyPanel = createApplyPanel();
        JPanel pendingPanel = createPendingPanel();

        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(applyPanel, "apply");
        contentPanel.add(pendingPanel, "pending");

        dashboardBtn.addActionListener(e ->
                cardLayout.show(contentPanel, "dashboard"));

        applyBtn.addActionListener(e ->
                cardLayout.show(contentPanel, "apply"));

        pendingBtn.addActionListener(e -> {
            loadPendingLeaves();
            cardLayout.show(contentPanel, "pending");
        });

        logoutBtn.addActionListener(e -> System.exit(0));

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);

        createTableIfNotExists();
    }

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private void createTableIfNotExists() {

        try (Connection con = getConnection()) {

            Statement st = con.createStatement();

            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS leave_requests (" +
                            "request_id INT AUTO_INCREMENT PRIMARY KEY," +
                            "employee_id VARCHAR(20)," +
                            "employee_name VARCHAR(100)," +
                            "leave_type VARCHAR(50)," +
                            "from_date DATE," +
                            "to_date DATE," +
                            "duration INT," +
                            "reason VARCHAR(255)," +
                            "status VARCHAR(20) DEFAULT 'PENDING'" +
                            ")"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createDashboardPanel() {

    JPanel panel = new JPanel();
    panel.setLayout(null);

    JLabel companyLabel = new JLabel("LCMC Latur");
    companyLabel.setBounds(50, 20, 700, 40);
    companyLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    companyLabel.setFont(new Font("Arial", Font.BOLD, 22));


   
    JLabel dashboardLabel = new JLabel("Dashboard");
    dashboardLabel.setBounds(50, 100, 250, 40);
    dashboardLabel.setFont(new Font("Arial", Font.BOLD, 32));

    JButton applyLeaveBtn = new JButton("Apply Leave");
    applyLeaveBtn.setBounds(50, 180, 180, 50);
    applyLeaveBtn.setFont(new Font("Arial", Font.PLAIN, 20));

    JButton pendingLeaveBtn = new JButton("Pending Leave");
    pendingLeaveBtn.setBounds(300, 180, 180, 50);
    pendingLeaveBtn.setFont(new Font("Arial", Font.PLAIN, 20));

    // Apply Leave Button Action
    applyLeaveBtn.addActionListener(e -> {
        cardLayout.show(contentPanel, "apply");
    });

    // Pending Leave Button Action
    pendingLeaveBtn.addActionListener(e -> {
        loadPendingLeaves();
        cardLayout.show(contentPanel, "pending");
    });

    JLabel footer = new JLabel(
            "© Municipal Corporation Leave Management System",
            SwingConstants.CENTER);

    footer.setForeground(Color.RED);
    footer.setBounds(150, 500, 500, 20);

    panel.add(companyLabel);
    panel.add(dashboardLabel);
    panel.add(applyLeaveBtn);
    panel.add(pendingLeaveBtn);
    panel.add(footer);

    return panel;
}

   private JPanel createApplyPanel() {

    JPanel panel = new JPanel();
    panel.setLayout(null);

    JLabel lbl1 = new JLabel("Employee ID");
    lbl1.setBounds(50, 50, 150, 30);
    lbl1.setFont(new Font("Arial", Font.BOLD, 18));

    empIdField = new JTextField();
    empIdField.setBounds(220, 50, 250, 35);
    empIdField.setFont(new Font("Arial", Font.PLAIN, 18));

    JLabel lbl2 = new JLabel("Employee Name");
    lbl2.setBounds(50, 100, 150, 30);
    lbl2.setFont(new Font("Arial", Font.BOLD, 18));

    empNameField = new JTextField();
    empNameField.setBounds(220, 100, 250, 35);
    empNameField.setFont(new Font("Arial", Font.PLAIN, 18));

    JLabel lbl3 = new JLabel("Leave Type");
    lbl3.setBounds(50, 150, 150, 30);
    lbl3.setFont(new Font("Arial", Font.BOLD, 18));

    leaveTypeBox = new JComboBox<>(new String[]{
            "Casual Leave",
            "Sick Leave",
            "Earned Leave",
            "Medical Leave",
            "Emergency Leave",
            "Maternity Leave"
    });

    leaveTypeBox.setBounds(220, 150, 250, 35);
    leaveTypeBox.setFont(new Font("Arial", Font.PLAIN, 18));

    JLabel lbl4 = new JLabel("From Date");
    lbl4.setBounds(50, 200, 150, 30);
    lbl4.setFont(new Font("Arial", Font.BOLD, 18));

    fromDateField = new JTextField("2025-01-01");
    fromDateField.setBounds(220, 200, 250, 35);
    fromDateField.setFont(new Font("Arial", Font.PLAIN, 18));

    JLabel lbl5 = new JLabel("To Date");
    lbl5.setBounds(50, 250, 150, 30);
    lbl5.setFont(new Font("Arial", Font.BOLD, 18));

    toDateField = new JTextField("2025-01-02");
    toDateField.setBounds(220, 250, 250, 35);
    toDateField.setFont(new Font("Arial", Font.PLAIN, 18));

    JLabel lbl6 = new JLabel("Duration");
    lbl6.setBounds(50, 300, 150, 30);
    lbl6.setFont(new Font("Arial", Font.BOLD, 18));

    durationField = new JTextField();
    durationField.setBounds(220, 300, 250, 35);
    durationField.setFont(new Font("Arial", Font.PLAIN, 18));

    JLabel lbl7 = new JLabel("Reason");
    lbl7.setBounds(50, 350, 150, 30);
    lbl7.setFont(new Font("Arial", Font.BOLD, 18));

    reasonArea = new JTextArea();
    reasonArea.setFont(new Font("Arial", Font.PLAIN, 18));

    JScrollPane reasonScroll = new JScrollPane(reasonArea);
    reasonScroll.setBounds(220, 350, 300, 100);

    JButton submitBtn = new JButton("Submit Request");
    submitBtn.setBounds(220, 480, 180, 45);
    submitBtn.setFont(new Font("Arial", Font.BOLD, 18));

    JButton resetBtn = new JButton("Reset");
    resetBtn.setBounds(420, 480, 120, 45);
    resetBtn.setFont(new Font("Arial", Font.BOLD, 18));

    submitBtn.addActionListener(e -> submitLeave());

    resetBtn.addActionListener(e -> {
        empIdField.setText("");
        empNameField.setText("");
        durationField.setText("");
        reasonArea.setText("");
        leaveTypeBox.setSelectedIndex(0);
    });

    panel.add(lbl1);
    panel.add(empIdField);

    panel.add(lbl2);
    panel.add(empNameField);

    panel.add(lbl3);
    panel.add(leaveTypeBox);

    panel.add(lbl4);
    panel.add(fromDateField);

    panel.add(lbl5);
    panel.add(toDateField);

    panel.add(lbl6);
    panel.add(durationField);

    panel.add(lbl7);
    panel.add(reasonScroll);

    panel.add(submitBtn);
    panel.add(resetBtn);

    return panel;
}

       

    private JPanel createPendingPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        String[] cols = {
                "Emp ID",
                " Emp Name",
                "Leave Type",
                "Duration",
                "From",
                "To",
                "Reason",
                "Status"
        };

        tableModel = new DefaultTableModel(cols, 0);

        pendingTable = new JTable(tableModel);

        JScrollPane scroll =
                new JScrollPane(pendingTable);

        JPanel buttonPanel = new JPanel();
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void submitLeave() {

        try {

            Connection con = getConnection();

            String sql =
                    "INSERT INTO leave_requests " +
                            "(employee_id,employee_name,leave_type,from_date,to_date,duration,reason) " +
                            "VALUES(?,?,?,?,?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, empIdField.getText());
            ps.setString(2, empNameField.getText());
            ps.setString(3, leaveTypeBox.getSelectedItem().toString());
            ps.setString(4, fromDateField.getText());
            ps.setString(5, toDateField.getText());
            ps.setInt(6, Integer.parseInt(durationField.getText()));
            ps.setString(7, reasonArea.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Leave Request Submitted Successfully");

            loadPendingLeaves();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage());

        }
    }

    private void loadPendingLeaves() {

        tableModel.setRowCount(0);

        try {

            Connection con = getConnection();

            Statement st = con.createStatement();

            ResultSet rs =
                    st.executeQuery(
                            "SELECT * FROM leave_requests");

            while (rs.next()) {

                tableModel.addRow(new Object[]{
                        rs.getInt("request_id"),
                        rs.getString("employee_id"),
                        rs.getString("employee_name"),
                        rs.getString("leave_type"),
                        rs.getInt("duration"),
                        rs.getDate("from_date"),
                        rs.getDate("to_date"),
                        rs.getString("reason"),
                        rs.getString("status")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStatus(String status) {

        int row = pendingTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Select a leave request first");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);

        try {

            Connection con = getConnection();

            PreparedStatement ps =
                    con.prepareStatement(
                            "UPDATE leave_requests SET status=? WHERE request_id=?");

            ps.setString(1, status);
            ps.setInt(2, id);

            ps.executeUpdate();

            loadPendingLeaves();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LeaveManagementSystem5().setVisible(true);
        });
    }
}