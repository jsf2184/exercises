package com.jsf2184.mlm;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

public class ConcMapPlay {
    @Test
    public void testGet() {
        ConcurrentHashMap<String, String>  map = new ConcurrentHashMap<>();
        map.put("a", "A");
        Assert.assertEquals("A", map.get("a"));
    }
    @Test
    public void testNGetNoKey() {
        ConcurrentHashMap<String, String>  map = new ConcurrentHashMap<>();
        map.put("a", "A");
        Assert.assertNull( map.get("b"));
    }
    @Test
    public void testGetAfterNullPut() {
        ConcurrentHashMap<String, String>  map = new ConcurrentHashMap<>();
        try {
            map.put("a", null);
        } catch(Exception e) {
            System.out.printf("Caught exception %s\n", e.getClass().getSimpleName());
        }
    }

}
