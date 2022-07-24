import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReadCSV {
    public ReadCSV(){}

    public CSV openFile(String nameOfFile) throws ReadCSVException{
        return new CSV(nameOfFile);
    }

    public boolean compareFiles(CSV file1, CSV file2, String[] columns) throws ReadCSVException{
        ArrayList<String> e = new ArrayList<String>();

        //Headers will be checked against headers
        List<String> headersForFile1 = Arrays.asList(file1.head);
        List<String> headersForFile2 = Arrays.asList(file2.head);

        String[] cloneHeadersForFile1 = file1.head.clone();
        String[] cloneHeadersForFile2 = file2.head.clone();
        Arrays.sort(cloneHeadersForFile1);
        Arrays.sort(cloneHeadersForFile2);

        // Check if there's same number of headers or they have the same content
        if (headersForFile1.size() != headersForFile2.size()||!Arrays.equals(cloneHeadersForFile1, cloneHeadersForFile2)){
            throw new ReadCSVException("Headers of files do not match and is thus incomparable");
        }

        for (int i = 1; i < file1.count; i++){
            String line = file1.lines.get(i);
            List<String> arrayForLines = new ArrayList<String>(Arrays.asList(line.split(",")));
            List<String> arrayToCompare = new ArrayList<String>(Arrays.asList(line.split(",")));
            ArrayList<String> unique = new ArrayList<String>();
        

            for (String header: columns){
                int index = headersForFile1.indexOf("" + header +"");
                String columnValue = arrayForLines.get(index);
                unique.add(columnValue);
                arrayToCompare.remove(columnValue);
            }

            for (int j = 1; j<file2.count; j++){
                String line2 = file2.lines.get(j);
                List<String> arrayForLines2 = new ArrayList<String>(Arrays.asList(line2.split(",")));
                List<String> arrayToCompare2 = new ArrayList<String>(Arrays.asList(line2.split(",")));
                ArrayList<String> unique2 = new ArrayList<String>();

                for (String header: columns){
                    int index = headersForFile2.indexOf("" + header + "");
                    String columnValue = arrayForLines2.get(index);
                    unique2.add(columnValue);
                    arrayToCompare2.remove(columnValue);
                }

                //For comparison:
                Collections.sort(unique);
                Collections.sort(unique2);

                if (unique.equals(unique2)){
                    Collections.sort(arrayToCompare);
                    Collections.sort(arrayToCompare2);

                    if(!arrayToCompare.equals(arrayToCompare2)){
                        e.add(line);
                        e.add(line2);
                    }
                }
            }
        }

        if (e.size()>0){
            System.out.println("Creating exception file in the exception folder....");
            try{
                File file = new File("./exception/output.csv");
                file.createNewFile();
                FileWriter writeCSV = new FileWriter(file);

                for (String r : e){
                    writeCSV.append(r);
                    writeCSV.append("\n");
                }

                writeCSV.flush();
                writeCSV.close();
                System.out.println("Total number of exceptions: " + e.size());
                System.out.println("You can find the exception file under the exception folder (/exception).");
            } catch (IOException ex) {
                throw new ReadCSVException("File is not found");
            }
        }
        return e.size() ==0;
    }
}
