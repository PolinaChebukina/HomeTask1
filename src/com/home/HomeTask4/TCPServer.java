package com.home.HomeTask4;
import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        int port = 12345; // Порт, на якому працює сервер

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущено, очікування підключення...");

            while (true) {
                // Очікуємо підключення клієнта
                Socket clientSocket = serverSocket.accept();
                System.out.println("Підключено клієнта: " + clientSocket.getInetAddress());

                // Обробка повідомлень
                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String message = in.readLine();
                    System.out.println("Отримано повідомлення: " + message);

                    // Відповідь клієнту
                    String response = "Сервер отримав ваше повідомлення: " + message;
                    out.println(response);

                    System.out.println("Відповідь надіслано.");
                }
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Помилка сервера: " + e.getMessage());
        }
    }
}
