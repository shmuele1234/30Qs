import java.util.*;

public class GeneralNameInserter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("What table do you want to insert into? ");

        String table = scanner.nextLine();
        
        // get user input
        System.out.print("Enter the names separated by spaces no spaces in names allowed: ");
        String categoriesInput = scanner.nextLine();

        List<String> names = Arrays.asList(categoriesInput.split("\\s+"));

        for (String name: names){
            System.out.println("\nINSERT INTO " + table + "('" + name + "');");
        }
    }
}
