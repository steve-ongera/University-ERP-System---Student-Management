import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class UniversityERP {
    // Modern color palette
    public static final Color PRIMARY_COLOR = new Color(33, 150, 243);      // Blue
    public static final Color PRIMARY_DARK = new Color(25, 118, 210);       // Darker blue
    public static final Color ACCENT_COLOR = new Color(255, 193, 7);        // Amber
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);       // Green
    public static final Color ERROR_COLOR = new Color(244, 67, 54);         // Red
    public static final Color BACKGROUND_COLOR = new Color(250, 250, 250);  // Light gray
    public static final Color CARD_COLOR = Color.WHITE;
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117);
    public static final Color BORDER_COLOR = new Color(224, 224, 224);
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Set system look and feel
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
                    
                    // Force anti-aliasing for better text rendering
                    System.setProperty("awt.useSystemAAFontSettings","on");
                    System.setProperty("swing.aatext", "true");
                    
                    // Set UI Manager properties for better styling
                    UIManager.put("Button.focus", new Color(0, 0, 0, 0));
                    UIManager.put("TextField.selectionBackground", PRIMARY_COLOR);
                    UIManager.put("Table.selectionBackground", new Color(227, 242, 253));
                    UIManager.put("Table.selectionForeground", TEXT_PRIMARY);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new LoginFrame();
            }
        });
    }
}

// ================= ENHANCED UI COMPONENTS =================
class ModernButton extends JButton {
    private Color backgroundColor;
    private Color hoverColor;
    
    public ModernButton(String text, Color bgColor) {
        super(text);
        this.backgroundColor = bgColor;
        this.hoverColor = bgColor.brighter();
        
        setBackground(backgroundColor);
        setForeground(Color.WHITE);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(true);
        setOpaque(true);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(120, 35));
        
        // Force custom painting
        setUI(new javax.swing.plaf.basic.BasicButtonUI());
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(backgroundColor);
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(backgroundColor.darker());
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (contains(e.getPoint())) {
                    setBackground(hoverColor);
                } else {
                    setBackground(backgroundColor);
                }
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Paint rounded rectangle background
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        
        // Paint text
        g2d.setColor(getForeground());
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2d.drawString(getText(), x, y);
        
        g2d.dispose();
    }
}

class ModernTextField extends JTextField {
    private boolean hasFocus = false;
    
    public ModernTextField(int columns) {
        super(columns);
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, 35));
        
        // Remove default border and set custom one
        setBorder(null);
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                hasFocus = true;
                repaint();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                hasFocus = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Paint background
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
        
        // Paint border
        if (hasFocus) {
            g2d.setColor(UniversityERP.PRIMARY_COLOR);
            g2d.setStroke(new BasicStroke(2));
        } else {
            g2d.setColor(UniversityERP.BORDER_COLOR);
            g2d.setStroke(new BasicStroke(1));
        }
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
        
        g2d.dispose();
        
        // Paint text with margin
        Graphics textGraphics = g.create();
        textGraphics.translate(12, 0);
        textGraphics.clipRect(0, 0, getWidth() - 24, getHeight());
        super.paintComponent(textGraphics);
        textGraphics.dispose();
    }
    
    @Override
    public Insets getInsets() {
        return new Insets(8, 12, 8, 12);
    }
}

class ModernPasswordField extends JPasswordField {
    private boolean hasFocus = false;
    
    public ModernPasswordField(int columns) {
        super(columns);
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, 35));
        
        // Remove default border
        setBorder(null);
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                hasFocus = true;
                repaint();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                hasFocus = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Paint background
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
        
        // Paint border
        if (hasFocus) {
            g2d.setColor(UniversityERP.PRIMARY_COLOR);
            g2d.setStroke(new BasicStroke(2));
        } else {
            g2d.setColor(UniversityERP.BORDER_COLOR);
            g2d.setStroke(new BasicStroke(1));
        }
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
        
        g2d.dispose();
        
        // Paint text with margin
        Graphics textGraphics = g.create();
        textGraphics.translate(12, 0);
        textGraphics.clipRect(0, 0, getWidth() - 24, getHeight());
        super.paintComponent(textGraphics);
        textGraphics.dispose();
    }
    
    @Override
    public Insets getInsets() {
        return new Insets(8, 12, 8, 12);
    }
}

