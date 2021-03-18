package com.jsf2184.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Or;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Slf4j
public class PriorityQueueOrderBookTest {

    enum Side {
        Bid,
        Ask;

        Comparator<Order> comparator;

    }

    @Data
    @AllArgsConstructor
    public static class Order implements Comparable<Order> {
        Side side;
        int sqno;
        BigDecimal price;
        Integer qty;

        int compare(Order other) {
            final int c = price.compareTo(other.price);
            if (c != 0) {
                return c;
            }
            return sqno - other.sqno;
        }

        @Override
        public int compareTo(Order other) {
            final int c = compare(other);
            if (side == Side.Bid) {
               return -c;
            }
            return c;
        }

        public boolean isTradeable(Order incoming) {

            return  ((side == Side.Bid && price.compareTo(incoming.price) >= 0) || // If Bid, my price is >= incoming
                    (side == Side.Ask && price.compareTo(incoming.price) <= 0)) && // If Ask, my price is <= incoming
                    (qty > 0 && incoming.qty > 0);
        }

        public Trade makeTrade(Order incoming) {
            if (!isTradeable(incoming)) {
                return null;
            }
            int size = Math.min(qty, incoming.qty);
            Trade trade = new Trade(new Order(side, sqno, price, size),
                                    new Order(incoming.side, incoming.sqno, incoming.price, size));
            qty -= size;
            incoming.qty -= size;
            return trade;
        }
    }

    @Data
    @AllArgsConstructor
    public static class Trade {
        Order bookSide;
        Order incoming;
    }

    @Data
    public static class OrderBook {
        // asks: lowest ask first.
        PriorityQueue<Order> asks = new PriorityQueue<>((a, b) -> a.compareTo(b));
        // bids: highest bid first.
        PriorityQueue<Order> bids = new PriorityQueue<>((a, b) -> a.compareTo(b));
        int sqno = 0;

        public int numBids() {
            return bids.size();
        }

        public int numAsks() {
            return asks.size();
        }

        public List<Trade> processAsk(BigDecimal price, int qty) {
            Order incomingOrder = new Order(Side.Ask, sqno++, price, qty);
            List<Trade> trades = new ArrayList<>();
            PriorityQueue<Order> incomingOrders = asks;
            PriorityQueue<Order> existingOrders = bids;

            Order bestExistingOrder;
            while(true) {
                bestExistingOrder = existingOrders.peek();
                if (bestExistingOrder == null) {
                    break;
                }
                Trade trade = bestExistingOrder.makeTrade(incomingOrder);
                if (trade == null) {
                    break;
                }
                trades.add(trade);
                if (bestExistingOrder.qty == 0) {
                    existingOrders.remove();
                }
            }
            if (incomingOrder.qty > 0) {
                incomingOrders.add(incomingOrder);
            }
            return  trades;
        }

        public List<Trade> processBid(BigDecimal price, int qty) {
            Order incomingOrder = new Order(Side.Bid, sqno++, price, qty);
            List<Trade> trades = new ArrayList<>();
            PriorityQueue<Order> incomingOrders = bids;
            PriorityQueue<Order> existingOrders = asks;

            Order bestExistingOrder;
            while(true) {
                bestExistingOrder = existingOrders.peek();
                if (bestExistingOrder == null) {
                    break;
                }
                Trade trade = bestExistingOrder.makeTrade(incomingOrder);
                if (trade == null) {
                    break;
                }
                trades.add(trade);
                if (bestExistingOrder.qty == 0) {
                    existingOrders.remove();
                }
            }
            if (incomingOrder.qty > 0) {
                incomingOrders.add(incomingOrder);
            }
            return  trades;
        }
    }

