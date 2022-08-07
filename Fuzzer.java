import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Fuzzer {
    private int repeat = 1;
    private int maxStrLen = 20;
    private int maxCol= 10;
    private int maxCSVDepth = 20;
    private String pool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

    public Fuzzer(int repeat){
        if (repeat > 0){
            this.repeat = repeat;
        }
        else{
            System.out.println("Please ensure that fuzzer run at least once");
        }
    }

    public Fuzzer(){}

    public void run(){
        ArrayList<String> fails = new ArrayList<String>();
        int drops = 0;
        for (int i = 0; i<repeat; i ++){
            ArrayList<String> fileNames = new ArrayList<String>();
            ArrayList<String> data = generateCSVs();
            StringBuilder stringBuilder = new StringBuilder();

            fileNames.add(data.get(0));
            stringBuilder.append(data.get(0));
            stringBuilder.append(" ");

            fileNames.add(data.get(1));
            stringBuilder.append(data.get(1));
            stringBuilder.append(" ");

            stringBuilder.append("--columns");
            stringBuilder.append(" ");

            for (int j = 2; j <data.size(); j++){
                stringBuilder.append(String.format("\"%s\"", data.get(j)));
                stringBuilder.append(",");
            }

            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

            String cmd = stringBuilder.toString();

            // total of 3% chance of mutating
            cmd = randomlyMutate(cmd.toString(), 100);
            
            try{
                String[] args = cmd.split(" ");

                int v = new Random().nextInt(10);

                if (v==1){
                    List<String> list = new ArrayList<String>(Arrays.asList(args));
                    list.remove(args[(new Random()).nextInt(args.length)]);
                    args = list.toArray(new String[list.size()]);
                    cmd = String.join(" ", list);
                    System.out.println("An argument is randomly dropped.");
                    drops++;
                }

                Main.main(args);
            } catch (Exception e){
                fails.add(e.getMessage());
                fails.add(cmd);
            }
            System.out.println(String.format("Execution coverage: %.2f%%",((float) i /repeat)*100));
            System.out.println(String.format("Total crashes detected: %s", fails.size()));
            System.out.println(String.format("Total arguments dropped: %s", drops));
        }

        try{
            File file = new File("./fuzzed_summary.csv");
            file.createNewFile();
            FileWriter csvWriter = new FileWriter(file);

            for (String e: fails){
                csvWriter.write(e);
                csvWriter.write("\n");
            }

            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        for (int j =0; j<50; ++j) System.out.println();
        
        System.out.println("Fuzzing is completed");

        if (fails.size()>0){
            System.out.println("Oh no, there are crashes detected!");
            System.out.println("Check the output of fuzz_summary.csv");
        }
        else{
            System.out.println("Yay, there are no crashes :-)");
        }
        System.out.println(String.format("%s out of %s cases are executed.", repeat, repeat));
        System.out.println(String.format("Total No. of crashes: %s", fails.size()/2));
        System.out.println(String.format("Total No. of arguments dropped: %s", drops));

        if (fails.size() == 0){
            deleteDir(new File("./sample/fuzz"));
        }

    }

    public static boolean deleteDir(File dir){
        if (dir.isDirectory()){
            String[] child = dir.list();
            for (int i = 0; i< child.length; i++){
                boolean success = deleteDir(new File(dir, child[i]));
                if (!success){
                    return false;
                }
            }
        }
        return dir.delete();
    }

    private ArrayList<String> generateCSVs(){
        String fileName1 = "fuzz/" + generateFileName();
        String fileName2 = "fuzz/" + generateFileName();
        ArrayList<String> data = new ArrayList<String>();
        data.add(fileName1);
        data.add(fileName2);

        ArrayList<String> cols = generateColumns();
        data.addAll(cols);

        int n = cols.size();
        int depth = new Random().nextInt(maxCSVDepth);

        ArrayList<String> l = new ArrayList<String>();
        for (int i = 0; i < randomlyMutate(depth); i++){
            StringBuilder stringBuilder = new StringBuilder(randomlyMutate(n));

            for (int j = 0; j < n; j++){
                stringBuilder.append(generateStringRandomly());
                stringBuilder.append(",");
            }

            if (stringBuilder.length() > 0){
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            l.add(stringBuilder.toString());
        }

        try{
            File file = new File("./sample/" + fileName1);
            file.createNewFile();
            FileWriter csvWriter = new FileWriter(file);

            for (String row: l){
                csvWriter.append(row);
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
        }catch (IOException e){}

        try{
            File file = new File("./sample/" + fileName2);
            file.createNewFile();
            FileWriter csvWriter = new FileWriter(file);

            for (String row:l){
                // chance of dropping the whole line is 10%
                int ran = new Random().nextInt(100);
                if (ran <=9){
                    continue;
                }
                csvWriter.append(row);
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
        } catch(IOException e) {}
        return data;
    }

    private String generateFileName(){
        String validExt = ".csv";
        String string = generateStringRandomly();
        String ranFileName = string + validExt;
        
        // mutates filename randomly
        return randomlyMutate(ranFileName);
    }

    private String generateStringRandomly(){
        int len = new Random().nextInt(1, maxStrLen);
        int index = new Random().nextInt(2);
        if (index == 1){
            return randomString(len);
        }
        return randomAlphaNumericString(len);
    }

    private String randomString(int n){
        byte[] arr = new byte[n];
        new Random().nextBytes(arr);
        return new String(arr, Charset.forName("UTF-8"));
    }

    private String randomAlphaNumericString(int n){
        StringBuilder stringBuilder = new StringBuilder(n);
        for (int i = 0; i<n; i++){
            int idx = (int) (pool.length()*Math.random());
            stringBuilder.append(pool.charAt(idx));
        }
        return stringBuilder.toString();
    }

    private String randomlyMutate(String string){
        int index = new Random().nextInt(4);
        switch(index){
            // randomly delete
            case 0:
                return deleteAtRandom(string);
            // randomly insert
            case 1:
                return insertAtRandom(string);
            // randomly replace
            case 2:
                 return replaceAtRandom(string);
            default:
                return string;
        }
    }

    private String deleteAtRandom(String str){
        int n = str.length();
        int ran = new Random().nextInt(n);
        StringBuilder stringBuilder = new StringBuilder(str);
        stringBuilder.deleteCharAt(ran);
        return stringBuilder.toString();
    }

    private String insertAtRandom(String string){
        int n = string.length();
        int index = new Random().nextInt(n);
        int ran = new Random().nextInt(pool.length());
        StringBuilder stringBuilder = new StringBuilder(string);
        stringBuilder.insert(index, pool.charAt(ran));
        return stringBuilder.toString();
    }

    private String replaceAtRandom(String string){
        int n = string.length();
        int index = new Random().nextInt(n);
        int ran = new Random().nextInt(pool.length());
        StringBuilder stringBuilder = new StringBuilder(string);
        stringBuilder.replace(index, index + 1, Character.toString(ran));
        return stringBuilder.toString();
    }

    private ArrayList<String> generateColumns(){
        // random no. of max 10 columns generated
        int n = new Random().nextInt(maxCol);
        ArrayList<String> cols = new ArrayList<String>();
        for (int i = 0; i<n; i++){
            String str = generateStringRandomly();
            cols.add(randomlyMutate(str));
        }
        return cols;
    }

    private int randomlyMutate(int number){
        // 5& chance of mutating
        int index = new Random().nextInt(18);

        switch (index){
            // decrement
            case 0:
                return number--;
            // increment
            case 1:
                return number++;
            default:
                return number;
        }
    }

    private String randomlyMutate(String str, int b){
        int index = new Random().nextInt(b);

        switch (index){
            // randomly delete
            case 0:
                return deleteAtRandom(str);
            
            // randomly insert
            case 1:
                return insertAtRandom(str);

            // randomly replace
            case 2:
                return replaceAtRandom(str);

            default:
                return str;
        }
    }
}
