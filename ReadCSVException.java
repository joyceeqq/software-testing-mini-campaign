public class ReadCSVException extends Exception{
    public ReadCSVException(String msg){
        super("Error: " + msg);
    }
}
