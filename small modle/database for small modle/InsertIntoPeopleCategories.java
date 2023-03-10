import java.sql.*;
import java.util.*;

public class InsertIntoPeopleCategories {
    public static void main(String[] args) throws SQLException {
       DatabaseConnection dbCon = new DatabaseConnection();
       Connection con = dbCon.getConnection(); 
        
        // create a scanner object to read user input
        Scanner scanner = new Scanner(System.in);
       
        String output = "";

        while (scanner.hasNextLine()) {
            System.out.println("Enter the person_name");

            String name = scanner.nextLine();
            if (name.isEmpty()) {
                break;
            } 
            System.out.println("enter a list of categories with value true, separated by spaces. ");

            String categories = scanner.nextLine();
            if (categories.isEmpty()) {
                break;
            } 
            output += insertLine(name, categories, con);
        }
        
        System.out.println(output);
    }

    public static String insertLine(String name, String cat ,Connection con) throws SQLException{

        int person_id = getPersonId(con, cat);

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

    public static int getPersonId(Connection con, String person_name) throws SQLException{ 
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM people WHERE name = " + "'" + person_name + "'");

        rs.next();
        int person_id = rs.getInt("person_id");

        if (rs.next()){
            System.out.println("more than one person named: " + person_name);
            return -1;
        }

        return person_id;
    }
}
