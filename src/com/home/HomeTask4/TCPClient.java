package com.home.HomeTask4;

import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Адреса сервера
        int port = 12345; // Порт сервера

        try (Socket socket = new Socket(serverAddress, port)) {
            System.out.println("Підключено до сервера.");

            // Відправлення повідомлення серверу
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String message = "IASA!";
                out.println(message);
                System.out.println("Надіслано повідомлення: " + message);

                // Отримання відповіді від сервера
                String response = in.readLine();
                System.out.println("Отримано відповідь від сервера: " + response);
            }
        } catch (IOException e) {
            System.err.println("Помилка клієнта: " + e.getMessage());
        }
    }
}
