package com.jsf2184.fb.practice;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Slf4j
public class AlternateHashing {

    MessageDigest md;

    @Before
    public void setup() throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("MD5");
    }

    public int getMd5Hash(String s) throws NoSuchAlgorithmException {
        md.reset();
        md.update(s.getBytes());
        final int result = Arrays.hashCode(md.digest());
        return result;
    }

    @Test
    public void test() throws NoSuchAlgorithmException {
        testIt("s0.0");
        testIt("s0.1");
        testIt("s0.2");
        testIt("s0.3");
        testIt("s1.0");
        testIt("s1.1");
        testIt("s1.2");
        testIt("s1.3");
        testIt("s0.0");
        testIt("s0.1");
        testIt("s0.2");
        testIt("s0.3");

    }

    public void testIt(String s) throws NoSuchAlgorithmException {
        final int md5Hash = getMd5Hash(s);
        log.info("For {}, hash is {}", s, md5Hash);
    }
}
