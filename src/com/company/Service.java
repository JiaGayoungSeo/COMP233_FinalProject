package com.company;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.DataOutputStream;
import java.sql.*;

public abstract class Service {

    DataOutputStream responseWriter;

    public Service(DataOutputStream responseWriter){
        this.responseWriter = responseWriter;
    }

    //public DataOutputStream getResponseWriter() {
        //return responseWriter;
   // }

    //public void setResponseWriter(DataOutputStream responseWriter) {
        //this.responseWriter = responseWriter;
    //}

    public abstract void doWork();
}

class SQLSelectService extends Service{
    private String requestString;
    private String SQLCommand;

    public SQLSelectService(DataOutputStream responseWriter, String requestString){
        super(responseWriter);
        this.requestString = requestString;
    }

    public void setSQLCommand(String requestString){
        //extract criteria
        int startCriteria = requestString.indexOf("Criteria");
        int lastCriteria = requestString.indexOf("&Field");
        String criteria = requestString.substring(startCriteria+9, lastCriteria);

        //extract field
        int startField = requestString.indexOf("Field");
        int lastField = requestString.indexOf("&Submit");
        String field = requestString.substring(startField+6, lastField);

        if(field.equals("FirstName")||field.equals("LastName")){
            criteria ="'"+criteria+"'";
        }

        this.SQLCommand = "Select * From employee Where "+field+"="+criteria;
    }


    @Override
    public void doWork() {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstm = null; //SQL 명령어 나타내는 객체
        ResultSet rset = null; //Query를 날리면(select문을 실행하면) 리턴되는 값을 담을 객체
        ResultSetMetaData rsmd = null;

        try{
            //call setSQLCommand
            setSQLCommand (requestString);
            System.out.println("This is sql command "+SQLCommand);

            //connect to an Oracle database
            conn = DBConnection.getConnection ();

            //pstm = conn.prepareStatement (SQLCommand);

            //executeQuery and get resultset
            //rset = pstm. executeQuery ();

            //SQLCommand = "Select * From employee where FirstName = 'Kelly'";

            stmt = conn.createStatement ();
            stmt.executeQuery ( SQLCommand );
            //rset = pstm.executeQuery();
            rset =stmt.getResultSet ();
            rsmd = rset.getMetaData();

            while(rset.next()) {
                for(int i=0;i<rsmd.getColumnCount();i++)
                System.out.print(rset.getString(i+1)+" ");
                System.out.println();
            }

            //Set up the Web page
            responseWriter.writeBytes("<html><head><title>test page ");
            responseWriter.writeBytes ( "</title></head><body>" );

            /*
            responseWriter.writeBytes("<html><head><title>test");
            responseWriter.writeBytes("</title></head><body>" );
             */

            //Loop through the resultset writing it to IE using the reponseWriter.
            //You will have to format the Strings with a little HTML



        }catch (Exception e){
            e.printStackTrace ();
        }finally {
            try{
                rset.close ();
                pstm.close ();
                conn.close ();
            } catch (Exception e){

            }

        }
    }
}