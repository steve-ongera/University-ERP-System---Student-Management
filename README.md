# University ERP System - Student Management

A comprehensive Java desktop application for managing university student records with MySQL database integration.

## Features

### Core Functionality
- **Student Management**: Add, edit, delete, and view student records
- **Search Functionality**: Real-time search across all student fields
- **Database Integration**: MySQL database for persistent data storage
- **User Authentication**: Secure login system
- **Data Validation**: Input validation for all student fields
- **Modern UI**: Clean Swing-based user interface

### Student Information Managed
- Student ID (Unique identifier)
- Personal Details (Name, Email, Phone, Date of Birth, Gender)
- Academic Information (Department, Year of Study, GPA)
- Address Information
- Timestamps (Creation date)

## Prerequisites

### Software Requirements
- **Java Development Kit (JDK) 8 or higher**
- **MySQL Server 5.7 or higher**
- **MySQL JDBC Driver (Connector/J)**

### System Requirements
- **Operating System**: Windows, macOS, or Linux
- **RAM**: Minimum 4GB
- **Storage**: 100MB free space

## Installation & Setup

### Step 1: Install MySQL Server

1. Download and install MySQL Server from [https://dev.mysql.com/downloads/mysql/](https://dev.mysql.com/downloads/mysql/)
2. During installation, set up a root password (remember this password)
3. Start MySQL service
4. Verify installation by connecting to MySQL

### Step 2: Download MySQL JDBC Driver

1. Download MySQL Connector/J from [https://dev.mysql.com/downloads/connector/j/](https://dev.mysql.com/downloads/connector/j/)
2. Extract the JAR file (e.g., `mysql-connector-java-8.0.33.jar`)
3. Note the path to this JAR file

### Step 3: Database Configuration

1. **Update Database Credentials** in the `DatabaseConnection` class:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/university_erp";
   private static final String USERNAME = "root";
   private static final String PASSWORD = "your_mysql_password"; // Change this
   ```

2. **Create Database** (Optional - the application will create it automatically):
   ```sql
   CREATE DATABASE university_erp;
   USE university_erp;
   ```

### Step 4: Compile and Run the Application

1. **Save the code** as `UniversityERP.java`

2. **Compile with JDBC Driver**:
   ```bash
   javac -cp ".:mysql-connector-java-8.0.33.jar" UniversityERP.java
   ```
   
   On Windows:
   ```cmd
   javac -cp ".;mysql-connector-java-8.0.33.jar" UniversityERP.java
   ```

3. **Run the application**:
   ```bash
    java -cp ".;mysql-connector-j-9.4.0.jar" UniversityERP
   ```
   
   On Windows:
   ```cmd
   java -cp ".;mysql-connector-java-8.0.33.jar" UniversityERP
   ```

## Usage Guide

### Login Credentials
- **Username**: `admin`
- **Password**: `admin123`

### Main Operations

#### 1. Adding a New Student
1. Click the **"Add Student"** button
2. Fill in all required fields:
   - Student ID (must be unique)
   - First Name and Last Name
   - Email (must be unique and valid format)
   - Phone Number
   - Date of Birth (YYYY-MM-DD format)
   - Gender (dropdown selection)
   - Department (dropdown with custom option)
   - Year of Study (1-5)
   - GPA (0.00-4.00)
   - Address
3. Click **"Save"** to add the student

#### 2. Editing Student Information
1. Select a student from the table
2. Click **"Edit Student"** button
3. Modify the required fields
4. Click **"Update"** to save changes

#### 3. Deleting Students
1. Select a student from the table
2. Click **"Delete Student"** button
3. Confirm deletion in the dialog

#### 4. Searching Students
1. Type in the search box at the top
2. Search works across: Student ID, Name, Email, Department
3. Results update in real-time

#### 5. Refreshing Data
- Click **"Refresh"** button to reload the latest data from database

## Database Schema

```sql
CREATE TABLE students (
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
);
```

## Application Architecture

### Classes Overview

1. **UniversityERP**: Main class that launches the application
2. **DatabaseConnection**: Handles all database connections and table creation
3. **Student**: Model class representing student data
4. **LoginFrame**: Authentication window
5. **MainFrame**: Main application window with student table and operations
6. **StudentDialog**: Add/Edit student form dialog

### Key Features

- **Connection Pooling**: Efficient database connection management
- **Data Validation**: Input validation for all fields
- **Error Handling**: Comprehensive error handling for database operations
- **Search Functionality**: Real-time search across multiple fields
- **Responsive UI**: Clean and intuitive user interface

## Troubleshooting

### Common Issues

#### 1. ClassNotFoundException: com.mysql.cj.jdbc.Driver
**Solution**: Ensure MySQL JDBC driver is in the classpath
```bash
java -cp ".:mysql-connector-java-8.0.33.jar" UniversityERP
```

#### 2. SQLException: Access denied for user 'root'@'localhost'
**Solution**: Check MySQL credentials in `DatabaseConnection` class
- Verify username and password
- Ensure MySQL server is running

#### 3. SQLException: Unknown database 'university_erp'
**Solution**: The application will create the database automatically. If it fails:
```sql
CREATE DATABASE university_erp;
```

#### 4. Date Format Error
**Solution**: Use the correct date format: YYYY-MM-DD (e.g., 2000-01-15)

#### 5. Duplicate Entry Error
**Solution**: Ensure Student ID and Email are unique for each student

### Performance Tips

1. **Regular Database Maintenance**: Periodically optimize the database
2. **Index Optimization**: Add indexes for frequently searched fields
3. **Connection Management**: The app handles connections efficiently
4. **Data Backup**: Regular backup of the database is recommended

## Sample Data for Testing

You can insert sample data using MySQL:

```sql
INSERT INTO students (student_id, first_name, last_name, email, phone, date_of_birth, gender, department, year_of_study, gpa, address) VALUES
('STU001', 'John', 'Doe', 'john.doe@university.edu', '+1234567890', '2000-05-15', 'Male', 'Computer Science', 2, 3.75, '123 Main St, City'),
('STU002', 'Jane', 'Smith', 'jane.smith@university.edu', '+1234567891', '1999-08-22', 'Female', 'Engineering', 3, 3.89, '456 Oak Ave, City'),
('STU003', 'Mike', 'Johnson', 'mike.johnson@university.edu', '+1234567892', '2001-03-10', 'Male', 'Business Administration', 1, 3.45, '789 Pine Rd, City'),
('STU004', 'Sarah', 'Williams', 'sarah.williams@university.edu', '+1234567893', '2000-11-30', 'Female', 'Medicine', 4, 3.95, '321 Elm St, City'),
('STU005', 'David', 'Brown', 'david.brown@university.edu', '+1234567894', '1998-07-18', 'Male', 'Arts', 4, 3.67, '654 Maple Dr, City');
```

## Security Considerations

### Current Implementation
- Basic username/password authentication
- SQL injection protection using PreparedStatements
- Input validation for all fields

### Production Recommendations
- Implement password hashing (BCrypt)
- Add role-based access control
- Use environment variables for database credentials
- Implement session management
- Add audit logging
- Use HTTPS for web deployment

## Future Enhancements

### Planned Features
- [ ] Export data to PDF/Excel
- [ ] Advanced reporting and analytics
- [ ] Course enrollment management
- [ ] Grade management system
- [ ] Fee management
- [ ] Email notifications
- [ ] Backup and restore functionality
- [ ] Multi-user support with roles
- [ ] Photo upload for students
- [ ] Barcode generation for student IDs

### Technical Improvements
- [ ] Migration to Spring Boot for web version
- [ ] REST API development
- [ ] Mobile app integration
- [ ] Cloud database support
- [ ] Microservices architecture
- [ ] Docker containerization

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Make your changes
4. Add tests for new functionality
5. Commit your changes (`git commit -am 'Add new feature'`)
6. Push to the branch (`git push origin feature/new-feature`)
7. Create a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support & Contact

For support, questions, or bug reports:
- Create an issue in the repository
- Email: support@universityerp.com
- Documentation: [Wiki](https://github.com/yourrepo/wiki)

## Changelog

### Version 1.0.0 (Current)
- Initial release
- Basic CRUD operations for students
- MySQL database integration
- Search functionality
- Login authentication
- Data validation

---

**Version**: 1.0.0  
**Last Updated**: August 2025  
**Compatibility**: Java 8+, MySQL 5.7+  
**Author**: University ERP Development Team