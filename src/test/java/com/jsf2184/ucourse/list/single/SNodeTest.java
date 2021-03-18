package com.jsf2184.ucourse.list.single;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
@Data
public class SNodeTest {

    @Test
    public void testBuild() {
        final SNode head = SNode.create(new int[]{0, 1, 2, 3});
        SNode.print(head);
    }

    @Test
    public void reverseInPlace() {
        SNode head = SNode.create(new int[]{0, 1, 2, 3});
        SNode reversed = SNode.reverseInPlace(head, null);
        SNode.print(reversed);

        head = SNode.create(new int[]{0});
        reversed = SNode.reverseInPlace(head, null);
        SNode.print(reversed);

    }

    @Test
    public void testPartialReverse() {
        SNode head = SNode.create(new int[]{1, 2, 3, 4, 5});
        SNode reversed = SNode.reverseInnerList(head, 2, 4);
        SNode.print(reversed);

        head = SNode.create(new int[]{1, 2, 3, 4, 5});
        reversed = SNode.reverseInnerList(head, 6, 10);
        SNode.print(reversed);

        head = SNode.create(new int[]{1, 2, 3, 4, 5});
        reversed = SNode.reverseInnerList(head, 1, 10);
        SNode.print(reversed);

        head = SNode.create(new int[]{1, 2, 3, 4, 5});
        reversed = SNode.reverseInnerList(head, 1, 3);
        SNode.print(reversed);

    }


}
