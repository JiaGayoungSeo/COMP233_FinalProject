package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer extends JFrame {
    private ServerSocket requestListener;

    private static JTextArea textArea;
    private JButton start;
    private JButton stop;
    private ExecutorService responses;
    private Scrollbar scrollbar;
    private JScrollPane txtScroll;


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


        txtScroll = new JScrollPane(textArea);
        txtScroll.setSize(500,500);
        txtScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        // txtScroll.add(textArea);
        add(txtScroll, BorderLayout.CENTER);

        /*
        scrollbar = new Scrollbar(Scrollbar.VERTICAL,50,20,0,50);
        add(scrollbar,layout.CENTER);
        scrollbar.setSize(15,100);
        scrollbar.setLocation(30,30);
        textArea.add(scrollbar);
        textArea.setVisible(true);
        */




        //set background color
        textArea.setBackground(new Color(249,231,159));

        //set font
        textArea.setFont(font);
        //textArea.add(txtScroll);

        Panel buttonContainer = new Panel();
        buttonContainer.add(start);
        buttonContainer.add(stop);

        add(buttonContainer,BorderLayout.SOUTH);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getConnection();
            }
        });

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getDisconnect();
            }
        });
    }

    public void start(){
        while(true){
            try{
                Responder r = new Responder(requestListener.accept(), this);
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

    public void getDisconnect(){
        try{
            System.out.println("Connection is lost");
            requestListener.close();
        }catch (Exception e){

        }
    }


    public void writeToArea(String s){
        textArea.append(s);
        txtScroll.getVerticalScrollBar().setValue(txtScroll.getVerticalScrollBar().getMaximum());
    }

    public static void main(String[] args) throws Exception {
        WebServer myServer = new WebServer();
        myServer.start();
    }

}