package com.home.HomeTask8;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatrixServer {

    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");
                executorService.execute(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())
        ) {
            // Receive dimensions
            int N = input.readInt();
            int M = input.readInt();
            int L = input.readInt();

            // Validate dimensions
            if (M <= 0 || N <= 0 || L <= 0) {
                output.writeUTF("Invalid matrix dimensions");
                clientSocket.close();
                return;
            }

            // Receive matrix A
            int[][] matrixA = new int[N][M];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    matrixA[i][j] = input.readInt();
                }
            }

            // Receive matrix B
            int[][] matrixB = new int[M][L];
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < L; j++) {
                    matrixB[i][j] = input.readInt();
                }
            }

            System.out.println("Received matrices: A(" + N + "x" + M + "), B(" + M + "x" + L + ")");

            // Perform matrix multiplication
            int[][] resultMatrix = multiplyMatrices(matrixA, matrixB);

            // Send result matrix back to client
            for (int[] row : resultMatrix) {
                for (int value : row) {
                    output.writeInt(value);
                }
            }

            System.out.println("Result matrix sent to client.");
        } catch (IOException e) {
            System.err.println("Client handling exception: " + e.getMessage());
        }
    }

    private static int[][] multiplyMatrices(int[][] matrixA, int[][] matrixB) {
        int rows = matrixA.length;
        int cols = matrixB[0].length;
        int common = matrixA[0].length;

        int[][] result = new int[rows][cols];
        ExecutorService executor = Executors.newFixedThreadPool(rows);

        for (int i = 0; i < rows; i++) {
            final int row = i;
            executor.execute(() -> {
                for (int j = 0; j < cols; j++) {
                    for (int k = 0; k < common; k++) {
                        result[row][j] += matrixA[row][k] * matrixB[k][j];
                    }
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all tasks to finish
        }

        return result;
    }
}
