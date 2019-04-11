package Demo;

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



}
