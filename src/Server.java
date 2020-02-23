package src;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
    
    private final static int PORT = 9000;
    private static Socket clientSocket = null;
    private static ServerSocket serverSocket = null;
    public static void main (String [] args ) throws IOException {
      
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("FileShare Server started at port " + PORT);

            File file = new File("server");
            file.mkdir();
        } catch (Exception e) {
            System.err.println("Port already in use.");
            System.exit(1);
        }

        Scanner in = new Scanner(System.in);
               
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Accepted connection : " + clientSocket);

                Thread t = new Thread(new Connection(clientSocket));

                t.start();

            } catch (Exception e) {
                System.err.println("Error in connection attempt.");
                break;
            }
            finally{

            }
        }
        //TODO
        serverSocket.close();
        System.out.println("FileShare Server stopped");
        in.close();
        System.exit(0);
        

        

    }
}