import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentGUI extends JFrame {
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField courseField = new JTextField(20); // Added for course input
    private final JTextField idField = new JTextField(5);
    private final JTextArea displayArea = new JTextArea(10, 40);
    private final String url = "jdbc:sqlite:students.db";
    private final Student studentDAO = new Student();

    public StudentGUI() {
        setTitle("Student Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel (form)
        JPanel formPanel = new JPanel(new GridLayout(5, 2)); // Increased to 5 for the course input
        formPanel.add(new JLabel("ID (for Update/Delete):"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Course:")); // Added label for course
        formPanel.add(courseField); // Added course input field

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Student");
        JButton viewButton = new JButton("View All");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearAllButton = new JButton("Clear All");

        // Add buttons to panel
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearAllButton);

        // Display area
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        createTable();

        addButton.addActionListener(e -> insertStudent());
        viewButton.addActionListener(e -> getAllStudents());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearAllButton.addActionListener(e -> clearAllStudents());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS students (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, course TEXT)";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            showMessage("Error: " + e.getMessage());
        }
    }

    private void insertStudent() {
        String name = nameField.getText();
        String email = emailField.getText();
        String course = courseField.getText();  // Get course from the text field

        if (name.isEmpty() || email.isEmpty() || course.isEmpty()) {
            showMessage("Please enter name, email, and course.");
            return;
        }

        String sql = "INSERT INTO students(name, email, course) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, course);
            pstmt.executeUpdate();
            showMessage("Student added successfully!");
            clearFields();
        } catch (SQLException e) {
            showMessage("Error: " + e.getMessage());
        }
    }

    private void getAllStudents() {
        String sql = "SELECT * FROM students";
        displayArea.setText("ID\tName\tEmail\tCourse\n-------------------------------\n");
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                displayArea.append(rs.getInt("id") + "\t" + rs.getString("name") + "\t" + rs.getString("email") + "\t" + rs.getString("course") + "\n");
            }
        } catch (SQLException e) {
            showMessage("Error: " + e.getMessage());
        }
    }

    private void updateStudent() {
        String idText = idField.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String course = courseField.getText();  // Get course from the text field

        if (idText.isEmpty() || name.isEmpty() || email.isEmpty() || course.isEmpty()) {
            showMessage("Please enter ID, name, email, and course to update.");
            return;
        }

        int id = Integer.parseInt(idText);
        String sql = "UPDATE students SET name = ?, email = ?, course = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, course);
            pstmt.setInt(4, id);
            int rows = pstmt.executeUpdate();
            showMessage(rows > 0 ? "Student updated successfully!" : "Student not found.");
            clearFields();
        } catch (SQLException e) {
            showMessage("Error: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        String idText = idField.getText();

        if (idText.isEmpty()) {
            showMessage("Please enter an ID to delete.");
            return;
        }

        int id = Integer.parseInt(idText);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete student ID " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            showMessage(rows > 0 ? "Student deleted successfully!" : "Student not found.");
            clearFields();
        } catch (SQLException e) {
            showMessage("Error: " + e.getMessage());
        }
    }

    private void clearAllStudents() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete ALL students and reset IDs?", "Confirm Clear All", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
    
        studentDAO.clearAll(); 
        displayArea.setText("");

        getAllStudents();
    
        showMessage("All student records deleted and IDs reset!");
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        courseField.setText("");  // Clear the course field
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentGUI::new);
    }
}
