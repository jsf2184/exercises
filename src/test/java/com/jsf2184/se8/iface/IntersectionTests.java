package com.jsf2184.se8.iface;

import org.junit.Assert;
import org.junit.Test;

public class IntersectionTests {
    interface YProvider {
        int getY();
    }

    interface NoFunc {
//        int getX();
    }

    @Test
    public void testIntersection() {
        YProvider yProvider = () -> 5;
        Assert.assertEquals(5, yProvider.getY());
        YProvider yIntersection = (YProvider & NoFunc) () -> 5;
        Assert.assertEquals(5, yIntersection.getY());
        NoFunc noFunc = (YProvider & NoFunc) () -> 6;
        Assert.assertEquals(6, ((YProvider) noFunc).getY());
    }

}
