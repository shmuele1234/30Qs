import java.sql.*;
import java.util.*;


public class PeopleCategoriesInsertDemo {

    public static void main(String[] args) throws SQLException {
        DatabaseConnection dbCon = new DatabaseConnection();
        Connection con = dbCon.getConnection();
        
        // create a scanner object to read user input
        Scanner scanner = new Scanner(System.in);
        
        // get user input
        System.out.print("Enter the person's name, than enter a list of categories with value true, separated by spaces: ");
        String categoriesInput = scanner.nextLine();

        List<String> categories = Arrays.asList(categoriesInput.split("\\s+"));

        int person_id = getPersonId(con, categories);

        if (person_id == -1){
            return;
        }
        
        // create SQL INSERT statement
        String insertStatement = "INSERT INTO people_categories (person_name";
        String values = "VALUES (" + person_id + ", '" + categories + "'"; //adds to values the id and the name
        
        for (String category : categories) {
            insertStatement += ", " + category;
            values += ", true";
        }
        
        insertStatement += ") " + values + ");";
        
        // print SQL INSERT statement
        System.out.println(insertStatement);
    }

    public static int getPersonId(Connection con, List<String> person_name) throws SQLException{ 
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM people WHERE person_name = " + "'" + person_name + "'");

        if (rs.next()){
            System.out.println("more than one person named: " + person_name);
            return -1;
        }

        int person_id = rs.getInt("person_id");
        return person_id;
    }
}
