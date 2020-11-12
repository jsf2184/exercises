package com.jsf2184.mockitoPlay;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

// Thee are 3 alternative ways to trigger creation of all the mocks and tne SUT built
//   1 - Use annotation @RunWith(MockitoJUnitRunner.class)
//   2 - Use MockitoRule
//   3 - Call

// Alternative 1:
@RunWith(MockitoJUnitRunner.class)
public class AnnotateMockitoTests {


    @InjectMocks
    Holder _sut;

    @Mock
    private Dep1 _dep1;

    @Mock
    private Dep2 _dep2;

    // Alternative 2
//    @Rule
//    public MockitoRule mockitoRule = MockitoJUnit.rule();

    public static int setupCount = 0;

    @Before
    public void setup()
    {
//        setupCount++;
//        System.out.printf("running setup, count = %d\n", setupCount);
        // Alternative 3
//        MockitoAnnotations.initMocks(this);
    }

    public interface Dep1 {
        int f(int x);
    }
    public interface Dep2 {
        int f(int x);
    }

    public static class Holder {
        Dep1 _dep1;

        public Holder(Dep1 dep1) {
            System.out.println("Holder Constructor");
            this._dep1 = dep1;
        }

        public int f(int x) {
            return _dep1.f(x);
        }
    }

    @Test
    public void test1() {
        commonTest(_sut, 10);
    }

    @Test
    public void test2() {
        commonTest(_sut, 11);
    }


    public void commonTest(Holder sut, int res) {
        when(_dep1.f(anyInt())).thenReturn(res);
        Assert.assertEquals(res, sut.f(97));
        verify(_dep1, times(1)).f(97);
        verify(_dep1, times(0)).f(96);
        verify(_dep1).f(97);
        verifyNoInteractions(_dep2);

    }

    @Test
    public void test2MockReturns() {
        when(_dep1.f(anyInt())).thenReturn(3).thenReturn(4);
        Assert.assertEquals(3, _sut.f(100));
        Assert.assertEquals(4, _sut.f(100));
    }



    public Holder create() {
        Holder res = new Holder(_dep1);
        return res;
    }
}