    @Test
    public void testAddAsks() {
        OrderBook orderBook = new OrderBook();
        orderBook.processAsk(new BigDecimal(100), 1);
        orderBook.processAsk(new BigDecimal(98), 2);
        orderBook.processAsk(new BigDecimal(102), 3);
        orderBook.processAsk(new BigDecimal(97), 4);
        orderBook.processAsk(new BigDecimal(100), 5);
        orderBook.processAsk(new BigDecimal(102), 6);

        final PriorityQueue<Order> asks = orderBook.getAsks();
        Order order;
        while ((order = asks.poll()) != null) {
            log.info("{}", order.toString());
        }
    }

    @Test
    public void testAddBids() {
        OrderBook orderBook = new OrderBook();
        orderBook.processBid(new BigDecimal(93), 1);
        orderBook.processBid(new BigDecimal(91), 2);
        orderBook.processBid(new BigDecimal(95), 3);
        orderBook.processBid(new BigDecimal(89), 4);
        orderBook.processBid(new BigDecimal(93), 5);
        orderBook.processBid(new BigDecimal(96), 6);

        final PriorityQueue<Order> bids = orderBook.getBids();
        Order order;
        while ((order = bids.poll()) != null) {
            log.info("{}", order.toString());
        }
    }

    @Test
    public void testBidTradeable() {
        Order bid = new Order(Side.Bid, 1, new BigDecimal(100), 10);
        Assert.assertTrue(bid.isTradeable(new Order(Side.Ask, 1, new BigDecimal(100), 10) ));
        Assert.assertTrue(bid.isTradeable(new Order(Side.Ask, 1, new BigDecimal(99), 10) ));
        Assert.assertFalse(bid.isTradeable(new Order(Side.Ask, 1, new BigDecimal(101), 10) ));
        Assert.assertFalse(bid.isTradeable(new Order(Side.Ask, 1, new BigDecimal(100), 0) ));

        bid = new Order(Side.Bid, 1, new BigDecimal(100), 0);
        Assert.assertFalse(bid.isTradeable(new Order(Side.Ask, 1, new BigDecimal(99), 10) ));
    }

    @Test
    public void testAskTradeable() {
        Order ask = new Order(Side.Ask, 1, new BigDecimal(100), 10);
        Assert.assertTrue(ask.isTradeable(new Order(Side.Bid, 1, new BigDecimal(100), 10) ));
        Assert.assertTrue(ask.isTradeable(new Order(Side.Bid, 1, new BigDecimal(101), 10) ));
        Assert.assertFalse(ask.isTradeable(new Order(Side.Bid, 1, new BigDecimal(99), 10) ));
        Assert.assertFalse(ask.isTradeable(new Order(Side.Bid, 1, new BigDecimal(100), 0) ));

        ask = new Order(Side.Ask, 1, new BigDecimal(100), 0);
        Assert.assertFalse(ask.isTradeable(new Order(Side.Bid, 1, new BigDecimal(100), 10) ));
    }

    @Test
    public void testScenario() {
        OrderBook orderBook = new OrderBook();
        // All return an empty list of trades
        Assert.assertEquals(0, orderBook.processAsk(new BigDecimal(101), 1).size());
        Assert.assertEquals(0, orderBook.processAsk(new BigDecimal(102), 2).size());
        Assert.assertEquals(0, orderBook.processAsk(new BigDecimal(103), 3).size());
        Assert.assertEquals(0, orderBook.processAsk(new BigDecimal(104), 4).size());
        Assert.assertEquals(0, orderBook.processAsk(new BigDecimal(105), 5).size());
        // And we now have 5 asks
        Assert.assertEquals(5, orderBook.numAsks());

        Assert.assertEquals(0, orderBook.processBid(new BigDecimal(99), 9).size());
        Assert.assertEquals(1, orderBook.numBids());

        final List<Trade> trades = orderBook.processBid(new BigDecimal(102), 2);
        Assert.assertEquals(2, trades.size());
        Assert.assertEquals(4, orderBook.numAsks());
        Assert.assertEquals(1, orderBook.numBids());






    }


}
