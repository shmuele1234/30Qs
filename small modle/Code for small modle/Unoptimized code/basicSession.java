import java.util.*;
import java.sql.*;

public class basicSession {

    private static int numOfCategories; //the total number of categories
    private static int numOfPeople; //the total number of people
    private static int numOfActivePeople = numOfPeople; //number of people that have not been ruled out yet
    private static int[] askedQuestions = new int[numOfCategories]; // an array contaning the ids of all the asked questions 
    private static int numOfAskedQuestions = 0;

    public static void main(String[] args) throws Exception{
        DatabaseConnection dbCon = new DatabaseConnection();
        Connection con = dbCon.getConnection();

        numOfCategories = getNumOfCategories(con);
        numOfPeople = getnumOfPeople(con);

        boolean[][] people_categoriesTable = new boolean[numOfCategories][numOfPeople]; //this connects to people_categories table in the database
        people_categoriesTable = getPeople_categoriesTable(con);
        String[] categoryName = new String[numOfCategories]; //this connects to categories table in the database; for debug only. no use in the code
        categoryName = getCategoryName(con);
        String[] categoryQuestion = new String[numOfCategories]; //this connects categories table in the database
        categoryQuestion = getCategoryQuestion(con);
        String[] personName = new String[numOfPeople]; //this connects to people table in the database
        personName = getpersonName(con);
        //the index of all these arrays, is the id of the person or category accordingly 
        dbCon.closeConnection();

        while (numOfActivePeople>1) {//argument that means that only one person remains 
            printCurrentStatus(people_categoriesTable, categoryName, personName, numOfActivePeople, askedQuestions, numOfAskedQuestions); //for debug. no use in code

            if (numOfAskedQuestions == numOfCategories){
                throw new Exception ("Could not find person"); //give an exception that the person has not been found
            }
            int idOfBestQuestion = findBest(people_categoriesTable);
            boolean userAnswerIsPositive = askQuestion(categoryQuestion[idOfBestQuestion]);
            updateData(userAnswerIsPositive, people_categoriesTable, idOfBestQuestion);
        }
        int personId = findPerson(people_categoriesTable);
        System.out.println("the person is: " + personName[personId]);
    }
    
    public static int findPerson(boolean[][] people_categoriesTable) {//returns the id of the only active person left 
        // check here to find the error

        int j = 0; //the id of the person
        int i = 0; //the id of the category

        for (; j<people_categoriesTable[i].length; j++){ //finds a person with a still active category

            for (; i<people_categoriesTable.length; i++){
                if (people_categoriesTable[i][j])
                    break;
            }
        }

        return j;
    }

    public static boolean askQuestion(String quest){ //user interface 
        Scanner scn = new Scanner(System.in);
        while (true) {
            System.out.println(quest);
        String answer = scn.next();
        switch (answer) {
            case "y":
                scn.close();
                return true;

            case "n":
                scn.close();
                return false;

            default:
                System.out.println("invalid input");
                break;
        }
        } 
    }

    public static int findBest(boolean[][] personStatusOnCategory){ //first box indicates the category_id and the second box indicates the person_id
        int[] total = new int[numOfCategories]; //the total of active people in a category; the index is the category_id

        for (int i = 0; i<total.length; i++) {//i indexes the category_id
            total[i] = 0;

            for (int j = 0; j<personStatusOnCategory[i].length; j++){ //j indexes the person_id
            if (personStatusOnCategory[i][j])
                total[i]++; //adds one to the numbering of the total of people that are in category of index i
            }
        }

        return findClosesdTo50(total); //returns the id of the chosen category
    }

