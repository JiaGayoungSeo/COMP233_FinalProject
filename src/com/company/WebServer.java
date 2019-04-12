package com.company;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class WebServer {
    private ServerSocket requestListener;
    private static int HTTP_PORT = 12346;
    private ExecutorService responses;


    public WebServer(){
        try{
            requestListener = new ServerSocket(HTTP_PORT);
            responses = Executors.newFixedThreadPool(100);

        } catch (Exception e){

        }

    }

    public void start(){
        while(true){
            try{
                Responder r = new Responder(requestListener.accept());
                r.run();
            }catch (Exception e){

            }
        }
    }



    public static void main(String[] args){
        WebServer myServer = new WebServer();
        myServer.start();
    }

}