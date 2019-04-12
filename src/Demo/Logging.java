package Demo;


import javax.imageio.IIOException;
import java.io.FileWriter;
import java.io.IOException;

public class Logging {
    private String request;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public Logging(String request){
        this.request = request;

        FileWriter fw = null;
        try{
            fw = new FileWriter("23233.txt");
            fw.write(request);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(fw!=null){
                    fw.close();
                }
            }catch (IOException e){

            }

        }
    }

}
