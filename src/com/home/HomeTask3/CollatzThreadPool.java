package com.home.HomeTask3;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CollatzThreadPool {
    public static void main(String[] args) throws InterruptedException {
        int maxNumber = 10_000; // Зменшена кількість чисел для тестування
        int numberOfThreads = 4; // Кількість потоків

        AtomicInteger totalSteps = new AtomicInteger(0); // Атомарна змінна для підрахунку кроків
        AtomicInteger count = new AtomicInteger(0); // Атомарна змінна для підрахунку чисел

        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfThreads);

        long startTime = System.currentTimeMillis();

        // Завдання для обчислення
        for (int i = 1; i <= maxNumber; i++) {
            final int number = i; // Локальна копія для лямбда-виразу
            threadPool.submit(() -> {
                int steps = computeCollatzSteps(number);
                totalSteps.addAndGet(steps); // Атомарне додавання кроків
                count.incrementAndGet(); // Атомарне збільшення кількості чисел
            });
        }

        threadPool.shutdown(); // Завершення роботи пулу потоків
        threadPool.awaitTermination(1, TimeUnit.HOURS); // Очікування завершення всіх задач

        double averageSteps = (double) totalSteps.get() / count.get(); // Обчислення середнього

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
}
