import java.sql.*;
import java.util.*;

public class InsertIntoPeopleCategories {
    public static void main(String[] args) throws SQLException {
       DatabaseConnection dbCon = new DatabaseConnection();
       Connection con = dbCon.getConnection(); 
        
        // create a scanner object to read user input
        Scanner scanner = new Scanner(System.in);
        
        // get user input
        System.out.println("Enter the data you want to insert, in the following order: \n   person_name, than enter a list of categories with value true, separated by spaces. \n   insert each person in a separate line.");

        String output = "";

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            } 
            output += insertLine(line, con);
        }
        
        System.out.println(output);
    }

    public static String insertLine(String line, Connection con) throws SQLException{
        List<String> categories = Arrays.asList(line.split("\\s+"));

        int person_id =  1; getPersonId(con, categories);

        if (person_id == -1){
            return "";
        }
        
        // create SQL INSERT statement
        String insertStatement = "INSERT INTO people_categories (person_name";
        String values = "VALUES (" + person_id + ", '" + categories.get(0) + "'"; //adds to values the id and the name
        
        for (int i = 1; i<categories.size(); i++) { //starts from first category skiping name
            insertStatement += ", " + categories.get(i);
            values += ", true";
        }
        
        insertStatement += ") \n" + values + "); \n";
        
        // print SQL INSERT statement
       return insertStatement;
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
