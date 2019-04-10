package com.company;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.Socket;
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

    public Responder(Socket requestHandler){
        this.requestHandler = requestHandler;
    }

    @Override
    public void run() {

        try{
            requestReader = new Scanner(
                    new InputStreamReader (requestHandler.getInputStream()));


            int lineCount = 0;

            do{
                lineCount++; //This will be used later
                HTTPMessage = requestReader.nextLine();

                if (lineCount ==1){
                    requestedFile = "WebRoot\\"
                            + HTTPMessage.substring(5,HTTPMessage.indexOf("HTTP/1.1")-1);
                }

                System.out.println(HTTPMessage);
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
                    pageReader = new Scanner(file);
                }

                pageWriter = new DataOutputStream(
                        requestHandler.getOutputStream());

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
}