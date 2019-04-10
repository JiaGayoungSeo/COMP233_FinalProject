package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection dbConn;

    public static Connection getConnection(){
        Connection conn = null;
        try{
            String user = " c##gseo";
            String pw = "bekind";
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";

            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(url,user,pw);

            System.out.println ( "Connected!" );
        }catch (ClassNotFoundException cnfe) {
            System.out.println("Failed to load DB driver" + cnfe.toString());
        }catch (SQLException sqle){
            System.out.println("Failed to connect DB" + sqle.toString());
        }catch (Exception e){
            System.out.println("Unknown error");
            e.printStackTrace();
        }
        return conn;

    }
}