    public static int findClosesdTo50(int[] arr){
        for (int i = 0; i<arr.length; i++){
            arr[i] = Math.abs(numOfCategories/2 - arr[i]); //measures the displasment of the category from the ideal one for querying (witch is to include 50% of the people)
        }
        
        Arrays.sort(arr); //arranges the categories in order from most to least efctive for querying
        
        int i = 0; //tracks the category_id of the candidate question 
        int j = 0; 

        for (; j<numOfAskedQuestions; j++) { //the loop stops if we find a place where there is no id of a question is stored- 
                                             //therefor category i has not been quereid upon yet and we can save in that spot the id of the chosen category
            
            if(arr[i] == askedQuestions[j]){ //if the question was asked:
                i++; //check if the next efective category has been asked
                j = 0; //start the loop over for chcking the next caregory
            }
        }   
            askedQuestions[j] = i; //saves the id of the chosen category in the askedQuestions array 
            return i; //returns the id of the chosen category
        
        /* alternative code, does not include checking if the question has already been asked:
         int min = arr[0]; 
        
        int i = 0;
        for (; i < arr.length; i++) { 
            if (Math.abs(arr.length/2 - arr[i]) < min) { 
                min = arr[i];
            }
        }

        return i;*/
    }
    public static void updateData(boolean userAnswerIsPositive, boolean[][] people_categoriesTable, int idOfBestQuestion) {
        if (userAnswerIsPositive){ //if user's answer is positive, set to false all categories of the people not associated with the asked question's category
            for (int j = 0; j<people_categoriesTable[idOfBestQuestion].length; j++){
                if (!people_categoriesTable[idOfBestQuestion][j]){
                    numOfActivePeople--; //decrement numOfPeople since this person is no longer relevent for us 

                    for (int i = 0; i<people_categoriesTable.length; i++)
                    people_categoriesTable[i][j] = false;
                }
            }
        }

        else{ //if user's answer is negetive, set to false all categories of the people associated with the asked question's category
            for (int j = 0; j<people_categoriesTable[idOfBestQuestion].length; j++){
                if (people_categoriesTable[idOfBestQuestion][j]){
                    numOfActivePeople--; //decrement numOfPeople since this person is no longer relevent for us 

                    for (int i = 0; i<people_categoriesTable.length; i++)
                        people_categoriesTable[i][j] = false;
                }
            }
        }
    }

    public static void printCurrentStatus(boolean[][] people_categoriesTable, String[] categoryName, String[] personName,
                                          int numOfActivePeople, int[] askedQuestions, int numOfAskedQuestions) { //prints all variables for debug
        
        System.out.print("\n      ");
        for(int i = 0; i<categoryName.length; i++){
            System.out.print(categoryName[i] + " ");
        }
        
        int i = 0;
        for (int j = 0; j<people_categoriesTable[i].length; j++){
            System.out.print("\n" + personName[j] + "  ");
            for (; i<people_categoriesTable.length; i++){
                System.out.print(people_categoriesTable[i][j] + " ");
            }
        }

        System.out.println("\n numOfActivePeople: " + numOfActivePeople);

        System.out.println("\n numOfAskedQuestions: " + numOfAskedQuestions);

        for (int j = 0; j<askedQuestions.length; j++){
            System.out.println(categoryName[askedQuestions[j]]);
        }

    }

    public static int getNumOfCategories(Connection con) throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * categories ORDER BY category_id DESC"); 

        int catNum = rs.getInt("category_id");
        return catNum;
    }

    public static int getnumOfPeople(Connection con) throws SQLException{ 
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * people ORDER BY person_id DESC");

        int pplNum = rs.getInt("person_id");
        return pplNum;
    }

    public static boolean[][] getPeople_categoriesTable(Connection con) throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * people_categories");
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        boolean[][] valueOfPersonInColumn = new boolean[numOfCategories][numOfPeople];

        for (int i = 0; rs.next(); i++){
            for (int j = 1; j <= columnCount; j++) {
                valueOfPersonInColumn[j][i] =  rs.getBoolean(i);
            }
        }

        return valueOfPersonInColumn;
    }

    public static String[] getCategoryName(Connection con) throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * categories");
        
        String[] categoryName = new String[numOfCategories];

        for (int i = 0; rs.next(); i++){  
            categoryName[i] = rs.getString("category_name");
        }

        return categoryName;
    }

    public static String[] getCategoryQuestion(Connection con) throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * categories");

        String question[] = new String[numOfCategories];
        
        for (int i = 0; rs.next(); i++){  
            question[i] = rs.getString("category_question");
        }

        return question;
    }

    public static String[] getpersonName(Connection con) throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * people");

        String name[] = new String[numOfPeople];
        
        for (int i = 0; rs.next(); i++){ 
            name[i] = rs.getString("name");
        }

        return name;
    }

}
