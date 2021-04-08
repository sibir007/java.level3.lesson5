package ru.geekbrains.java.level3.lesson5;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Car implements Runnable{
    private static int CARS_COUNT;
    private static boolean isWin = false;
    private static Lock lock = new ReentrantLock();
    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            MainClass.cdl1.countDown();
            MainClass.cb.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        try {
            lock.lock();
            if(!isWin) {
                isWin = true;
                System.out.println(this.name + "-WIN");
            }
        } finally {
            lock.unlock();
        }
        MainClass.cdl2.countDown();
    }

}
