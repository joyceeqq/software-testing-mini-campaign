import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSV {
    String con = "";
    ArrayList<String> lines = new ArrayList<>();
    String[] head = new String[0];
    int count = 0;

    public CSV(String pathToFile) throws ReadCSVException{
        try {
            BufferedReader bufferReader = new BufferedReader(new FileReader(pathToFile));
            String oneLine;

            while ((oneLine = bufferReader.readLine())!=null){
                count++;
                con += oneLine + "\n";
                String[] tokenised = oneLine.split(",");

                if (count == 1){
                    head = tokenised;
                } else{
                    if (tokenised.length != head.length){
                        System.out.println("Invalid file: " + pathToFile);
                        throw new ReadCSVException("Your csv file is malformed");
                    }
                }

                if (oneLine.matches("^[,|\s]+$")){
                    throw new ReadCSVException("You have a blank header or content");
                }

                lines.add(oneLine);
            }
            bufferReader.close();
        } catch (IOException e) {
            throw new ReadCSVException("File is not found");
        }
    }
}
