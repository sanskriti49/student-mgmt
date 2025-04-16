
# Student Database Management System

This is a simple Student Database Management System built using Java and SQLite. It allows you to add, update, delete, and view student records through a graphical user interface (GUI). The system uses the SQLite database to store student details such as name and email.

## Features
- Add a new student record.
- View all student records.
- Update student details.
- Delete a specific student record.
- Clear all student records and reset IDs.

## Technologies Used
- Java
- SQLite
- JDBC (Java Database Connectivity)
- Swing (for GUI)

**Running the Project**
Clone the Repository: Clone the repository to your local machine:

```git clone <your-github-repository-url> ```

**Compiling the Code:** Navigate to the project directory and compile the Java files:

```javac -cp ".;lib/sqlite-jdbc-3.49.1.0.jar" Main.java Student.java StudentGUI.java```

**Running the Application:** To run the application, use the following command:

```java -cp ".;lib/sqlite-jdbc-3.49.1.0.jar" Main```

