package com.jsf2184.mlm;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StaticSingletonPlay {

    public static class MyClass {

        private static MyClass singleton1 = new MyClass("singleton1 from static declaration");
        private static MyClass singleton2;
        private static MyClass singleton3;
        private static MyClass singleton4;
        private static volatile MyClass singleton5;

        private MyClass(String msg) {
            System.out.printf("In the constructor of MyClass w/ msg=%s\n", msg);
        }

        static {
            singleton2 = new MyClass("singleton2 from static block");
        }

        static MyClass getSingleton3NoSync() {
            if (singleton3 == null) {
                singleton3 = new MyClass("singleton3 from getSingleton3NoSync method");
            }
            return singleton3;
        }
        static synchronized MyClass getSingleton4StdSync() {
            if (singleton4 == null) {
                singleton4 = new MyClass("singleton4 from getSingleton4StdSync method");
            }
            return singleton4;
        }

        static MyClass getSingleton5DoubleCheckSync() {
            if (singleton5 == null) {
                synchronized (MyClass.class) {
                    if (singleton5 == null) {
                        singleton5 = new MyClass("singleton5 from getSingleton5DoubleCheckSync method");
                    }
                }
            }
            return singleton5;
        }
    }

    @Test
    public void getSingletonMethodTest() {
        Assert.assertNotNull(MyClass.getSingleton3NoSync());
        Assert.assertNotNull(MyClass.getSingleton4StdSync());
        Assert.assertNotNull(MyClass.getSingleton5DoubleCheckSync());
    }

    @Test
    public void isMyClassAnObject() {
        Class<MyClass> myClass = MyClass.class;
        Assert.assertTrue(myClass instanceof Object);
    }

    @Test
    public void whatKindOfClassIsMyClass() {
        // Since MyClass is an object, what kind of class is it?
        Class<MyClass> myClass = MyClass.class;
        Class<? extends Class> myClassClass = myClass.getClass();
        Assert.assertTrue(myClassClass instanceof Object);
        Assert.assertEquals(myClassClass.getSimpleName(), "Class" );
    }

    @Test
    public void getSingleton3NoSync() {
        testSingletonMethod(MyClass::getSingleton3NoSync);
    }

    @Test
    public void getSingleton4StdSync() {
        testSingletonMethod(MyClass::getSingleton4StdSync);
    }

    @Test
    public void getDoubleCheckConcurrently() {
        testSingletonMethod(MyClass::getSingleton5DoubleCheckSync);
    }


    <T> void testSingletonMethod(Supplier<T> supplier) {
        long start = DateTime.now().getMillis();
        List<T> instances =  IntStream.range(0,1000)
                                            .boxed()
                                            .parallel()
                                            .map(i-> supplier.get())
                                            .collect((Collectors.toList()));

        long finish = DateTime.now().getMillis();
        long duration = finish - start;
        System.out.printf("It took %d ms\n", duration);

        T first = instances.get(0);
        instances.forEach(i -> Assert.assertSame(i, first));
    }
}
