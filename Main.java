import java.util.List;
import java.util.Arrays;

public class Main {
     
    public static void main(String[] args){
        List<String> clickArgs = Arrays.asList(args);
        int variable = clickArgs.indexOf("--columns");

        if (variable == -1){
            System.out.println("Error: Columns not specified");
            System.exit(1);
        }

        String variable2 = clickArgs.get(variable + 1);
        String[] columns = variable2.split(",");

        if (columns.length == 0){
            System.out.println("Error: There are no columns selected");
            System.exit(1);
        }

        ReadCSV reader = new ReadCSV();
        String filename1 = args[0];
        String filename2 = args[1];

        CSV arg1 = reader.openFile("./sample/" + filename1);
        CSV arg2 = reader.openFile("./sample/" + filename2);

        reader.compareFiles(arg1, arg2, columns);

    }
}