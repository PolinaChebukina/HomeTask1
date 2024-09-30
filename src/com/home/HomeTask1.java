package com.home;

public class HomeTask1 {

    public static void main(String[] args) {
        // Створюємо два потоки
        Thread thread1 = new Thread(new CalculationTask(1));
        Thread thread2 = new Thread(new CalculationTask(2));

        // Запускаємо обидва потоки
        thread1.start();
        thread2.start();

        System.out.println("Паралельні обчислення почались!");
    }
}

// Клас, який реалізує обчислювальну задачу
class CalculationTask implements Runnable {
    private int taskNumber;

    public CalculationTask(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void run() {
        // Імітація обчислень
        for (int i = 1; i <= 5; i++) {
            System.out.println("Задача " + taskNumber + " виконує обчислення: " + (i * taskNumber));
            try {
                // Затримка для імітації часу обчислення
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Задача " + taskNumber + " завершена.");
    }
}
