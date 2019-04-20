package com.company;

import Demo.Logging;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;



public class Responder implements Runnable{
    private Socket requestHandler;
    private Scanner requestReader;
    private Scanner pageReader;
    private DataOutputStream pageWriter;
    private String HTTPMessage;
    private String requestedURL;
    private String requestedFile;
    final static String DEFAULT = "WebRoot/Util/Error404.html";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private WebServer ws;


    public Responder(Socket requestHandler, WebServer ws ){
        this.requestHandler = requestHandler;
        this.setWs(ws);
    }



    @Override
    public void run() {
        try{

            requestReader = new Scanner(
                    new InputStreamReader (requestHandler.getInputStream()));

            int lineCount = 0;
            String time = "";
            do{
                lineCount++; //This will be used later
                //HTTP 요청 헤더: 웹브라우저가 HTTP프로토콜을 이용해 요청 정보를 웹서버로 전송할때 부가적인 정보를 담아 전송
                setHTTPMessage(requestReader.nextLine());//request

                if (lineCount ==1){
                    requestedFile = "WebRoot\\"
                            + getHTTPMessage().substring(5, getHTTPMessage().indexOf("HTTP/1.1")-1);
                    logging(time, getHTTPMessage());
                }
                else{
                    logging(getHTTPMessage());
                }
                System.out.println(getHTTPMessage());

            } while(getHTTPMessage().length() != 0);
        } catch (Exception e){
            System.out.println(e.toString());
            System.out.println("\n");
            e.printStackTrace();;
        }

        try{

            pageWriter = new DataOutputStream(requestHandler.getOutputStream());
            pageWriter.flush();

            if(requestedFile.indexOf("doSERVICE")>-1){
                Service s = new SQLSelectService(pageWriter,requestedFile);
                s.doWork();
            }else {
                File file = new File(requestedFile);

                try{
                    pageReader = new Scanner (file);
                }catch (FileNotFoundException fnfe){
                    file = new File(DEFAULT);
                    logging("404Error - File Not Found Error");
                    logging("");
                    pageReader = new Scanner(file);
                }

                while (pageReader.hasNext()){
                    String s = pageReader.nextLine();
                    System.out.println(s);
                    pageWriter.writeBytes(s);
                }

                //tell the browser we're done writing to it.
                pageReader.close();
                pageWriter.close();
                requestHandler.close();
            }


        } catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public void logging(String string) throws IOException{

        BufferedWriter bw = new BufferedWriter(new FileWriter("logging.txt",true));
        PrintWriter pw = new PrintWriter(bw,true);
        pw.write(string);
        pw.write(LINE_SEPARATOR);
        pw.flush();
        getWs().writeToArea(string+"\n");

    }


    public void logging(String timeStamp, String string) throws  IOException{
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        BufferedWriter bw = new BufferedWriter(new FileWriter("logging.txt",true));
        PrintWriter pw = new PrintWriter(bw,true);
        pw.write(timeStamp);
        pw.write(LINE_SEPARATOR);
        pw.write(string);
        pw.flush();
        getWs().writeToArea(timeStamp+"\n"+string+"\n");
    }


    public String getHTTPMessage() {
        return HTTPMessage;
    }

    public void setHTTPMessage(String HTTPMessage) {
        this.HTTPMessage = HTTPMessage;
    }

    public WebServer getWs() {
        return ws;
    }

    public void setWs(WebServer ws) {
        this.ws = ws;
    }
}