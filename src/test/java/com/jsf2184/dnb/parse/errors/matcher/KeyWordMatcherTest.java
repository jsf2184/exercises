package com.jsf2184.dnb.parse.errors.matcher;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class KeyWordMatcherTest {

    IKeyWordMatcher simpleKeyWordMatcher;
    IKeyWordMatcher approximateKeyWordMatcher;
    KeyWordMatcher keyWordMatcher;


    @Before
    public void testSetup() {
        simpleKeyWordMatcher = mock(IKeyWordMatcher.class);
        approximateKeyWordMatcher = mock(IKeyWordMatcher.class);
        keyWordMatcher = new KeyWordMatcher(simpleKeyWordMatcher, approximateKeyWordMatcher);
    }

    @Test
    public void testMatchWithSimpleMatch() {
        when(simpleKeyWordMatcher.matchWord("error")).thenReturn(KeyWordEnum.ERROR);
        KeyWordEnum result = keyWordMatcher.matchWord("error");
        Assert.assertEquals(KeyWordEnum.ERROR, result);
        verifyNoInteractions(approximateKeyWordMatcher);
    }

    @Test
    public void testMatchWhenApproximateMatchIsNeeded() {
        when(simpleKeyWordMatcher.matchWord("arror")).thenReturn(null);
        when(approximateKeyWordMatcher.matchWord("arror")).thenReturn(KeyWordEnum.ERROR);
        KeyWordEnum result = keyWordMatcher.matchWord("arror");
        Assert.assertEquals(KeyWordEnum.ERROR, result);
        verify(simpleKeyWordMatcher, times(1)).matchWord("arror");
        verify(approximateKeyWordMatcher, times(1)).matchWord("arror");
    }


}