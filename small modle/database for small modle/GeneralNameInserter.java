import java.util.*;

public class GeneralNameInserter {
    //need to add an option for adding questions for the categories.
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("What table do you want to insert into? ");

        String table = scanner.nextLine();
        
        String[] names = new String[1500];
        int i = 0;
        System.out.println("Enter the names in separate rows: ");

        for (; scanner.hasNextLine(); i++){
            names[i] = scanner.nextLine();
        }
        for (int j = 0; j<i; j++){
            System.out.println("INSERT INTO " + table + "('" + names[j] + "');");
        }
    }
}
