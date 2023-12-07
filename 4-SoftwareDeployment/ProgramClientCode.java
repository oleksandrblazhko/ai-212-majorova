import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5455/lab12";

        // Database credentials
        String user = "postgres";
        String password = "root";

        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(url, user, password);

            // Create a statement
            Statement statement = connection.createStatement();

            // Execute a query
            String query = "SELECT * FROM example_table";
            ResultSet resultSet = statement.executeQuery(query);

            // Process the query results
            while (resultSet.next()) {
                // Replace 'column_name' with your column names
                System.out.println(resultSet.getInt(1) + " " +
                        resultSet.getString(2) + " " + resultSet.getInt(3));
            }

            // Close the resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