// ================= DATABASE CONNECTION CLASS =================
class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/university_erp";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "cp7kvt";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
    
    public static void createDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS university_erp");
            System.out.println("Database created successfully");
            
        } catch (Exception e) {
            System.out.println("Error creating database: " + e.getMessage());
        }
        
        createTable();
    }
    
    public static void createTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS students (
                id INT AUTO_INCREMENT PRIMARY KEY,
                student_id VARCHAR(20) UNIQUE NOT NULL,
                first_name VARCHAR(50) NOT NULL,
                last_name VARCHAR(50) NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                phone VARCHAR(15),
                date_of_birth DATE,
                gender ENUM('Male', 'Female', 'Other'),
                department VARCHAR(50),
                year_of_study INT,
                gpa DECIMAL(3,2),
                address TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(createTableSQL);
            System.out.println("Students table created successfully");
            
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }
}

// ================= STUDENT MODEL CLASS =================
class Student {
    private int id;
    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String gender;
    private String department;
    private int yearOfStudy;
    private double gpa;
    private String address;
    
    public Student(String studentId, String firstName, String lastName, String email,
                  String phone, String dateOfBirth, String gender, String department,
                  int yearOfStudy, double gpa, String address) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.gpa = gpa;
        this.address = address;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }
    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}

// ================= MODERN LOGIN FRAME =================
class LoginFrame extends JFrame {
    private ModernTextField usernameField;
    private ModernPasswordField passwordField;
    
    public LoginFrame() {
        setTitle("University ERP - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        DatabaseConnection.createDatabase();
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, UniversityERP.PRIMARY_COLOR,
                    0, getHeight(), UniversityERP.PRIMARY_DARK
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        // Login card
        JPanel loginCard = new JPanel();
        loginCard.setBackground(UniversityERP.CARD_COLOR);
        loginCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UniversityERP.BORDER_COLOR, 1),
            new EmptyBorder(40, 40, 40, 40)
        ));
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        
        // Logo and title
        JLabel titleLabel = new JLabel("University ERP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(UniversityERP.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Student Management System", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(UniversityERP.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        
        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        usernameLabel.setForeground(UniversityERP.TEXT_PRIMARY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        usernameField = new ModernTextField(20);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, usernameField.getPreferredSize().height));
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(UniversityERP.TEXT_PRIMARY);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordField = new ModernPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height));
        
        // Add enter key listener
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        
        ModernButton loginButton = new ModernButton("Login", UniversityERP.PRIMARY_COLOR);
        ModernButton exitButton = new ModernButton("Exit", UniversityERP.TEXT_SECONDARY);
        
        loginButton.addActionListener(e -> login());
        exitButton.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);
        
        // Add components to form
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(25));
        formPanel.add(buttonPanel);
        
        // Add components to card
        loginCard.add(titleLabel);
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(subtitleLabel);
        loginCard.add(Box.createVerticalStrut(30));
        loginCard.add(formPanel);
        
        mainPanel.add(loginCard);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.equals("admin") && password.equals("admin123")) {
            dispose();
            new MainFrame();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid credentials! Use admin/admin123", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
}

// ================= MODERN MAIN FRAME =================
class MainFrame extends JFrame {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private ModernTextField searchField;
    private JLabel totalStudentsLabel, avgGpaLabel;
    
