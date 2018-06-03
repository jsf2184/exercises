package com.jsf2184.se8.iface;

import org.junit.Assert;
import org.junit.Test;

public class IfaceDefaultTests {
    interface I {
        int getX();
        default int getY() {return getX() + 1;}
    }


    public static class IImpl implements  I {
        @Override
        public int getX() {
            return 105;
        }
    }

    public static class OImpl implements  I {
        @Override
        public int getX() {
            return 107;
        }

        @Override
        public int getY() {
            return I.super.getY() + 2;
        }
    }


    @Test
    public void testDefaultInterface() {
        I impl = new IImpl();
        Assert.assertEquals(105, impl.getX());
        Assert.assertEquals(106, impl.getY());
    }

    @Test
    public void testInvokeDefaultImplementationFromDerivation() {
        I impl = new OImpl();
        Assert.assertEquals(107, impl.getX());
        Assert.assertEquals(110, impl.getY());
    }

    interface Other {
        int getX();
    }

    class Both implements I, Other {

        int _x;

        public Both(int x) {
            _x = x;
        }

        @Override
        public int getX() {
            return _x;
        }
    }

    @Test
    public void testImplImplementsBoth() {
        Both both = new Both(5);
        Assert.assertEquals(5, both.getX());
    }

    interface OtherWithY extends Other {
        default int getY() {return getX() + 2;}
    }

    class BothWithConflict implements I, OtherWithY {

        int _x;

        public BothWithConflict(int x) {
            _x = x;
        }

        @Override
        public int getX() {
            return _x;
        }

        // Note since there is a conflict between getY() default implementations, the
        // compiler insists that we implement it. Otherwise, it wouldn't know which
        // getY() to use.
        //
        public int getY() {
            return OtherWithY.super.getY();
        }
    }

    @Test
    public void testResolvedMultipleInheritanceConflict() {
        BothWithConflict bwc = new BothWithConflict(5);
        OtherWithY otherAnonymous = new OtherWithY() {
            @Override
            public int getX() {
                return 5;
            }
        };
        Assert.assertEquals(otherAnonymous.getY(), bwc.getY() );

    }

    interface Doctor {
        default String who() { return "Doctor";};
    }
    interface Surgeon extends Doctor {
        default String who() { return "Surgeon";};
    }

    class DoubleDoc implements Doctor, Surgeon {

    }

    @Test
    public void verifyNoConflictWhenOneIfaceInheritsFromAnother() {
        DoubleDoc doubleDoc = new DoubleDoc();
        Assert.assertEquals("Surgeon", doubleDoc.who());
    }

}
