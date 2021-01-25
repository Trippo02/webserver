package com.mycompany.server_web;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Leonardo
 */
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mycompany.server_web.PuntiVendita;
import java.math.BigDecimal;
import java.io.*;
import java.net.*;
import java.util.*;

public class serverwebmain implements Runnable{ 	
	static final File WEB_ROOT = new File("../");
	static final String DEFAULT_FILE = "index.html";
	static final String FILE_NOT_FOUND = "404.html";
	static final String METHOD_NOT_SUPPORTED = "not_supported.html";
	//Assegno un numero alla porta del server
        static final int PORT = 8080;
        //Verbose mode
	static final boolean verbose = true;
        //Inizializzo il socket del client
	private Socket clientsocket;
        
	public serverwebmain(Socket c) {
		clientsocket = c;
	}
	
	public static void main(String[] args) {
            /*File directory = new File("./");
            System.out.println(directory.getAbsolutePath());*/
		try {
			ServerSocket serversocket = new ServerSocket(PORT);
			System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
			//Il server rimane sempre attivo
                        while (true) {
				serverwebmain serverweb = new serverwebmain(serversocket.accept());
				
                                if (verbose) {
					System.out.println("Connection opened. (" + new Date() + ")");
				}
                                
				Thread thread = new Thread(serverweb);
				thread.start();
			}
		} catch (IOException e) {
			System.err.println("Server Connection error : " + e.getMessage());
		}
	}
        
