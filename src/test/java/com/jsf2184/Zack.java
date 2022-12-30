package com.jsf2184;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Zack {

    static class TextView {
        String val;
        int a;
        int b;
        int c;
        public TextView(String val) {
            this.val = val;
        }

        public void setA(int a) {
            this.a = a;
        }

        public void setB(int b) {
            this.b = b;
        }

        public void setC(int c) {
            this.c = c;
        }

        @Override
        public String toString() {
            return "TextView{" +
                    "val='" + val + '\'' +
                    ", a=" + a +
                    ", b=" + b +
                    ", c=" + c +
                    '}';
        }
    }


    static class Pojo {
        int x;
        String y;
        double z;

        public Pojo(int x, String y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int getX() {
            return x;
        }

        public String getY() {
            return y;
        }

        public double getZ() {
            return z;
        }
    }

    static class TextViewFactory {

        Function<Pojo, String> retriever;

        public TextViewFactory(Function<Pojo, String> retriever) {
            this.retriever = retriever;
        }

        TextView create(Pojo pojo) {
            final TextView textView = new TextView(retriever.apply(pojo));
            textView.setA(0);
            textView.setB(1);
            textView.setC(2);
            return textView;
        }

    }

    @Test
    public void testIt() {
        List<TextViewFactory> factories = Arrays.asList(new TextViewFactory(p ->Integer.toString(p.getX())),
                                                        new TextViewFactory(Pojo::getY),
                                                        new TextViewFactory(p ->Double.toString(p.getZ())));
        Pojo pojo = new Pojo(1, "2", 3.0);
        final List<TextView> list = factories.stream().map(f -> f.create(pojo)).collect(Collectors.toList());
        list.forEach(tv ->System.out.println(tv.toString()));

    }

}
