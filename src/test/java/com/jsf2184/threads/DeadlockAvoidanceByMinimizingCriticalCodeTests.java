package com.jsf2184.threads;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiFunction;

public class DeadlockAvoidanceByMinimizingCriticalCodeTests {

    public static abstract class Account {
        String _name;
        double _balance;

        public Account(String name, double balance) {
            _name = name;
            _balance = balance;
        }

        @SuppressWarnings("SameParameterValue")
        abstract void pay(double amount, Account other);
        abstract void  receive(double amount);
        String getName() {
            return _name;
        }

        public double getBalance() {
            return _balance;
        }
    }

    public static class Account1 extends Account {
        public Account1(String name, double balance) {
            super(name, balance);
        }
        @Override
        public synchronized void pay(double amount, Account other) {
            _balance -= amount;
            System.out.printf("Account1 %s: paying %f to %s and now have %f\n", _name, amount, other.getName(), _balance);
            other.receive(amount);
        }
        @Override
        public synchronized void receive(double amount) {
            _balance += amount;
            System.out.printf("Account1 %s: received %f and now have %f\n", _name, amount, _balance);
        }
    }

    public static class Account2 extends Account {
        public Account2(String name, double balance) {
            super(name, balance);
        }
        @Override
        public void pay(double amount, Account other) {
            synchronized (this) {
                _balance -= amount;
                System.out.printf("Account1 %s: paying %f to %s and now have %f\n", _name, amount, other.getName(), _balance);
            }
            other.receive(amount);
        }
        @Override
        public synchronized void receive(double amount) {
            _balance += amount;
            System.out.printf("Account1 %s: received %f and now have %f\n", _name, amount, _balance);
        }
    }



    @Test
    public void testDeadlock1() {
        testDeadlock(Account1::new, false);
    }

    @Test
    public void testDeadlock2() {
        testDeadlock(Account2::new, true);
    }

    public void testDeadlock(BiFunction<String, Double, Account> factory, boolean shouldWork) {
        Account alex = factory.apply("alex", 100.0);
        Account zack = factory.apply("zack", 100.0);
        Thread alexThread = new Thread(() -> alex.pay(10.0, zack));
        Thread zackThread = new Thread(() -> zack.pay(10.0, alex));

        alexThread.start();
        zackThread.start();

        try {
            alexThread.join(1000);
            zackThread.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (shouldWork) {
            Assert.assertEquals(100.0, alex.getBalance(), .0001);
            Assert.assertEquals(100.0, zack.getBalance(), .0001);
        }
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignore) {

        }
    }
}
