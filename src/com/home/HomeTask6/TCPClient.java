package com.home.HomeTask6;

import java.io.*;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(hostname, port)) {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            for (int i = 0; i < 100; i++) {
                // Create and send message
                String message = "Hello Server, message #" + (i + 1);
                byte[] messageBytes = message.getBytes();
                output.writeInt(messageBytes.length);
                output.write(messageBytes);

                // Read server response
                int responseLength = input.readInt();
                byte[] responseBytes = new byte[responseLength];
                input.readFully(responseBytes);
                String serverResponse = new String(responseBytes);
                System.out.println("Received from server: " + serverResponse);
            }

            socket.close();
            System.out.println("Client finished communication");

        } catch (IOException ex) {
            System.out.println("Client exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
