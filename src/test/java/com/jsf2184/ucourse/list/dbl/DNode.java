package com.jsf2184.ucourse.list.dbl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DNode {
    int value;
    DNode prev;
    DNode next;
    DNode child;

    public DNode(int value) {
        this.value = value;
        this.next = null;
        this.prev = null;
        this.child = null;
    }

    public static DNode create(int[] values) {
        DNode head = null;
        DNode tail = null;
        for (int v : values) {
            DNode node = new DNode(v);
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

    public static void print(DNode node) {
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

    public static DNode reverseInnerList(DNode head, int rvsStart, int rvsEnd) {
        DNode prior = null;
        DNode current = head;
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

    public static DNode reverseInPlace(DNode node, Integer count) {
        DNode head = null;
        int i= 0;
        DNode current = node;
        int num = 0;
        while(current != null && (count == null || num < count)  ) {
            num++;
            DNode next = current.next;
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
