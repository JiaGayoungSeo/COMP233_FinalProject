package com.company;

import java.io.DataOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public abstract class Service {

    private DataOutputStream responseWriter;

    public Service(DataOutputStream responseWriter){
        this.responseWriter = responseWriter;
    }

    public DataOutputStream getResponseWriter() {
        return responseWriter;
    }

    public void setResponseWriter(DataOutputStream responseWriter) {
        this.responseWriter = responseWriter;
    }

    public abstract void doWork();
}

class SQLSelectService extends Service{
    private String requestString;
    private String SQLCommand;

    public SQLSelectService(DataOutputStream responseWriter, String requestString){
        super(responseWriter);
        this.requestString = requestString;
    }
/*
    public void getSQLCommand() {
        String sqlcommand = "";
        String s = getRequestString();
        String criteria = s.substring(24, s.indexOf("Field"));

        if (criteria.equals("&")) {
            criteria = "'%'";
        } else {
            criteria = String.format("%s", criteria);
            criteria = criteria.substring(0, criteria.indexOf("&"));
            criteria = "'%" + criteria + "%'";
        }
        //When we are using s.indexOf("something") its index has to be added to number
        //of letters in order to get index of last letter in "somehting"
        String field = s.substring(s.indexOf("Field=") + 6, s.indexOf("&Submit"));
        System.out.println(field);

        sqlcommand = String.format("select * from employee where %s like %s", field, criteria);
        return SQLCommand;
    }
*/
/*
    public void setSQLCommand(String requestString){

        String sqlcommand = "";
        String s = requestString;
        String criteria = s.substring(24, s.indexOf("Field"));

        if (criteria.equals("&")) {
            criteria = "'%'";
        } else {
            criteria = String.format("%s", criteria);
            criteria = criteria.substring(0, criteria.indexOf("&"));
            criteria = "'%" + criteria + "%'";
        }
        //When we are using s.indexOf("something") its index has to be added to number
        //of letters in order to get index of last letter in "somehting"
        String field = s.substring(s.indexOf("Field=") + 6, s.indexOf("&Submit"));
        System.out.println(field);

        sqlcommand = String.format("select * from employee where %s like %s", field, criteria);
        this.SQLCommand = sqlcommand;
    }
*/
    public void setSQLCommand(String requestString){
        int startCriteria = requestString.indexOf("Criteria");
        requestString.substring(startCriteria+6,requestString.indexOf("&Field")-1);
    }


    @Override
    public void doWork() {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstm = null; //SQL 명령어 나타내는 객체
        ResultSet rset = null; //Query를 날리면(select문을 실행하면) 리턴되는 값을 담을 객체

        try{
            //call setSQLCommand
            setSQLCommand (requestString);
            System.out.println("This is sql command"+SQLCommand);

            //connect to an Oracle database
            conn = DBConnection.getConnection ();

            //pstm = conn.prepareStatement (SQLCommand);

            //executeQuery and get resultset
            //rset = pstm. executeQuery ();

            SQLCommand = "Select * From employee where FirstName = 'Kelly'";

            stmt = conn.createStatement ();
            stmt.executeQuery ( SQLCommand );
            // rset = pstm.executeQuery();
            rset =stmt.getResultSet ();

            while(rset.next()){
                System.out.println(rset.getString(1));
            }

            //Set up the Web page
            super.getResponseWriter ().writeBytes ( "<html><head><title>test" );
            super.getResponseWriter ().writeBytes ( "</title></head><body>" );

            /*
            responseWriter.writeBytes("<html><head><title>test");
            responseWriter.writeBytes("</title></head><body>" );
             */

            //Loop through the resultset writing it to IE using the reponseWriter.
            //You will have to format the Strings with a little HTML

            while (rset.next ()){
                super.getResponseWriter ().writeBytes (rset.toString ());
            }

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