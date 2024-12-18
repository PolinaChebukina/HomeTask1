package com.home.HomeTask2;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class CollatzParallel {

    public static void main(String[] args) throws InterruptedException {
        int numberOfThreads = 4; // Кількість потоків задається вручну
        int maxNumber = 10_000; // Генеруємо числа (зменшимо для тестування)

        BlockingQueue<Integer> taskQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Integer> resultsQueue = new LinkedBlockingQueue<>();

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        long startTime = System.currentTimeMillis();

        // Завдання для додавання чисел у чергу
        Runnable producerTask = () -> {
            try {
                for (int i = 1; i <= maxNumber; i++) {
                    taskQueue.put(i);
                }
                for (int i = 0; i < numberOfThreads; i++) {
                    taskQueue.put(-1); // Маркер завершення
                }
                System.out.println("Усі числа додані до черги.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // Завдання для обчислення кроків гіпотези Колаца
        Runnable consumerTask = () -> {
            try {
                while (true) {
                    int number = taskQueue.take();
                    if (number == -1) break; // Завершення задачі
                    int steps = computeCollatzSteps(number);
                    resultsQueue.put(steps);

                    // Лог для відслідковування прогресу
                    if (number % 1000 == 0) {
                        System.out.println("Оброблено число: " + number);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // Запуск потоків
        executor.submit(producerTask);
        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(consumerTask);
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        // Підрахунок середньої кількості кроків
        double averageSteps = calculateAverageSteps(resultsQueue);

        long endTime = System.currentTimeMillis();
        System.out.println("Середня кількість кроків для виродження в 1: " + averageSteps);
        System.out.println("Час виконання: " + (endTime - startTime) + " мс");
    }

    // Метод для обчислення кількості кроків за гіпотезою Колаца
    private static int computeCollatzSteps(int n) {
        int steps = 0;
        while (n != 1) {
            if (n % 2 == 0) {
                n /= 2;
            } else {
                n = 3 * n + 1;
            }
            steps++;
        }
        return steps;
    }

    // Метод для підрахунку середньої кількості кроків
    private static double calculateAverageSteps(BlockingQueue<Integer> resultsQueue) {
        long totalSteps = 0;
        int count = 0;
        while (!resultsQueue.isEmpty()) {
            totalSteps += resultsQueue.poll();
            count++;
        }
        System.out.println("Оброблено результатів: " + count);
        return (double) totalSteps / count;
    }
}
