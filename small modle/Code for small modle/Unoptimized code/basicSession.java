import java.util.*;

public class basicSession {

    private static int numOfCategories; //value initialized in function not writen yet
    private static int numOfPeople; //value initialized in function not writen yet
    private static int[] askedQuestions = new int[numOfCategories];
    private static int numOfAskedQuestions = 0;

    public static void main(String[] args) {

        boolean[][] people_categoriesTable = new boolean[numOfCategories][numOfPeople]; //this connects to people_categories table in the database
        String[] categoryName = new String[numOfCategories]; //this connects to categories table in the database; for debug only. no use in the code
        String[] categoryQuestion = new String[numOfCategories]; //this connects categories table in the database
        String[] personName =new String[numOfPeople]; //this connects to people table in the database
        //the index of all these arrays, is the id of the person or category accordingly 

        while (numOfPeople>1) {//argument that means that only one person remains 
            if (numOfAskedQuestions == numOfCategories){
                //give an error that the person has not been found
                return;
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

        for (; j<people_categoriesTable[i].length; j++){

            for (; i<people_categoriesTable.length; i++){
                if (people_categoriesTable[i][j])
                    break;
            }
        }

        return j;
    }

    public static boolean askQuestion(String quest){
        Scanner scn = new Scanner(System.in);
        while (true) {
            System.out.println(quest);
        String b =scn.next();
        switch (b) {
            case "y":
                return true;

            case "n":
                return false;

            default:
                System.out.println("invalid input");
                break;
        }
        }
        
    }

    public static int findBest(boolean[][] personStatusOnCategory){ //first box indicates the category_id and the second box indicates the person_id
        int[] total = new int[numOfCategories]; //the total of active people in a category; length of first box

        for (int i = 0; i<total.length; i++) {
            total[i] = 0;

            for (int j = 0; j<personStatusOnCategory[i].length; j++){ //length of second box
            if (personStatusOnCategory[i][j])
                total[i]++;
            }
        }

        return findClosesdTo50(total);
    }

    public static int findClosesdTo50(int[] arr){
        Arrays.sort(arr);
        
        int i = 0;
        while(true){

            for (int j = 0; j<numOfAskedQuestions; j++) {
                if(arr[i] != askedQuestions[j]){
                    askedQuestions[j] = arr[i];
                    return arr[i];
                }
            }
        i++;
        }
        /*  int min = arr[0]; 
        
        int i = 0;
        for (; i < arr.length; i++) { 
            if (Math.abs(arr.length/2 - arr[i]) < min) { 
                min = arr[i];
            }
        }

        return i;*/
    }
    public static void updateData(boolean userAnswerIsPositive, boolean[][] people_categoriesTable, int idOfBestQuestion) {
        if (userAnswerIsPositive){
            for (int j = 0; j<people_categoriesTable[idOfBestQuestion].length; j++){
                if (!people_categoriesTable[idOfBestQuestion][j]){
                    numOfPeople--;

                    for (int i = 0; i<people_categoriesTable.length; i++)
                    people_categoriesTable[i][j] = false;
                }
            }
        }

        else{
            for (int j = 0; j<people_categoriesTable[idOfBestQuestion].length; j++){
                if (people_categoriesTable[idOfBestQuestion][j]){
                    numOfPeople--;

                    for (int i = 0; i<people_categoriesTable.length; i++)
                    people_categoriesTable[i][j] = false;
                }
            }
        }
    }

}
