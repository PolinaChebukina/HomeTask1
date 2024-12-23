package com.home.HomeTask6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            for (int i = 0; i < 100; i++) {
                // Read message length
                int length = input.readInt();

                // Read message payload
                byte[] messageBytes = new byte[length];
                input.readFully(messageBytes);
                String receivedMessage = new String(messageBytes);
                System.out.println("Received from client: " + receivedMessage);

                // Respond to client
                String responseMessage = "Message " + (i + 1) + " received";
                byte[] responseBytes = responseMessage.getBytes();
                output.writeInt(responseBytes.length);
                output.write(responseBytes);
            }

            socket.close();
            System.out.println("Server finished communication");

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