    public MainFrame() {
        setTitle("University ERP - Student Management Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        
        initializeUI();
        loadStudentData();
        updateStatistics();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(UniversityERP.BACKGROUND_COLOR);
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setOpaque(false);
        
        // Statistics panel
        JPanel statsPanel = createStatisticsPanel();
        mainPanel.add(statsPanel, BorderLayout.NORTH);
        
        // Table panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, UniversityERP.PRIMARY_COLOR,
                    getWidth(), 0, UniversityERP.PRIMARY_DARK
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        // Title
        JLabel titleLabel = new JLabel("Student Management Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(20, 30, 20, 20));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(new EmptyBorder(15, 20, 15, 30));
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        searchField = new ModernTextField(20);
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                searchStudents();
            }
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(searchField);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createStatisticsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Total Students Card
        totalStudentsLabel = new JLabel("0");
        JPanel totalCard = createStatCard("Total Students", totalStudentsLabel, UniversityERP.PRIMARY_COLOR);
        
        // Average GPA Card
        avgGpaLabel = new JLabel("0.00");
        JPanel avgGpaCard = createStatCard("Average GPA", avgGpaLabel, UniversityERP.SUCCESS_COLOR);
        
        // Departments Card
        JLabel deptLabel = new JLabel("9");
        JPanel deptCard = createStatCard("Departments", deptLabel, UniversityERP.ACCENT_COLOR);
        
        // Active Students Card (placeholder)
        JLabel activeLabel = new JLabel("100%");
        JPanel activeCard = createStatCard("Active Rate", activeLabel, UniversityERP.ERROR_COLOR);
        
        statsPanel.add(totalCard);
        statsPanel.add(avgGpaCard);
        statsPanel.add(deptCard);
        statsPanel.add(activeCard);
        
        return statsPanel;
    }
    
    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UniversityERP.CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UniversityERP.BORDER_COLOR, 1),
            new EmptyBorder(20, 15, 20, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(UniversityERP.TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());
        
        return card;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        
        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setOpaque(false);
        actionPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        ModernButton addButton = new ModernButton("Add Student", UniversityERP.SUCCESS_COLOR);
        ModernButton editButton = new ModernButton("Edit Student", UniversityERP.PRIMARY_COLOR);
        ModernButton deleteButton = new ModernButton("Delete Student", UniversityERP.ERROR_COLOR);
        ModernButton refreshButton = new ModernButton("Refresh", UniversityERP.TEXT_SECONDARY);
        ModernButton logoutButton = new ModernButton("Logout", UniversityERP.TEXT_SECONDARY);
        
        addButton.addActionListener(e -> showAddStudentDialog());
        editButton.addActionListener(e -> showEditStudentDialog());
        deleteButton.addActionListener(e -> deleteStudent());
        refreshButton.addActionListener(e -> { loadStudentData(); updateStatistics(); });
        logoutButton.addActionListener(e -> logout());
        
        actionPanel.add(addButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        actionPanel.add(Box.createHorizontalStrut(20));
        actionPanel.add(refreshButton);
        actionPanel.add(logoutButton);
        
        // Table
        String[] columns = {"ID", "Student ID", "Name", "Email", "Phone", 
                           "Birth Date", "Gender", "Department", "Year", "GPA"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studentTable.setRowHeight(30);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setSelectionBackground(new Color(227, 242, 253));
        studentTable.setSelectionForeground(UniversityERP.TEXT_PRIMARY);
        
        // Style table header
        JTableHeader header = studentTable.getTableHeader();
        header.setBackground(UniversityERP.PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(0, 35));
        
        // Style table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        studentTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        studentTable.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
        studentTable.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
        
        // Column widths
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UniversityERP.BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(actionPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private void loadStudentData() {
        tableModel.setRowCount(0);
        
        String sql = "SELECT * FROM students ORDER BY student_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("student_id"));
                row.add(rs.getString("first_name") + " " + rs.getString("last_name"));
                row.add(rs.getString("email"));
                row.add(rs.getString("phone"));
                row.add(rs.getDate("date_of_birth"));
                row.add(rs.getString("gender"));
                row.add(rs.getString("department"));
                row.add(rs.getInt("year_of_study"));
                row.add(String.format("%.2f", rs.getDouble("gpa")));
                
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateStatistics() {
        String countSQL = "SELECT COUNT(*) as total, AVG(gpa) as avg_gpa FROM students";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(countSQL)) {
            
            if (rs.next()) {
                totalStudentsLabel.setText(String.valueOf(rs.getInt("total")));
                avgGpaLabel.setText(String.format("%.2f", rs.getDouble("avg_gpa")));
            }
            
        } catch (SQLException e) {
            totalStudentsLabel.setText("Error");
            avgGpaLabel.setText("Error");
        }
    }
    
    private void searchStudents() {
        String searchTerm = searchField.getText().toLowerCase().trim();
        
        if (searchTerm.isEmpty()) {
            loadStudentData();
            return;
        }
        
        tableModel.setRowCount(0);
        
        String sql = """
            SELECT * FROM students 
            WHERE LOWER(student_id) LIKE ? OR LOWER(first_name) LIKE ? OR 
                  LOWER(last_name) LIKE ? OR LOWER(email) LIKE ? OR LOWER(department) LIKE ?
            ORDER BY student_id
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            for (int i = 1; i <= 5; i++) {
                pstmt.setString(i, searchPattern);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("student_id"));
                row.add(rs.getString("first_name") + " " + rs.getString("last_name"));
                row.add(rs.getString("email"));
                row.add(rs.getString("phone"));
                row.add(rs.getDate("date_of_birth"));
                row.add(rs.getString("gender"));
                row.add(rs.getString("department"));
                row.add(rs.getInt("year_of_study"));
                row.add(String.format("%.2f", rs.getDouble("gpa")));
                
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error searching data: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showAddStudentDialog() {
        new StudentDialog(this, "Add New Student", null);
    }
    
    private void showEditStudentDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to edit.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int studentId = (Integer) tableModel.getValueAt(selectedRow, 0);
        new StudentDialog(this, "Edit Student", getStudentById(studentId));
    }
    
    private Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Student student = new Student(
                    rs.getString("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getDate("date_of_birth").toString(),
                    rs.getString("gender"),
                    rs.getString("department"),
                    rs.getInt("year_of_study"),
                    rs.getDouble("gpa"),
                    rs.getString("address")
                );
                student.setId(rs.getInt("id"));
                return student;
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching student: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return null;
    }
    
    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this student?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            int studentId = (Integer) tableModel.getValueAt(selectedRow, 0);
            
            String sql = "DELETE FROM students WHERE id = ?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, studentId);
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully!", 
                                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadStudentData();
                    updateStatistics();
                }
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting student: " + e.getMessage(), 
                                            "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void logout() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame();
        }
    }
    
    public void refreshTable() {
        loadStudentData();
        updateStatistics();
    }
}

// ================= MODERN STUDENT DIALOG =================
class StudentDialog extends JDialog {
    private ModernTextField studentIdField, firstNameField, lastNameField, emailField, phoneField;
    private ModernTextField dateOfBirthField, addressField, gpaField;
    private JComboBox<String> genderCombo, departmentCombo, yearCombo;
    private MainFrame parentFrame;
    private Student student;
    private boolean isEdit;
    
    public StudentDialog(MainFrame parent, String title, Student student) {
        super(parent, title, true);
        this.parentFrame = parent;
        this.student = student;
        this.isEdit = (student != null);
        
        setSize(600, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        initializeUI();
        
        if (isEdit) {
            populateFields();
        }
        
        setVisible(true);
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(UniversityERP.BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, UniversityERP.PRIMARY_COLOR,
                    getWidth(), 0, UniversityERP.PRIMARY_DARK
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, 60));
        
        JLabel titleLabel = new JLabel(isEdit ? "Edit Student Information" : "Add New Student");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(20, 30, 20, 30));
        headerPanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        formPanel.setBackground(UniversityERP.CARD_COLOR);
        
        // Personal Information Section
        formPanel.add(createSectionHeader("Personal Information"));
        formPanel.add(Box.createVerticalStrut(15));
        
        // Row 1: Student ID and First Name
        JPanel row1 = new JPanel(new GridLayout(1, 2, 15, 0));
        row1.setOpaque(false);
        
        JPanel studentIdPanel = createFieldPanel("Student ID *", studentIdField = new ModernTextField(0));
        JPanel firstNamePanel = createFieldPanel("First Name *", firstNameField = new ModernTextField(0));
        
        row1.add(studentIdPanel);
        row1.add(firstNamePanel);
        formPanel.add(row1);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Row 2: Last Name and Email
        JPanel row2 = new JPanel(new GridLayout(1, 2, 15, 0));
        row2.setOpaque(false);
        
        JPanel lastNamePanel = createFieldPanel("Last Name *", lastNameField = new ModernTextField(0));
        JPanel emailPanel = createFieldPanel("Email *", emailField = new ModernTextField(0));
        
        row2.add(lastNamePanel);
        row2.add(emailPanel);
        formPanel.add(row2);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Row 3: Phone and Date of Birth
        JPanel row3 = new JPanel(new GridLayout(1, 2, 15, 0));
        row3.setOpaque(false);
        
        JPanel phonePanel = createFieldPanel("Phone", phoneField = new ModernTextField(0));
        JPanel dobPanel = createFieldPanel("Date of Birth (YYYY-MM-DD)", dateOfBirthField = new ModernTextField(0));
        
        row3.add(phonePanel);
        row3.add(dobPanel);
        formPanel.add(row3);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Row 4: Gender and Address
        JPanel row4 = new JPanel(new GridLayout(1, 2, 15, 0));
        row4.setOpaque(false);
        
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        styleComboBox(genderCombo);
        JPanel genderPanel = createFieldPanel("Gender", genderCombo);
        
        JPanel addressPanel = createFieldPanel("Address", addressField = new ModernTextField(0));
        
        row4.add(genderPanel);
        row4.add(addressPanel);
        formPanel.add(row4);
        formPanel.add(Box.createVerticalStrut(25));
        
        // Academic Information Section
        formPanel.add(createSectionHeader("Academic Information"));
        formPanel.add(Box.createVerticalStrut(15));
        
        // Row 5: Department and Year
        JPanel row5 = new JPanel(new GridLayout(1, 2, 15, 0));
        row5.setOpaque(false);
        
        departmentCombo = new JComboBox<>(new String[]{
            "Computer Science", "Engineering", "Business Administration", 
            "Medicine", "Arts", "Mathematics", "Physics", "Chemistry", "Biology"
        });
        departmentCombo.setEditable(true);
        styleComboBox(departmentCombo);
        JPanel deptPanel = createFieldPanel("Department", departmentCombo);
        
        yearCombo = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
        styleComboBox(yearCombo);
        JPanel yearPanel = createFieldPanel("Year of Study", yearCombo);
        
        row5.add(deptPanel);
        row5.add(yearPanel);
        formPanel.add(row5);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Row 6: GPA
        JPanel row6 = new JPanel(new GridLayout(1, 2, 15, 0));
        row6.setOpaque(false);
        
        JPanel gpaPanel = createFieldPanel("GPA (0.00 - 4.00)", gpaField = new ModernTextField(0));
        row6.add(gpaPanel);
        row6.add(new JPanel()); // Empty panel for spacing
        
        formPanel.add(row6);
        formPanel.add(Box.createVerticalStrut(30));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        ModernButton saveButton = new ModernButton(isEdit ? "Update Student" : "Save Student", 
                                                  UniversityERP.SUCCESS_COLOR);
        ModernButton cancelButton = new ModernButton("Cancel", UniversityERP.TEXT_SECONDARY);
        
        saveButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.setPreferredSize(new Dimension(150, 40));
        
        saveButton.addActionListener(e -> saveStudent());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel);
        
        // Scroll pane for form
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JLabel createSectionHeader(String text) {
        JLabel header = new JLabel(text);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setForeground(UniversityERP.PRIMARY_COLOR);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        return header;
    }
    
    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(UniversityERP.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (field instanceof ModernTextField) {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height));
        }
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        
        return panel;
    }
    
    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UniversityERP.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }
    
    private void populateFields() {
        studentIdField.setText(student.getStudentId());
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        emailField.setText(student.getEmail());
        phoneField.setText(student.getPhone());
        dateOfBirthField.setText(student.getDateOfBirth());
        genderCombo.setSelectedItem(student.getGender());
        departmentCombo.setSelectedItem(student.getDepartment());
        yearCombo.setSelectedItem(String.valueOf(student.getYearOfStudy()));
        gpaField.setText(String.valueOf(student.getGpa()));
        addressField.setText(student.getAddress());
    }
    
    private void saveStudent() {
        if (!validateFields()) {
            return;
        }
        
        try {
            if (isEdit) {
                updateStudent();
            } else {
                insertStudent();
            }
            
            parentFrame.refreshTable();
            dispose();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving student: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateFields() {
        // Check required fields
        if (studentIdField.getText().trim().isEmpty() ||
            firstNameField.getText().trim().isEmpty() ||
            lastNameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, "Please fill all required fields (marked with *)!", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validate email format
        String email = emailField.getText().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validate GPA
        try {
            double gpa = Double.parseDouble(gpaField.getText());
            if (gpa < 0.0 || gpa > 4.0) {
                JOptionPane.showMessageDialog(this, "GPA must be between 0.00 and 4.00!", 
                                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid GPA (e.g., 3.50)!", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validate date format
        if (!dateOfBirthField.getText().trim().isEmpty()) {
            try {
                Date.valueOf(dateOfBirthField.getText());
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Please enter date in YYYY-MM-DD format!", 
                                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        
        return true;
    }
    
    private void insertStudent() throws SQLException {
        String sql = """
            INSERT INTO students (student_id, first_name, last_name, email, phone, 
            date_of_birth, gender, department, year_of_study, gpa, address)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentIdField.getText().trim());
            pstmt.setString(2, firstNameField.getText().trim());
            pstmt.setString(3, lastNameField.getText().trim());
            pstmt.setString(4, emailField.getText().trim());
            pstmt.setString(5, phoneField.getText().trim());
            
            if (dateOfBirthField.getText().trim().isEmpty()) {
                pstmt.setNull(6, Types.DATE);
            } else {
                pstmt.setDate(6, Date.valueOf(dateOfBirthField.getText()));
            }
            
            pstmt.setString(7, (String) genderCombo.getSelectedItem());
            pstmt.setString(8, (String) departmentCombo.getSelectedItem());
            pstmt.setInt(9, Integer.parseInt((String) yearCombo.getSelectedItem()));
            pstmt.setDouble(10, Double.parseDouble(gpaField.getText()));
            pstmt.setString(11, addressField.getText().trim());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Student added successfully!", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void updateStudent() throws SQLException {
        String sql = """
            UPDATE students SET student_id = ?, first_name = ?, last_name = ?, email = ?, 
            phone = ?, date_of_birth = ?, gender = ?, department = ?, year_of_study = ?, 
            gpa = ?, address = ? WHERE id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentIdField.getText().trim());
            pstmt.setString(2, firstNameField.getText().trim());
            pstmt.setString(3, lastNameField.getText().trim());
            pstmt.setString(4, emailField.getText().trim());
            pstmt.setString(5, phoneField.getText().trim());
            
            if (dateOfBirthField.getText().trim().isEmpty()) {
                pstmt.setNull(6, Types.DATE);
            } else {
                pstmt.setDate(6, Date.valueOf(dateOfBirthField.getText()));
            }
            
            pstmt.setString(7, (String) genderCombo.getSelectedItem());
            pstmt.setString(8, (String) departmentCombo.getSelectedItem());
            pstmt.setInt(9, Integer.parseInt((String) yearCombo.getSelectedItem()));
            pstmt.setDouble(10, Double.parseDouble(gpaField.getText()));
            pstmt.setString(11, addressField.getText().trim());
            pstmt.setInt(12, student.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}