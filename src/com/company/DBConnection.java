package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection dbConn;

    public static Connection getConnection(){
        Connection conn = null;
        try {
            String user = "CISTU030";
            String pw = "bekind";
            String url = "jdbc:oracle:thin:@bisoracle.siast.sk.ca:1521:ACAD";

            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(url, user, pw);

            System.out.println("Connected!");
        }catch (ClassNotFoundException cnfe) {
            System.out.println("Failed to load DB driver " + cnfe.toString());
        }catch (SQLException sqle){
            System.out.println("Failed to connect DB" + sqle.toString());
        }catch (Exception e){
            System.out.println("Unknown error");
            e.printStackTrace();
        }
        return conn;

    }
}