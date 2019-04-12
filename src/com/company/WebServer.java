package com.company;

import javax.swing.*;
import java.awt.*;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer extends JFrame {
    private ServerSocket requestListener;

    private JTextArea textArea;
    private JButton start;
    private JButton stop;
    private static int HTTP_PORT;
    private ExecutorService responses;
    private FlowLayout flowLayout;

    public WebServer(){
        super("Server");
        //set dimensions
        setSize(500,500);
        //tell it to appear
        setVisible(true);


        ////determines the layout
        BorderLayout layout = new BorderLayout();
        setLayout( layout );

        //create a font object
        Font font = new Font("Helvetica",Font.PLAIN,15);


        //add some controls
        textArea = new JTextArea();
        start = new JButton("Start");
        stop = new JButton("Stop");
        add(textArea, BorderLayout.CENTER);


        //set background color

        textArea.setBackground(new Color(249,231,159));

        //set font
        textArea.setFont(font);


        Panel buttonContainer = new Panel();
        buttonContainer.add(start);
        buttonContainer.add(stop);

        //chatInputContainer.add(chatInput);
        //chatInputContainer.add(chatSend);

        add( buttonContainer,BorderLayout.SOUTH);
/*
        chatSend.addActionListener(

                new ActionListener(){

                    public void actionPerformed( ActionEvent ae){
                        sendData(chatInput);
                    }

                    private void sendData(JTextArea out){

                        try{
                            output.writeObject(out.getText());
                            out.setText("");
                        }
                        catch( Exception e){
                            System.out.println("Oops! : "+e.toString() );
                        }
                    }
                }
        );
*/
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

    public int getPortNumber(){
        int port;

        while (true){
            String input = JOptionPane.showInputDialog("Enter a port number");

            if(input.equals("12346")){
                port = Integer.parseInt(input);
                break;
            }
        }
        return port;
    }

    public void getConnection(){
        try{
            requestListener = new ServerSocket(getPortNumber());
            responses = Executors.newFixedThreadPool(100);
        }catch (Exception e){

        }

    }



    public static void main(String[] args){
        WebServer myServer = new WebServer();
        myServer.getConnection();
        myServer.start();
    }

}