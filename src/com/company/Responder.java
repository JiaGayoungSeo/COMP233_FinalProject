package com.company;

import Demo.Logging;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Responder implements Runnable{
    private Socket requestHandler;
    private Scanner requestReader;
    private Scanner pageReader;
    private DataOutputStream pageWriter;
    private String HTTPMessage;
    private String requestedURL;
    private String requestedFile;
    final static String DEFAULT = "WebRoot/Util/Error404.html";
    //static FileHandler fileHandler = new FileHandler("log.log",true);
    //static Logger  logger = Logger();
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public Responder(Socket requestHandler){
        this.requestHandler = requestHandler;
    }



    @Override
    public void run() {

        try{

            requestReader = new Scanner(
                    new InputStreamReader (requestHandler.getInputStream()));

            Logging logging;
            int lineCount = 0;
            String time = "";
            do{
                lineCount++; //This will be used later
                //HTTP 요청 헤더: 웹브라우저가 HTTP프로토콜을 이용해 요청 정보를 웹서버로 전송할때 부가적인 정보를 담아 전송
                HTTPMessage = requestReader.nextLine();//request


                if (lineCount ==1){
                    requestedFile = "WebRoot\\"
                            + HTTPMessage.substring(5,HTTPMessage.indexOf("HTTP/1.1")-1);
                    logging(time,HTTPMessage);
                }
                else{
                    logging(HTTPMessage);
                }

                System.out.println(HTTPMessage );


            } while(HTTPMessage.length() != 0);
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
                    //logger.log(Level.INFO,"error 404 page not found ");
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
    }

    public void logging(String timeStamp, String string) throws  IOException{
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        BufferedWriter bw = new BufferedWriter(new FileWriter("logging.txt",true));
        PrintWriter pw = new PrintWriter(bw,true);
        pw.write(timeStamp);
        pw.write(LINE_SEPARATOR);
        pw.write(string);
        pw.flush();
    }




}