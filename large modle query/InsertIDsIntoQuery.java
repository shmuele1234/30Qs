import java.util.*;

public class InsertIDsIntoQuery {
    //need to add an option for adding questions for the categories.
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        String[] ID = new String[5000];
        int i = 0;
        System.out.println("Enter the IDs in separate rows: ");

        for (; scanner.hasNextLine(); i++){
            ID[i] = scanner.nextLine();
        }
        for (int j = 0; j<i; j++){
            System.out.println("?item wdt:" + ID[j] + " ?value" + j +" .");
        }
    }
}