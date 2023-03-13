import java.util.*;
import java.sql.*;

public class basicSession {

    private static int numOfCategories; //the total number of categories
    private static int numOfPeople; //the total number of people
    private static int numOfActivePeople; //number of people that have not been ruled out yet
    private static List<Integer> askedQuestions = new ArrayList<>(); // an array contaning the ids of all the asked questions 
    private static int[] activePeople; //the total of active people in a category; the index is the category_id
    private static int numOfAskedQuestions = 0;

    public static void main(String[] args) throws Exception{
        DatabaseConnection dbCon = new DatabaseConnection();
        Connection con = dbCon.getConnection();

        numOfCategories = getNumOfCategories(con);
        numOfPeople = getnumOfPeople(con);
        numOfActivePeople = numOfPeople;

        boolean[][] people_categoriesTable = new boolean[numOfCategories][numOfPeople]; //this connects to people_categories table in the database
        people_categoriesTable = getPeople_categoriesTable(con);
        String[] categoryName = new String[numOfCategories]; //this connects to categories table in the database; for debug only. no use in the code
        categoryName = getCategoryName(con);
        String[] categoryQuestion = new String[numOfCategories]; //this connects categories table in the database
        categoryQuestion = getCategoryQuestion(con);
        String[] personName = new String[numOfPeople]; //this connects to people table in the database
        personName = getpersonName(con);
        activePeople = new int[numOfCategories];
        //the index of all these arrays, is the id of the person or category -1 (because we are counting from 0) accordingly 
        //dbCon.closeConnection();
        while (numOfActivePeople>1) {//argument that means that only one person remains 
            if (numOfAskedQuestions == numOfCategories){
                throw new Exception ("Could not find person"); //give an exception that the person has not been found
            }
            int idOfBestQuestion = findBest(people_categoriesTable);
            boolean userAnswerIsPositive = askQuestion(categoryQuestion[idOfBestQuestion]);
            updateData(userAnswerIsPositive, people_categoriesTable, idOfBestQuestion);

            printCurrentStatus(people_categoriesTable, categoryName, personName, numOfActivePeople, numOfAskedQuestions); //for debug. no use in code
        }
        int personId = findPerson(people_categoriesTable);
        if (personId == -1)
            System.out.println("person does not exist");

        System.out.println("the person is: " + personName[personId]);
    }
    
    public static int findPerson(boolean[][] people_categoriesTable) {//returns the id of the only active person left 
        // check here to find the error

        //j is the id of the person
        //i is the id of the category

        for (int j = 0; j<people_categoriesTable[0].length; j++){ //finds a person with a still active category

            for (int i = 0; i<people_categoriesTable.length; i++){
                if (people_categoriesTable[i][j])
                    return j;
            }
        }

       return -1;
    }

    public static boolean askQuestion(String quest) { // user interface
        Scanner scn = new Scanner(System.in);
        while (true) {
            System.out.println(quest);
            String answer = scn.nextLine();
            switch (answer) {
                case "y":
                    numOfAskedQuestions++;
                    return true;

                case "n":
                    numOfAskedQuestions++;
                    return false;

                default:
                    System.out.println("invalid input");
                    break;
            }
        }
    }

    public static int findBest(boolean[][] personStatusOnCategory) throws Exception{ //first box indicates the category_id and the second box indicates the person_id

        for (int i = 0; i<activePeople.length; i++) {//i indexes the category_id
            activePeople[i] = 0;

            for (int j = 0; j<personStatusOnCategory[i].length; j++){ //j indexes the person_id
                if (personStatusOnCategory[i][j])
                    activePeople[i]++; //adds one to the numbering of the total of people that are in category of index i
                }
        }
        System.out.println(Arrays.toString(activePeople));
        int best = findClosesdTo50(); //returns the id of the chosen category
        return best;
    }

