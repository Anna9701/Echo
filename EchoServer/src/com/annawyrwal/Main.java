package com.annawyrwal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("You should pass <port number>");
            System.exit(-1);
        }

        int portNumber = Integer.parseInt(args[0]);
        new Listener(portNumber).run();
    }
}

class Listener extends Thread {
    private int portNumber;
    private ServerSocket serverSocket;

    public Listener(int number) {
        portNumber = number;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("New client connected: " + client.getInetAddress().getHostName());
                new MessageExchange(client).start();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

class MessageExchange extends  Thread {
    Socket client;
    PrintWriter out;
    BufferedReader in;

    public MessageExchange(Socket socket) {
        client = socket;
    }

    public void run() {
        try {

            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (true) {
                String msg = in.readLine();
                out.println(msg);
            }
        } catch (IOException e) {
            System.err.println("Client " + client.getInetAddress().getHostName() + " disconnected");
            try {
                client.close();
            } catch (IOException e1) {
                System.err.println("Closing connection error " + e1.getMessage());
                return;
            }
            return;
        }
    }
}
