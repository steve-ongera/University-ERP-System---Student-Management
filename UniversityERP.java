import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class UniversityERP {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame();
        });
    }
}

// ================= DATABASE CONNECTION CLASS =================
class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/university_erp";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password"; // Change this to your MySQL password
    
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
            
            // Create database if not exists
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS university_erp");
            System.out.println("Database created successfully");
            
        } catch (Exception e) {
            System.out.println("Error creating database: " + e.getMessage());
        }
        
        // Create table
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
    
    // Constructor
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

// ================= LOGIN FRAME =================
class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginFrame() {
        setTitle("University ERP - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        // Initialize database
        DatabaseConnection.createDatabase();
        
        initializeUI();
    }
    
    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("University ERP System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Login form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit");
        
        loginButton.addActionListener(e -> login());
        exitButton.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        // Simple authentication (in real app, use database)
        if (username.equals("admin") && password.equals("admin123")) {
            dispose();
            new MainFrame();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// ================= MAIN APPLICATION FRAME =================
class MainFrame extends JFrame {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public MainFrame() {
        setTitle("University ERP - Student Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        initializeUI();
        loadStudentData();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Top Panel - Search and Actions
        JPanel topPanel = new JPanel(new FlowLayout());
        
        topPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                searchStudents();
            }
        });
        topPanel.add(searchField);
        
        JButton addButton = new JButton("Add Student");
        JButton editButton = new JButton("Edit Student");
        JButton deleteButton = new JButton("Delete Student");
        JButton refreshButton = new JButton("Refresh");
        JButton logoutButton = new JButton("Logout");
        
        addButton.addActionListener(e -> showAddStudentDialog());
        editButton.addActionListener(e -> showEditStudentDialog());
        deleteButton.addActionListener(e -> deleteStudent());
        refreshButton.addActionListener(e -> loadStudentData());
        logoutButton.addActionListener(e -> logout());
        
        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);
        topPanel.add(refreshButton);
        topPanel.add(logoutButton);
        
        // Table
        String[] columns = {"ID", "Student ID", "First Name", "Last Name", "Email", "Phone", 
                           "Date of Birth", "Gender", "Department", "Year", "GPA"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        setVisible(true);
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
                row.add(rs.getString("first_name"));
                row.add(rs.getString("last_name"));
                row.add(rs.getString("email"));
                row.add(rs.getString("phone"));
                row.add(rs.getDate("date_of_birth"));
                row.add(rs.getString("gender"));
                row.add(rs.getString("department"));
                row.add(rs.getInt("year_of_study"));
                row.add(rs.getDouble("gpa"));
                
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchStudents() {
        String searchTerm = searchField.getText().toLowerCase();
        
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
                row.add(rs.getString("first_name"));
                row.add(rs.getString("last_name"));
                row.add(rs.getString("email"));
                row.add(rs.getString("phone"));
                row.add(rs.getDate("date_of_birth"));
                row.add(rs.getString("gender"));
                row.add(rs.getString("department"));
                row.add(rs.getInt("year_of_study"));
                row.add(rs.getDouble("gpa"));
                
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error searching data: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showAddStudentDialog() {
        new StudentDialog(this, "Add Student", null);
    }
    
    private void showEditStudentDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this student?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            int studentId = (Integer) tableModel.getValueAt(selectedRow, 0);
            
            String sql = "DELETE FROM students WHERE id = ?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, studentId);
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                    loadStudentData();
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
    }
}

// ================= STUDENT DIALOG =================
class StudentDialog extends JDialog {
    private JTextField studentIdField, firstNameField, lastNameField, emailField, phoneField;
    private JTextField dateOfBirthField, addressField, gpaField;
    private JComboBox<String> genderCombo, departmentCombo, yearCombo;
    private MainFrame parentFrame;
    private Student student;
    private boolean isEdit;
    
    public StudentDialog(MainFrame parent, String title, Student student) {
        super(parent, title, true);
        this.parentFrame = parent;
        this.student = student;
        this.isEdit = (student != null);
        
        setSize(500, 600);
        setLocationRelativeTo(parent);
        
        initializeUI();
        
        if (isEdit) {
            populateFields();
        }
        
        setVisible(true);
    }
    
    private void initializeUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Student ID
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        studentIdField = new JTextField(20);
        mainPanel.add(studentIdField, gbc);
        
        // First Name
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        firstNameField = new JTextField(20);
        mainPanel.add(firstNameField, gbc);
        
        // Last Name
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        lastNameField = new JTextField(20);
        mainPanel.add(lastNameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        mainPanel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        mainPanel.add(phoneField, gbc);
        
        // Date of Birth
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        dateOfBirthField = new JTextField(20);
        mainPanel.add(dateOfBirthField, gbc);
        
        // Gender
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        mainPanel.add(genderCombo, gbc);
        
        // Department
        gbc.gridx = 0; gbc.gridy = 7;
        mainPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        departmentCombo = new JComboBox<>(new String[]{
            "Computer Science", "Engineering", "Business Administration", 
            "Medicine", "Arts", "Mathematics", "Physics", "Chemistry", "Biology"
        });
        departmentCombo.setEditable(true);
        mainPanel.add(departmentCombo, gbc);
        
        // Year of Study
        gbc.gridx = 0; gbc.gridy = 8;
        mainPanel.add(new JLabel("Year of Study:"), gbc);
        gbc.gridx = 1;
        yearCombo = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
        mainPanel.add(yearCombo, gbc);
        
        // GPA
        gbc.gridx = 0; gbc.gridy = 9;
        mainPanel.add(new JLabel("GPA:"), gbc);
        gbc.gridx = 1;
        gpaField = new JTextField(20);
        mainPanel.add(gpaField, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 10;
        mainPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(20);
        mainPanel.add(addressField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton(isEdit ? "Update" : "Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> saveStudent());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
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
        if (studentIdField.getText().trim().isEmpty() ||
            firstNameField.getText().trim().isEmpty() ||
            lastNameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            Double.parseDouble(gpaField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid GPA!", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
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
            pstmt.setDate(6, Date.valueOf(dateOfBirthField.getText()));
            pstmt.setString(7, (String) genderCombo.getSelectedItem());
            pstmt.setString(8, (String) departmentCombo.getSelectedItem());
            pstmt.setInt(9, Integer.parseInt((String) yearCombo.getSelectedItem()));
            pstmt.setDouble(10, Double.parseDouble(gpaField.getText()));
            pstmt.setString(11, addressField.getText().trim());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Student added successfully!");
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
            pstmt.setDate(6, Date.valueOf(dateOfBirthField.getText()));
            pstmt.setString(7, (String) genderCombo.getSelectedItem());
            pstmt.setString(8, (String) departmentCombo.getSelectedItem());
            pstmt.setInt(9, Integer.parseInt((String) yearCombo.getSelectedItem()));
            pstmt.setDouble(10, Double.parseDouble(gpaField.getText()));
            pstmt.setString(11, addressField.getText().trim());
            pstmt.setInt(12, student.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
            }
        }
    }
}