	@Override
	public void run() {
		// we manage our particular client connection
		BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
		String fileRequested = null;
		try {
			in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
			out = new PrintWriter(clientsocket.getOutputStream());
			dataOut = new BufferedOutputStream(clientsocket.getOutputStream());
			String input = in.readLine();
			StringTokenizer parse = new StringTokenizer(input);
			String method = parse.nextToken().toUpperCase();
			fileRequested = parse.nextToken().toLowerCase();
			
                        if (!method.equals("GET")  &&  !method.equals("HEAD")) {
                            
                            if (verbose) {
                                    System.out.println("501 Not Implemented : " + method + " method.");
                            }
                            
                            File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
                            int fileLength = (int) file.length();
                            String contentMimeType = "text/html";
                            byte[] fileData = readFileData(file, fileLength);
                            
                            out.println("HTTP/1.1 501 Not Implemented");
                            out.println("Server: Java HTTP Server from Trippodo : 1.0");
                            out.println("Date: " + new Date());
                            out.println("Content-type: " + contentMimeType);
                            out.println("Content-length: " + fileLength);
                            out.println();
                            out.flush();
                            
                            dataOut.write(fileData, 0, fileLength);
                            dataOut.flush();
                       
                        } else if(fileRequested.contains("punti-vendita.xml")){
                            
                            ObjectMapper mapper = new ObjectMapper();
                            PuntiVendita puntivendita = mapper.readValue(new File("Resources/puntiVendita.json"), PuntiVendita.class);
                            XmlMapper xmlMapper = new XmlMapper();
                            xmlMapper.writeValue(new File("puntiVendita.xml"), puntivendita);
                            File fileinxml = new File("puntiVendita.xml");
                            File file = new File(WEB_ROOT, FILE_NOT_FOUND);
                            int fileLength = (int) file.length();
                            String content = "application/xml";
                            String xml = xmlMapper.writeValueAsString(puntivendita);
                            byte[] fileData = readFileData(file, fileLength);
                            
                            out.println("HTTP/1.1 200 OK");
                            out.println("Location : " + fileRequested);
                            out.println("Server : Java HTTP Server from Trippodo : 1.0");
                            out.println("Date : " + new Date());
                            out.println("Content-type : " + content);
                            out.println("Content-length : " + fileLength);
                            out.println(); 
                            out.flush(); 
                            
                            out.write(xml);
                            out.flush();
                            dataOut.write(fileData, 0, fileLength);
                            dataOut.flush();
                            
                            if (verbose) {
                                    System.out.println("File " + fileRequested + " not found");
                            }
			} else {
                            // GET or HEAD method
                            if (fileRequested.endsWith("/db")) {
                                DatabaseSql databasesql = new DatabaseSql();
                                Vector v = new Vector();
                                v = databasesql.takevalue();
                                System.out.println("Elementi salvati : " + v.get(1) + " " + v.get(2));             
                                
                            }
                            else if (fileRequested.endsWith("/db/Utente")) {
                                DatabaseSql databasesql = new DatabaseSql();
                                Vector v = new Vector();
                                v = databasesql.takevalue();
                                
                                XmlMapper xmlMapper = new XmlMapper();
                                xmlMapper.writeValue(new File("DatabaseSqlxml.xml"), v);
                                File fileinxml = new File("DatabaseSql.xml");
                                File file = new File(WEB_ROOT, FILE_NOT_FOUND);
                                int fileLength = (int) file.length();
                                String content = "application/xml";
                                String xml = xmlMapper.writeValueAsString(v);
                                byte[] fileData = readFileData(file, fileLength);
                                    
                                out.println("HTTP/1.1 200 OK");
                                out.println("Server: Java HTTP Server from SSaurel : 1.0");
                                out.println("Date: " + new Date());
                                out.println("Content-type: " + content);
                                out.println("Content-length: " + fileLength);
                                out.println(); 
                                out.flush(); 
                                
                                out.write(xml);
                                out.flush();
                                dataOut.write(fileData, 0, fileLength);
                                dataOut.flush();
                            
                            if (verbose) {
                                    if (verbose) System.out.println("File " + fileRequested + " not found");
                            }	
                            }
                         if (fileRequested.endsWith("/resources/DatabaseSql")){
                            DatabaseSql databasesql = new DatabaseSql();
                            Vector v = new Vector();
                            v = databasesql.takevalue();
                            try {
                                File fileJSON = new File(WEB_ROOT, "dbjson.json");
                                ObjectMapper objectMapper = new ObjectMapper();
                                objectMapper.writeValue(new FileOutputStream("dbjson.json"), v);
                                File fileinjson = new File("dbjson.json");
                                int fileLength = (int) fileJSON.length();
                                byte[] fileData = readFileData(fileJSON, fileLength);

                                dataOut.write(fileData, 0, fileLength);
                                dataOut.flush();
                                
                                out.println("HTTP/1.1 200 OK");
                                out.println("Location: " + fileRequested);
                                out.println("Server: Java HTTP Server from SSaurel : 1.0");
                                out.println("Date: " + new Date());
                                out.println("Content-type: " + "application/xml");
                                out.println("Content-length: " + "application/json");
                                out.println(); // blank line between headers and content, very important !
                                out.flush(); // flush character output stream buffer

                                out.write(fileJSON.toString());
                                out.flush();

                                if (verbose) System.out.println("File " + fileRequested + " not found");
                            }catch (Throwable e){e.getMessage();} 
                            
                        } else {
                            // GET or HEAD method
                            if (fileRequested.endsWith("/")) {
                                    fileRequested += DEFAULT_FILE;
                            }

                            File file = new File(WEB_ROOT, fileRequested);
                            int fileLength = (int) file.length();
                            String content = getContentType(fileRequested);

                            if (method.equals("GET")) { // GET method so we return content
                                    byte[] fileData = readFileData(file, fileLength);
                                    
                                    // send HTTP Headers
                                    out.println("HTTP/1.1 200 OK");
                                    out.println("Server: Java HTTP Server from SSaurel : 1.0");
                                    out.println("Date: " + new Date());
                                    out.println("Content-type: " + content);
                                    out.println("Content-length: " + fileLength);
                                    out.println(); // blank line between headers and content, very important !
                                    out.flush(); // flush character output stream buffer

                                    dataOut.write(fileData, 0, fileLength);
                                    dataOut.flush();
                            }

                            if (verbose) {
                                    System.out.println("File " + fileRequested + " of type " + content + " returned");
                            }
				
			
                         }
                        }
                
		} catch (FileNotFoundException fnfe) {
			try {
				fileNotFound(out, dataOut, fileRequested);
			} catch (IOException ioe) {
				System.err.println("Error with file not found exception : " + ioe.getMessage());
			}
                        
		} catch (IOException ioe) {
			System.err.println("Server error : " + ioe);
		} finally {
			try {
				in.close();
				out.close();
				dataOut.close();
				clientsocket.close(); // we close socket connection
			} catch (Exception e) {
				System.err.println("Error closing stream : " + e.getMessage());
			} 
			
			if (verbose) {
				System.out.println("Connection closed.\n");
			}
		}
                        
        
	private byte[] readFileData(File file, int fileLength) throws IOException {
		FileInputStream fileIn = null;
		byte[] fileData = new byte[fileLength];
		
		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null) 
				fileIn.close();
		}
		return fileData;
	}
        
	private String getContentType(String fileRequested) {
		if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
			return "text/html";
		else
			return "text/plain";
	}
	
	private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
            if(!(fileRequested.endsWith("/") || fileRequested.endsWith(".html") || fileRequested.endsWith(".xml"))){
                File file = new File(WEB_ROOT, FILE_NOT_FOUND);
		int fileLength = (int) file.length();
		String content = "text/html";
		byte[] fileData = readFileData(file, fileLength);
		
                
		out.println("HTTP/1.1 301 Moved Permanently");
		out.println("Server: Java HTTP Server from SSaurel : 1.0");
		out.println("Date: " + new Date());
		out.println("Content-type: " + content);
		out.println("Content-length: " + fileLength);
                out.println("Location: " + fileRequested + "/");
		out.println(); // blank line between headers and content, very important !
		out.flush(); // flush character output stream buffer
		
		dataOut.write(fileData, 0, fileLength);
		dataOut.flush();
		
		if (verbose) {
			System.out.println("File " + fileRequested + " not found");
                }            
            }else{
                File file = new File(WEB_ROOT, FILE_NOT_FOUND);
		int fileLength = (int) file.length();
		String content = "text/html";
		byte[] fileData = readFileData(file, fileLength);
		
                
		out.println("HTTP/1.1 404 File Not Found");
		out.println("Server: Java HTTP Server from SSaurel : 1.0");
		out.println("Date: " + new Date());
		out.println("Content-type: " + content);
		out.println("Content-length: " + fileLength);
		out.println(); // blank line between headers and content, very important !
		out.flush(); // flush character output stream buffer
		
		dataOut.write(fileData, 0, fileLength);
		dataOut.flush();
		
		if (verbose) {
			System.out.println("File " + fileRequested + " not found");
		}
            }
	}
}
