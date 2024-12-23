package com.home.HomeTask8;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class MatrixClient {

    public static void main(String[] args) {
        String serverHost = "localhost";
        int serverPort = 8080;

        try (Socket socket = new Socket(serverHost, serverPort)) {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            // Generate random dimensions for matrices
            Random random = new Random();
            int N = random.nextInt(1000) + 1000;
            int M = random.nextInt(1000) + 1000;
            int L = random.nextInt(1000) + 1000;

            System.out.println("Generated matrices: A(" + N + "x" + M + "), B(" + M + "x" + L + ")");

            // Generate matrices A and B
            int[][] matrixA = generateMatrix(N, M);
            int[][] matrixB = generateMatrix(M, L);

            // Send dimensions to the server
            output.writeInt(N);
            output.writeInt(M);
            output.writeInt(L);

            // Send matrix A
            for (int[] row : matrixA) {
                for (int value : row) {
                    output.writeInt(value);
                }
            }

            // Send matrix B
            for (int[] row : matrixB) {
                for (int value : row) {
                    output.writeInt(value);
                }
            }

            // Receive result matrix
            int[][] resultMatrix = new int[N][L];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < L; j++) {
                    resultMatrix[i][j] = input.readInt();
                }
            }

            System.out.println("Matrix multiplication result received.");
        } catch (IOException e) {
            System.err.println("Client exception: " + e.getMessage());
        }
    }

    private static int[][] generateMatrix(int rows, int cols) {
        Random random = new Random();
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(100);
            }
        }
        return matrix;
    }
}
