/***
 * This class is responsible for creating a client that can communicate with a server at a specific IP address and port.
 * Interaction with the Client is via the Command Line, with the extraction of the relevant arguments occurring in the main method. 
 * The class has a number of private data members required for its functionality.
 * The class creates a socket with the specified address and port, and after obtaining the InputStream of the server, communicates by sending 
 * simple messages to the server. 
 * Three possible operations are possible on the client side: Upload, Download and Query a File List.
 * All errors are correctly handled, and the program gracefully exits after printing an appropriate message.
 * @version 1.00
 */

package src;

import java.io.*;
import java.net.*;

public class Client {

    private static Socket socket;
    private static int port;
    private static String operation;
    private static Protocol protocol;
    private static PrintStream os;
    private static BufferedReader in = null;

    public static void main(String[] args) throws IOException {

        if (args.length < 3) {
            System.out.println("Incorrect number of arguments!");
        } else {
            port = Integer.parseInt(args[1]);
            operation = args[2];
            InetAddress address = InetAddress.getByName(args[0]);
            try {
                socket = new Socket(address, port);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                os = new PrintStream(socket.getOutputStream());
                protocol = new Protocol(socket, in, os);
            } catch (Exception e) {
                System.err.println("Cannot connect to the server, try again later.");
                System.exit(1);
            }


            System.out.println("FileShare Application");
            System.out.println("=====================");
            System.out.println("Server IP Address: " + address);
            System.out.println("Server Port: " + port);
            if(!operation.equals("-l")){
                String fileName = args[3];
                switch (operation) {
                    case "-u":
                            System.out.println("Upload Requested: " + fileName);
                            System.out.println("=====================");
                            sendMessage("CMD|1|" + socket.getInetAddress() + "|" + socket.getPort(),"INITIATE UPLOAD");
                            protocol.sendFile(new File(fileName));
                            break;
                    case "-d":
                            System.out.println("Download Requested: " + fileName);
                            System.out.println("=====================");
                            sendMessage("CMD|2|" + socket.getInetAddress() + "|" + socket.getPort(),"INITIATE DOWNLOAD");
                            sendMessage("DAT|2|" + socket.getInetAddress() + "|" + socket.getPort(),fileName);
                            protocol.receiveFile(fileName);
                            break;
                }
            }
            else{
                System.out.println("Server Query Requested: ");
                System.out.println("=====================");
                sendMessage("CMD|3|" + socket.getInetAddress() + "|" + socket.getPort(),"INITIATE QUERY");
                protocol.listFiles();
            }
            createMessage("CMD|0|" + socket.getInetAddress() + "|" + socket.getPort(),"CONNECTION TERMINATED");
            socket.close();

            
    }
    }

    public static void sendMessage(String header, String body){
        Message m = new Message(header,body);
        os.println(m.getHeader());
        os.println(m.getBody());
        os.flush();
    }

    public static void createMessage(String header, String body){
        Message m = new Message(header,body);
    }


    
}