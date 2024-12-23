package com.home.HomeTask5;

import java.io.*;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(hostname, port)) {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // Sending messages to the server
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String text;

            System.out.println("Connected to the server. Type your messages:");

            do {
                System.out.print("You: ");
                text = consoleReader.readLine();
                writer.println(text);

                String serverResponse = reader.readLine();
                System.out.println(serverResponse);

            } while (!text.equalsIgnoreCase("bye"));

        } catch (IOException ex) {
            System.out.println("Client exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
