import java.util.*;
import java.
public class sessionOp1 {
    public static void main(String[] args) {
        while((a1+b2+c3-a2-a3-b3)>1){ //loop stops when there is only one prson left
            String quest = findBest();  //finds the best question to ask
            Boolean ans = askQuestion(quest);
            refreshData(ans);
        }
        findPersonInPeopleRelationsTable();
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

    public static String findBest(table tbl){
        int t = a1+b2+c3-a2-a3-b3;
        int[] categories = {t/2-a1, t/2-b2, t/2-c3}; //all are positve

        int min = categories[0]; 

        for (int i = 0; i < categories.length; i++) { 
            if (categories[i] < min) { 
                min = categories[i];
            }
        }
    }
}
