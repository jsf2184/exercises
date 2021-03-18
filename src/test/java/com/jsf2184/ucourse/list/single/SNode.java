package com.jsf2184.ucourse.list.single;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
@Data
public class SNode {
    int value;
    SNode next;

    public SNode(int value) {
        this.value = value;
        this.next = null;
    }

    public static SNode create(int[] values) {
        SNode head = null;
        SNode tail = null;
        for (int v : values) {
            SNode node = new SNode(v);
            if (head == null) {
                head = node;
                tail = node;
            } else {
                tail.next = node;
                tail = node;
            }
        }
        return head;
    }

    public static void print(SNode node) {
        StringBuilder sb = new StringBuilder();
        while (node != null) {
            if (sb.length() !=0) {
                sb.append(", ");
            }
            sb.append(node.value);
            node = node.next;
        }
        log.info(sb.toString());
    }

    public static SNode reverseInnerList(SNode head, int rvsStart, int rvsEnd) {
        SNode prior = null;
        SNode current = head;
        for (int i=1; i<rvsStart && current != null; i++) {
            prior = current;
            current = current.next;
        }
        if (current == null) {
            // nothing to reverse.
            return  head;
        }

        int count = (rvsEnd - rvsStart) + 1;
        if (prior != null) {
            prior.next = reverseInPlace(current, count);
            return head;
        } else {
            return  reverseInPlace(current, count);
        }

    }

    public static SNode reverseInPlace(SNode node, Integer count) {
        SNode head = null;
        int i= 0;
        SNode current = node;
        int num = 0;
        while(current != null && (count == null || num < count)  ) {
            num++;
            SNode next = current.next;
            current.next = head;
            head = current;
            current = next;
        }
        if (node != null) {
            // Note that node is our new tail of this list or sublist.
            node.next = current; // either null or what would have been next.
        }
        return head;
    }

}
