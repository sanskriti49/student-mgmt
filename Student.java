import java.sql.*;

public class Student {
    private final String url = "jdbc:sqlite:students.db";

    public Student() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("JDBC Driver loaded!");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found!");
        }
    }
    
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS students (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, course TEXT)";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String name, String email, String course) {
        String sql = "INSERT INTO students(name, email, course) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, course);
            pstmt.executeUpdate();
            System.out.println("Student added!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getAll() {
        String sql = "SELECT * FROM students";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Students:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("name") + " - " + rs.getString("email") + " - " + rs.getString("course"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(int id, String name, String email, String course) {
        String sql = "UPDATE students SET name = ?, email = ?, course = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, course);
            pstmt.setInt(4, id);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "Updated!" : "Student not found.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "Deleted!" : "Student not found.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void clearAll() {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM students");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='students'");
            System.out.println("All records cleared and ID reset!");
        } catch (SQLException e) {
            System.out.println("Error clearing records: " + e.getMessage());
        }
    }

    public void resetTable() {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
    
            stmt.executeUpdate("DROP TABLE IF EXISTS students");
    
            String sql = "CREATE TABLE students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "email TEXT, " +
                    "course TEXT)";
            stmt.executeUpdate(sql);
    
            System.out.println("Table reset with 'course' column!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
}
