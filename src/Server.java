package src;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
    
    private final static int PORT = 8080;
    private static Socket clientSocket = null;
    private static ServerSocket serverSocket = null;
    public static void main (String [] args ) throws IOException {
      
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("FileShare Server started at port " + PORT);
            File file = new File("server");
            boolean bool = file.mkdir();
        } catch (Exception e) {
            System.err.println("Port already in use.");
            System.exit(1);
        }

        Scanner in = new Scanner(System.in);
        String quit;
        /*
        quit = in.next();
        
        if(quit.equals("Q") || quit.equals("q")){
            serverSocket.close();
            System.out.println("FileShare Server stopped");
            in.close();
            System.exit(0);
        }*/
               
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
        }

        

    }
}