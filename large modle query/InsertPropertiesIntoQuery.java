import java.util.*;

public class InsertPropertiesIntoQuery {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        String[] property = new String[5000];
        int i = 0;
        System.out.println("Enter the IDs in separate rows: ");

        for (; scanner.hasNextLine(); i++){
            property[i] = scanner.nextLine();
        }

        System.out.println("SELECT ?person ?personLabel ");

        for (int j = 0; j<i; j++){
            System.out.println("?Property" + j + "Label ");
        }

        System.out.println("WHERE \n{ \nVALUES ?person { \n}");

        for (int j = 0; j<i; j++){
            System.out.println("OPTIONAL { ?person wdt:" + property[j] + " ?Property" + j +" .}");
        }

        System.out.println("SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\". } \n}");

    }
}
