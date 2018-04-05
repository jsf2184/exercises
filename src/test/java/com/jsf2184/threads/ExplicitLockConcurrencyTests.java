package com.jsf2184.threads;

import com.jsf2184.utility.Utility;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;


public class ExplicitLockConcurrencyTests {

    public static  abstract class Person {

        String _name;

        public Person(String name) {
            _name = name;
        }

        public abstract void bow(Person recipient);

        public abstract void bowBack(Person recipient);

        protected void bowInternal(Person other) {
            System.out.printf("My name is %s and I am bowing to %s\n", _name, other._name);
            other.bowBack(this);
            System.out.printf("My name is %s I have completed a bowing sequence with %s\n", _name, other._name);
        }

        protected void bowBackInternal(Person other) {
            System.out.printf("My name is %s and I am bowing back to %s\n", _name, other._name);
        }
    }

    public static class BowThread extends Thread {
        Person _originator;
        Person _recipient;
        private int _num;
        private int _successes;

        public BowThread(Person originator, Person recipient, int num) {
            _originator = originator;
            _recipient = recipient;
            _num = num;
        }

        @Override
        public void run() {
            for (_successes = 0; _successes < _num; _successes++) {
                Utility.sleep(2);
                _originator.bow(_recipient);
            }
        }

        public int getSuccesses() {
            return _successes;
        }
    }

    public static class DlockPerson extends  Person {
        public DlockPerson(String name) {
            super(name);
        }

        @Override
        public synchronized void bow(Person other) {
            bowInternal(other);
        }

        @Override
        public synchronized void bowBack(Person other) {
            bowBackInternal(other);
        }
    }

    public static class SafePerson extends  Person {

        Lock _lock;

        public SafePerson(String name) {
            super(name);
            _lock = new ReentrantLock(false);
        }

        @Override
        public void bow(Person other) {
            SafePerson person2 = (SafePerson) other;
            while (!acquireLocks(person2)) {
                Utility.sleep(2);
            }
            try {
                bowInternal(other);
            } finally {
                _lock.unlock();
                person2._lock.unlock();
            }
        }

        @Override
        public void bowBack(Person other) {

            bowBackInternal(other);
        }

        public boolean acquireLocks(SafePerson other) {
            boolean gotMine = false;
            boolean gotHis = false;
            boolean res;
            try {
                gotMine = _lock.tryLock();
                gotHis = other._lock.tryLock();

            } finally {

                if (gotMine && gotHis) {
                    res = true;
                } else {
                    res = false;
                    if (gotMine) {
                        _lock.unlock();
                    }
                    if (gotHis) {
                        other._lock.unlock();
                    }
                }
            }
            return res;
        }
    }


    @Test
    public void testOneBowWithNoThreading() {
        Person alex = new DlockPerson("Alex");
        Person zack = new DlockPerson("Zack");
        alex.bow(zack);
    }

    @Test
    public void testDlockPersonWithThreading() {
        // Using a DlockPerson, we are going to deadlock.
        runWithThreading(DlockPerson::new, 1, false);
    }

    @Test
    public void testSafePersonWithThreading() {
        runWithThreading(SafePerson::new, 100, true);
    }

    public void runWithThreading(Function<String, Person> factory, int attempts, boolean expectSuccess) {
        Person alex = factory.apply("Alex");
        Person zack = factory.apply("Zack");
        BowThread alexThread = new BowThread(alex, zack, attempts);
        BowThread zackThread = new BowThread(zack, alex, attempts);
        alexThread.start();
        zackThread.start();
        try {
            alexThread.join(500);
            zackThread.join(500);
        } catch (Exception ignore) {
        }
        if (expectSuccess) {
            Assert.assertEquals(attempts, alexThread.getSuccesses());
            Assert.assertEquals(attempts, zackThread.getSuccesses());
        }
    }


}


