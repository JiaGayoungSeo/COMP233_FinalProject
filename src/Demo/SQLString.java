package Demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;

public class SQLString {

    public static void main(String[] args){

    String get = "/doSERVICE?Criteria=3000&Field=JobCode&Submit=Run+Service HTTP/1.1";
    int start = get.indexOf("Criteria");
    int last = get.indexOf("&Field");
    String str = get.substring(start+9,last);
    boolean jobcode = get.contains("FirstName");

    int start2 = get.indexOf("Field");
    int last2 = get.indexOf("&Submit");
    String string = get.substring(start2+6,last2);



    System.out.println(string);

    }
    /*
    public static void writeFile(String queryReseult){
        String start = "<html>"+"<head>\n" + "</head>\n" + "<body>";
        String end = "</body>\n" + "</html>";
        try{
            File file = new File ( "WebRoot\\"+queryReseult+".html" );
            BufferedWriter bufferedWriter = new BufferedWriter ( new FileWriter( file ) );
            bufferedWriter.write ( start );
            bufferedWriter.write ( queryReseult );
            bufferedWriter.write ( end );
            bufferedWriter.flush ();

        }catch (Exception e){

        }
    }
*/



}
