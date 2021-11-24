package server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {


    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;


    private void run(String portNumber) {


        System.out.println("Initializing HTTP Server");
        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        server.setExecutor(null);

        System.out.println("Creating contexts");
        server.createContext("/", new CommandHandler());
        System.out.println("Starting server");
        server.start();
        System.out.println("Server started");
    }

    // "main" method for the com.example.testingpurposes.server program
    // "args" should contain one command-line argument, which is the port number
    // on which the com.example.testingpurposes.server should accept incoming com.example.testingpurposes.client connections.
    public static void main(String[] args) {
        String portNumber = "8080";
        int delta = 10;
        try {
            ServerData.string1 = args[1];
            ServerData.string2 = args[2];
            ServerData.string3 = args[3];
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Integer.parseInt(args[0]) > 0) {
            delta = Integer.parseInt(args[0]);
        }

        try {
            ServerData data = ServerData.getInstance();
            data.loadDB();
            data.setDelta(delta);
        } catch (Exception e) {
            e.printStackTrace();
        }



        new Server().run(portNumber);
        System.out.println("Started on port: " + portNumber);
    }
}



