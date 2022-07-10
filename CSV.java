import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSV {
    String con = "";
    ArrayList<String> lines = new ArrayList<>();
    String[] head = new String[0];
    int count = 0;

    public CSV(String pathToFile){
        try {
            BufferedReader bufferReader = new BufferedReader(new FileReader(pathToFile));
            String oneLine;

            while ((oneLine = bufferReader.readLine())!=null){
                count++;
                con += oneLine + "\n";
                lines.add(oneLine);

                if (count == 1){
                    head = oneLine.split(",");
                }
            }
            bufferReader.close();
        } catch (IOException e) {
            System.out.println("Error:" + e.getMessage());
            System.exit(1);
        }
    }
}
