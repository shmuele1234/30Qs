import java.sql.*;
import java.util.*;

public class InsertIntoPeopleCategories {
    public static void main(String[] args) throws SQLException {
       DatabaseConnection dbCon = new DatabaseConnection();
       Connection con = dbCon.getConnection(); 
        
        // create a scanner object to read user input
        Scanner scanner = new Scanner(System.in);
       
        String output = "";

        int emptyLineCount = 0;

        System.out.println("Enter person name and than enter in separate lines the categories, separate the people using an empty line: ");

        while (scanner.hasNextLine()) {
            String name = scanner.nextLine();
            String[] cats = new String[1000];
            int i = 0;
            for (;; i++){
                if (!scanner.hasNextLine())
                    break;
                cats[i] = scanner.nextLine();

                if (cats[i].equals(""))
                    break;
            }
            output += insertLine(name, cats, i, con);
        }
        System.out.println(output);
    }

    public static String insertLine(String name, String[] cats,int numcats, Connection con) throws SQLException{

        int person_id = getPersonId(con, name);

        if (person_id == -1){
            return "";
        }
        
        // create SQL INSERT statement
        String insertStatement = "INSERT INTO people_categories (person_id, person_name";
        String values = "VALUES (" + person_id + ", '" + name + "'"; //adds to values the id and the name
        
        for (int i = 0; i<numcats; i++) { //starts from first category
            insertStatement += ", " + cats[i];
            values += ", true";
        }
        
        insertStatement += ") \n" + values + "); \n";
        
        // print SQL INSERT statement
       return insertStatement;
    }

    public static int getPersonId(Connection con, String person_name) throws SQLException{ 
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM people WHERE person_name = " + "'" + person_name + "'");

        if (!rs.next()){
            System.out.println("no person named: " + person_name);
            return -1;
        }    
        int person_id = rs.getInt("person_id");

        if (rs.next()){
            System.out.println("more than one person named: " + person_name);
            return -1;
        }

        return person_id;
    }
}