    public static int findClosesdTo50() throws Exception{
        int[] total = new int[activePeople.length];
        for (int i = 0; i<total.length; i++){
            total[i] = activePeople[i];
        }
        
        for (int i = 0; i<total.length; i++){
            total[i] = Math.abs(numOfActivePeople/2 - total[i]); //measures the displasment of the category from the ideal one for querying (witch is to include 50% of the people)
        }
        System.out.println("activPeople: " + Arrays.toString(activePeople));
        /*
        Arrays.sort(total); //arranges the categories in order from most to least efctive for querying
       
        int i = 0; //tracks the category_id of the candidate question 
        int j = 0; 

        for (; j<numOfAskedQuestions; j++) { //the loop stops if we find a place where there is no id of a question is stored- 
                                             //therefor category i has not been quereid upon yet and we can save in that spot the id of the chosen category
            
            if(total[i] == askedQuestions[j]){ //if the question was asked:
                i++; //check if the next efective category has been asked
                j = 0; //start the loop over for chcking the next caregory
            }
        }   
            askedQuestions[j] = i; //saves the id of the chosen category in the askedQuestions array 
            return i; //returns the id of the chosen category
        
         alternative code, does not include checking if the question has already been asked: */
        int min = total[0]; 
        
        int minId = 0;
        for (int i = 0; i < total.length; i++) { 
            if (total[i] < min) { 
                min = total[i];
                if (!askedQuestions.contains(i)){
                    minId = i;
                } else if (minId == 0) throw new Exception("no question found");  
            }
        }

        askedQuestions.add(minId);
        return minId;
    }

    public static void updateData(boolean userAnswerIsPositive, boolean[][] people_categoriesTable, int idOfBestQuestion) {
        System.out.println("total: " + Arrays.toString(activePeople));
        System.out.println("idOfBestQuestion: " + idOfBestQuestion);
        System.out.println("total[idOfBestQuestion] " + activePeople[idOfBestQuestion]);
        if (userAnswerIsPositive){ //if user's answer is positive, set to false all categories of the people not associated with the asked question's category
            numOfActivePeople = activePeople[idOfBestQuestion];
            for (int j = 0; j<people_categoriesTable[idOfBestQuestion].length; j++){
                if (!people_categoriesTable[idOfBestQuestion][j] ){

                    for (int i = 0; i<people_categoriesTable.length; i++)
                        people_categoriesTable[i][j] = false;
                }
            }
        }

        else{ //if user's answer is negetive, set to false all categories of the people associated with the asked question's category
            numOfActivePeople -= activePeople[idOfBestQuestion];
            for (int j = 0; j<people_categoriesTable[idOfBestQuestion].length; j++){
                if (people_categoriesTable[idOfBestQuestion][j]){

                    for (int i = 0; i<people_categoriesTable.length; i++)
                        people_categoriesTable[i][j] = false;
                }
            }
        }
    }

    public static void printCurrentStatus(boolean[][] people_categoriesTable, String[] categoryName, String[] personName,
                                          int numOfActivePeople, int numOfAskedQuestions) { //prints all variables for debug
        
        System.out.print("\n\t");
        for(int i = 0; i<categoryName.length; i++){
            System.out.print(categoryName[i] + "\t");
        }
        
        for (int j = 0; j<people_categoriesTable[0].length; j++){
            System.out.print("\n" + personName[j] + "\t");
            for (int i = 0; i<people_categoriesTable.length; i++){
                System.out.print(people_categoriesTable[i][j] + "\t");
            }
        }

        System.out.println("\n\nnumOfActivePeople: " + numOfActivePeople);

        System.out.println("numOfAskedQuestions: " + numOfAskedQuestions);


    }

    public static int getNumOfCategories(Connection con) throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM categories ORDER BY category_id DESC"); 

        rs.next();
        int catNum = rs.getInt("category_id");
        
        return catNum;
    }

    public static int getnumOfPeople(Connection con) throws SQLException{ 
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM people ORDER BY person_id DESC");

        rs.next();
        int pplNum = rs.getInt("person_id");
        return pplNum;
    }

    public static boolean[][] getPeople_categoriesTable(Connection con) throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM people_categories");

        boolean[][] valueOfPersonInColumn = new boolean[numOfCategories][numOfPeople];

        for (int i = 0; rs.next(); i++){
            for (int j = 0; j < numOfCategories; j++) {
                valueOfPersonInColumn[j][i] =  rs.getBoolean(j+3);
            }
        }

        return valueOfPersonInColumn;
    }

    public static String[] getCategoryName(Connection con) throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM categories");
        
        String[] categoryName = new String[numOfCategories];

        for (int i = 0; rs.next(); i++){  
            categoryName[i] = rs.getString("category_name");
        }

        return categoryName;
    }

    public static String[] getCategoryQuestion(Connection con) throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM categories");

        String question[] = new String[numOfCategories];
        
        for (int i = 0; rs.next(); i++){  
            question[i] = rs.getString("question");
        }

        return question;
    }

    public static String[] getpersonName(Connection con) throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM people");

        String name[] = new String[numOfPeople];
        
        for (int i = 0; rs.next(); i++){ 
            name[i] = rs.getString("person_name");
        }

        return name;
    }

}
