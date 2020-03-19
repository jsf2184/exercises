package com.jsf2184.mockitoPlay;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class BasicMockitoTests {

    private Dep1 _dep1;
    private Dep2 _dep2;

    public interface Dep1 {
        int f(int x);
        void passArg(Arg arg);
    }
    public interface Dep2 {
        int f(int x);
    }

    public static class Arg {
        int _x;

        public Arg(int x) {
            _x = x;
        }

        public int getX() {
            return _x;
        }
    }

    public static class Dep42 implements Dep1 {

        @Override
        public int f(int x) {
            return 42;
        }

        @Override
        public void passArg(Arg arg) {

        }


    }

    public static class Holder {
        Dep1 _dep1;

        public Holder(Dep1 dep1) {
            this._dep1 = dep1;
        }

        public int f(int x) {
            return _dep1.f(x);
        }
        public void passArg(int x) {
            Arg arg = new Arg(x);
            _dep1.passArg(arg);
        }


    }

    @Test
    public void testIt() {
        Holder sut = create();
        when(_dep1.f(anyInt())).thenReturn(3);
        Assert.assertEquals(3, sut.f(97));
        verify(_dep1, times(1)).f(97);
        verify(_dep1, times(0)).f(96);
        verify(_dep1).f(97);
        verifyNoInteractions(_dep2);
    }

    @Test
    public void testWithSpy() {

        // With Spy, we can wrap a real class and sort of make it behave like a mocked class.

        Dep42 dep42 = new Dep42();
        Dep1 spy = spy(dep42);
        when(spy.f(anyInt())).thenReturn(35);
        Holder sut = new Holder(spy);
        Assert.assertEquals(35, sut.f(100));
        verify(spy, times(1)).f(100);


    }

    @Test
    public void testArgCapture() {
        Holder sut = create();
        sut.passArg(27);
        ArgumentCaptor<Arg> captor = ArgumentCaptor.forClass(Arg.class);
        verify(_dep1, times(1)).passArg(captor.capture());
        Arg passedArg = captor.getValue();
        Assert.assertEquals(27, passedArg.getX());
    }

    @Test
    public void testThenAnswer() {
        Holder sut = create();
        when(sut.f(anyInt())).thenAnswer(invocation -> {
            Object argument = invocation.getArgument(0);
            return (int) argument + 1;
        });
        Assert.assertEquals(28, sut.f(27));

    }

    public Holder create() {
        _dep1 = mock(Dep1.class);
        _dep2 = mock(Dep2.class);
        Holder res = new Holder(_dep1);
        return res;
    }
}
