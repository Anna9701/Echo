package com.annawyrwal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("<host name> <port number> required!");
            System.exit(-1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        EchoClient echoClient = new EchoClient(hostName, portNumber);
        echoClient.makeMessagesReciveEcho();
        echoClient.Disconnect();
    }
}

class EchoClient {
    private Socket echoSocket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;

    public EchoClient(String hostName, int portNumber) {
        try {
            echoSocket = new Socket(hostName, portNumber);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.err.println("Open connection failed: " + e.getMessage());
            System.exit(-1);
        }
    }


    public void makeMessagesReciveEcho() {
        String userInput;

        try {
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("Echo from server: " + in.readLine());
            }
        } catch (IOException e) {
            System.err.println("IO error " + e.getMessage());
            return;
        }
    }

    public void Disconnect() {
        try {
            echoSocket.close();
            out.close();
            in.close();
            stdIn.close();
        } catch (IOException e) {
            System.err.println("Error while disconnecting: " + e.getMessage());
            System.exit(-1);
        }
    }

}
