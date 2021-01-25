/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.server_web;
/**
 *
 * @author Loanardo
 */
import java.sql.*;
import java.util.*;
public class DatabaseSql{
     public Vector takevalue()
    {
        Vector v = new Vector();
        Utente u = new Utente();
        //carico il driver
        System.setProperty("jdbc.drivers", "sun.jdbc.odbc.JdbcOdbcDriver");
        //nome e indirizzo database
        String UrlDatabase = "jdbc:mysql://localhost:3306/DatabaseSql?serverTimezone=UTC&useLegacyDatetimeCode=false";
        //definizione delle query
        String query = "SELECT nome, cognome FROM Utente";
        //stabilisco la connessione
        System.out.println("Connessione con: " + UrlDatabase);
        Connection connessione = null;
        try
        {
            connessione = DriverManager.getConnection(UrlDatabase, "root", "root");
        }
        catch(Exception e)
        {
            System.out.println("Errore nella connessione: " + e);
            System.exit(1);
        }
        try
        {
            //statement per database
            Statement statement = connessione.createStatement();
            //interrogo il database con una query
            ResultSet resultset = statement.executeQuery(query);
            while (resultset.next()) {
              for (int i = 1; i <= 2; i++){
                 v.add(resultset.getString(i));
              }
            }
        }
        catch(Exception e)
        {
            System.out.println("Errore: " + e);
            System.exit(1);
        }
        finally{
            if(connessione != null)
            {
                try
                {
                    connessione.close();  //chiusura connessione
                }
                catch(Exception e)
                {
                    System.out.println("Errore nella chiusura della connessione: " + e);
                }
            }
        }
       return v;
    }
}