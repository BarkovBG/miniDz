import java.sql.*;

public class Student {
    private static String postgresqlAddress;
    private static String postgresqlUser;
    private static String postgresqlPassword;

    private int id;
    private String name;
    private boolean present;
    private int grade;

    static {
        postgresqlAddress = "jdbc:postgresql://localhost:5432/kpo";
        postgresqlUser = "admin";
        postgresqlPassword = "admin";
    }

    public Student(int id, String name, boolean present, int grade) {
        this.id = id;
        this.name = name;
        this.present = present;
        this.grade = grade;
    }

    public Student(String name) {
        this.id = -1;
        this.name = name;
        this.present = false;
        this.grade = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
        this.save();
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        setPresent(true);
        this.grade = grade;
        this.save();
    }

    public void save() {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection(postgresqlAddress, postgresqlUser, postgresqlPassword);

            if (id == -1) {
                statement = connection.prepareStatement("INSERT INTO students (name, present, grade) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, name);
                statement.setBoolean(2, present);
                statement.setInt(3, grade);
                statement.executeUpdate();

                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            } else {
                statement = connection.prepareStatement("UPDATE students SET name=?, present=?, grade=? WHERE id=?");
                statement.setString(1, name);
                statement.setBoolean(2, present);
                statement.setInt(3, grade);
                statement.setInt(4, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Student getById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(postgresqlAddress, postgresqlUser, postgresqlPassword);

            statement = connection.prepareStatement("SELECT * FROM students WHERE id=?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Student student = new Student(resultSet.getString("name"));
                student.id = resultSet.getInt("id");
                student.present = resultSet.getBoolean("present");
                student.grade = resultSet.getInt("grade");
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Student getRandomStudent() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Student student = null;

        try {
            connection = DriverManager.getConnection(postgresqlAddress, postgresqlUser, postgresqlPassword);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM students ORDER BY RANDOM() LIMIT 1;");

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                boolean present = resultSet.getBoolean("present");
                int grade = resultSet.getInt("grade");

                student = new Student(id, name, present, grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return student;
    }

    public static void printStudentList() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(postgresqlAddress, postgresqlUser, postgresqlPassword);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM students;");

            System.out.printf("%-10s %-20s %-10s %-10s\n", "ID", "Name", "Present", "Grade");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                boolean present = resultSet.getBoolean("present");
                int grade = resultSet.getInt("grade");

                System.out.printf("%-10d %-20s %-10b %-10d\n", id, name, present, grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearStudentTable() {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(postgresqlAddress, postgresqlUser, postgresqlPassword);
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM students;");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}