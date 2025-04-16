import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Student dao = new Student();
        
        dao.createTable();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Add Student\n2. View All\n3. Update\n4. Delete\n5. Exit");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Name: ");
                    String name = scanner.next();
                    System.out.print("Email: ");
                    String email = scanner.next();
                    System.out.print("Course: ");
                    String course = scanner.next(); // New field
                    dao.insert(name, email, course);  // Modified method
                    break;
                case 2:
                    dao.getAll();
                    break;
                case 3:
                    System.out.print("ID to update: ");
                    int id = scanner.nextInt();
                    System.out.print("New Name: ");
                    String newName = scanner.next();
                    System.out.print("New Email: ");
                    String newEmail = scanner.next();
                    System.out.print("New Course: ");
                    String newCourse = scanner.next(); // New field
                    dao.update(id, newName, newEmail, newCourse);  // Modified method
                    break;
                case 4:
                    System.out.print("ID to delete: ");
                    int delId = scanner.nextInt();
                    dao.delete(delId);
                    break;
                case 5:
                    dao.clearAll();
                    break;
                case 6:
                    System.exit(0);
                    break;
            }
        }
    }
}
