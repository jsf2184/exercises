package com.jsf2184.se8.functional;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Supplier;

public class SupplierTests {
    // Pass in a Supplier.
    //    A Supplier takes no argument and returns a templated type.

    public Integer callSupplier(Supplier<Integer> supplier) {
        Integer res = supplier.get();
        return res;
    }

    public Integer get5() {
        return 5;
    }

    public static Integer getStatic5() {
        return 5;
    }

    interface Getter {
        Integer get();
    }
    public Integer callGetter(Getter getter) {
        Integer res = getter.get();
        return res;
    }


        @Test
        public void testSupplier() {
            // Supplier is a lambda
            Assert.assertEquals(new Integer(5), callSupplier(() -> {return 5;}));
            // Supplier is a regular method of this class.
            Assert.assertEquals(new Integer(5), callSupplier(this::get5));
            // Supplier is a static method of this class.
            Assert.assertEquals(new Integer(5), callSupplier(SupplierTests::getStatic5));

            Assert.assertEquals(new Integer(5),  callSupplier(new Supplier<Integer>() {

                @Override
                public Integer get() {
                    return 5;
                }
            }));
        }

    @Test
    public void testCallGetter() {
        // Supplier is a lambda
        Assert.assertEquals(new Integer(5), callGetter(() -> {return 5;}));
        // Supplier is a regular method of this class.
        Assert.assertEquals(new Integer(5), callGetter(this::get5));
        // Supplier is a static method of this class.
        Assert.assertEquals(new Integer(5), callGetter(SupplierTests::getStatic5));

        Assert.assertEquals(new Integer(5),  callGetter(new Getter() {

            @Override
            public Integer get() {
                return 5;
            }
        }));
    }

}
