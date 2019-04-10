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

    public void setSQLCommand(){
        this.SQLCommand = "";
    }

    @Override
    public void doWork() {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstm = null; //SQL 명령어 나타내는 객체
        ResultSet rset = null; //Query를 날리면(select문을 실행하면) 리턴되는 값을 담을 객체

        try{
            //call setSQLCommand
            setSQLCommand ();

            //connect to an Oracle database
            conn = DBConnection.getConnection ();
            pstm = conn.prepareStatement (SQLCommand);
            //executeQuery and get resultset
            rset = pstm. executeQuery ();

            //stmt = conn.createStatement ();
            //stmt.executeQuery ( SQLCommand );
            // rset = pstm.executeQuery();
            //rset =stmt.getResultSet ();

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