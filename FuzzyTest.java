import java.io.File;

public class FuzzyTest {
    public static void main(String[] args){
        Fuzzer fuzzer = new Fuzzer();
        if (args.length > 0){
            try{
                int repeat = Integer.parseInt(args[0]);
                fuzzer = new Fuzzer(repeat);
            } catch(NumberFormatException e){
                System.out.println("Invalid argument");
                System.exit(1);
            }
        }

        new File("./sample/fuzz").mkdir();

        fuzzer.run();
    }
}
