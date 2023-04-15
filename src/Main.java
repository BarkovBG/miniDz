import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<String> NAMES = Arrays.asList("Boris", "Peter", "Eva", "Nasya");


    public static void main(String[] args) {

        Student.clearStudentTable();

        for (String name : NAMES) {
            Student student = new Student(name);
            student.save();
        }

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equals("/r")) {
                Student randomStudent = Student.getRandomStudent();
                System.out.println("Random student name: " + randomStudent.getName());
                System.out.println("Random student id: " + randomStudent.getId());
                if (randomStudent.isPresent()) {
                    System.out.println("The student has already answered");
                    continue;
                }
                System.out.println("What is the grade for this student?");

                String gradeString;
                int grade = 0;
                while (true) {
                    gradeString = scanner.nextLine().trim();
                    try {
                        grade = Integer.parseInt(gradeString);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a number for the grade");
                    }
                }
                randomStudent.setGrade(grade);
            } else if (input.equals("/l")) {
                Student.printStudentList();
            } else if (input.equals("exit")) {
                break;
            } else if (input.equals("/h")) {
                System.out.println("1. /r - choose random student\n2. /l - list of student with grades");
            } else {
                System.out.println("Incorrect command");
            }
        }

        scanner.close();
    }
}
