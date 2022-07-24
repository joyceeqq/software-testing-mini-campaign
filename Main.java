import java.util.List;
import java.util.Arrays;

public class Main {
     
    public static void main(String[] args){
        try {
            List<String> cliArgs = Arrays.asList(args);
            int i = cliArgs.indexOf("--columns");
            validateArguments(cliArgs);
            String c = cliArgs.get(i + 1);
            String[] cols = c.split(",");
      
            ReadCSV reader = new ReadCSV();
            String filename1 = args[0];
            String filename2 = args[1];
      
            CSV a = null;
            CSV b = null;
      
            a = reader.openFile("./sample/" + filename1);
            b = reader.openFile("./sample/" + filename2);
            reader.compareFiles(a, b, cols);
      
          } catch (ReadCSVException e) {
            System.out.println(e.getMessage());
            System.exit(1);
          }
        }

        public static void validateArguments(List<String> cliArgs) throws ReadCSVException {

            if (cliArgs.size() == 0) {
              throw new ReadCSVException("There is no argument stated.");
            }
        
            int i = cliArgs.indexOf("--columns");
        
            if (i == -1) {
              throw new ReadCSVException("--columns not specified");
            }
        
        
            if (i < 2) {
              throw new ReadCSVException("Filenames must be specified first");
            }
        
            if (cliArgs.get(cliArgs.size()-1) == "--columns") {
              throw new ReadCSVException("No columns selected.");
            }
        
            String filename1 = cliArgs.get(0);
            String filename2 = cliArgs.get(1);
        
            if (!filename1.endsWith(".csv") || !filename2.endsWith(".csv")) {
              throw new ReadCSVException("Files must be of type: csv");
            }
          }
        }