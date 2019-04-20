package com.company;


import java.io.*;
import java.sql.*;
import java.util.Scanner;

public abstract class Service {

    DataOutputStream responseWriter;

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
        String result = "Result Not Found";
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
            responseWriter.writeBytes(writePage());

            while(rset.next()) {
                String query= "";
                for(int i=1;i<rsmd.getColumnCount()+1;i++){
                    query += rset.getString(i)+" ";
                    System.out.print(rset.getString(i));
                    System.out.println();
                }
                responseWriter.writeBytes(query +
                        "</p>\n" +
                        "</body>\n" +
                        "\n" +
                        "</html>");
                responseWriter.flush();
                result ="";
            }

            responseWriter.writeBytes(result +
                    "</p>\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>");
            responseWriter.flush();

            System.out.print(result);
            //Set up the Web page

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


    public String writePage(){
        return "<html>\n" +
                "\n" +
                "<head>\n" +
                "<title>Comp 233, Query</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<p align='center'><font face='Arial' size='13' color=\"#0000FF\">Service Page</font></p>\n" +
                "\n" +
                "<p>\n" +
                "<form action='doSERVICE' method='GET'>\n" +
                "&nbsp;\n" +
                "<table border=\"0\" width=\"100%\" id=\"table1\">\n" +
                "\t<tr>\n" +
                "\t\t<td>\n" +
                "\t\t<p align=\"right\"><font face=\"Arial\">Search Criteria</font></td>\n" +
                "\t\t<td>\n" +
                "<input type='text' name='Criteria' size='15'></td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>\n" +
                "\t\t<p align=\"right\"><font face=\"Arial\">First Name</font></td>\n" +
                "\t\t<td>\n" +
                "<input type='radio' name='Field' value='FirstName' checked></td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>\n" +
                "\t\t<p align=\"right\"><font face=\"Arial\">Last Name</font></td>\n" +
                "\t\t<td>\n" +
                "<input type='radio' name='Field' value='LastName'></td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>\n" +
                "\t\t<p align=\"right\"><font face=\"Arial\">Job Code</font></td>\n" +
                "\t\t<td>\n" +
                "<input type='radio' name='Field' value='JobCode'></td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>\n" +
                "\t\t<p align=\"right\"><font face=\"Arial\">Employee ID</font></td>\n" +
                "\t\t<td>\n" +
                "<input type='radio' name='Field' value='empid'></td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td colspan=\"2\">\n" +
                "\t\t<p align=\"center\">\n" +
                "<input type='Submit' name='Submit' value='Run Service'></td>\n" +
                "\t</tr>\n" +
                "</table>\n" +
                "<p><br>\n" +
                "<br>\n" +
                "<br>\n" +
                "<br>\n" +
                "&nbsp; </p>\n" +
                "</form>\n";
    }

}