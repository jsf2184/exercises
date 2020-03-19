package com.jsf2184.hackerrank.testMatata;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {
    public static class TransactionKey implements Comparable<TransactionKey> {
        public final Integer userId;
        public final Integer transactionId;

        public TransactionKey(Integer userId, Integer transactionId) {
            this.userId = userId;
            this.transactionId = transactionId;
        }

        public int compareTo(TransactionKey other) {
            if (userId.compareTo(other.userId) != 0) {
                return userId.compareTo(other.userId);
            } else {
                return transactionId.compareTo(other.transactionId);
            }
        }

        public String toString() {
            return "(" + userId.toString() + "," + transactionId.toString() + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TransactionKey other = (TransactionKey) o;

            return this.compareTo(other) == 0;
        }

        @Override
        public int hashCode() {
            return userId.hashCode() * transactionId.hashCode();
        }
    }

    public static class InputParser {
        private byte[] hex2bytes(String hex) {
            int len = hex.length();
            byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                        + Character.digit(hex.charAt(i+1), 16));
            }
            return data;
        }

        public byte[] getStdin() {
            Scanner scanner = new Scanner(System.in);
            StringBuilder builder = new StringBuilder();
            while(scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }

            return hex2bytes(builder.toString());
        }


    }

    enum MsgType {
        LOGIN(0),
        LOGOUT(1),
        TRANSACTION_BEGIN(2),
        TRANSACTION_COMMIT(3),
        TRANSACTION_ROLLABACK(4),
        INSERT(5),
        UPSERT(6),
        DELETE(7);

        int id;
        MsgType(int msgId) {
            id = msgId;
        }
        static HashMap<Integer, MsgType> map = new HashMap<>();
        static {
            for (MsgType msgType : MsgType.values()) {
                map.put(msgType.id, msgType);
            }
        }
    }


    /**
     * A class which wraps a raw binary WAL such that the snapshot class
     * can use it to reconstruct system state.
     */
    public static class WAL {
        /**
         * Construct the WAL
         * @param input the raw binary WAL
         */

        ByteBuffer buffer;

        int offset;
        public WAL(byte[] input) {
        }


        public List<String> getMsgFields(int msgLength) {
            byte dest[] = new byte[msgLength];
            buffer.get(dest, buffer.arrayOffset(), msgLength );
            String string = new String(dest);
            String[] parts = string.split("\\|");
            return Arrays.asList(parts);

        }


    }

    public static class TransactionCache {

    }

    /**
     * A class which constructs a view of the system's state given the input WAL.
     */
    public static class Snapshot {
        public Snapshot(WAL wal) {
            // TODO
        }

        /**
         * Retrieve the current state of the HashTable
         *
         * @return a Map representing the Keys and Values of the current state
         */
        public Map<String, String> getTable() {
            return null;  // TODO
        }

        public static class Message {
        }
        public static class Transaction {
            String userId;
            String transactionId;
            List<Message> messages;

            public Transaction(String userId, String transactionId) {
                this.userId = userId;
                this.transactionId = transactionId;
            }

            public String getUserId() {
                return userId;
            }


            public String getTransactionId() {
                return transactionId;
            }

            public List<Message> getMessages() {
                return messages;
            }

            public void setMessages(List<Message> messages) {
                this.messages = messages;
            }
        }





    /**
         * Retrieve all pending transactions, where pending is defined as a transaction that has not yet been
         * committed or rolled-back.
         *
         * @return a list of TransactionKeys representing all pending transactions
         */
        public List<TransactionKey> getTransactions() {
            return null;  // TODO
        }

        /**
         * Retrieve all logged-in users
         * @return a list of user-ids representing logged-in users
         */
        public List<Integer> getUsers() {
            return null;  // TODO
        }

        /**
         * Retrieve the high-watermark of the system
         * @return an Instant representing the epoch timestamp of the latest event read
         */
        public Instant getHighWatermark() {
            return null;  // TODO
        }
    }

    public static void printReport(Snapshot snapshot) {
        System.out.println("High Watermark");
        System.out.println(snapshot.getHighWatermark());

        System.out.println("\nUsers");
        List<Integer> sortedUsers = snapshot.getUsers();
        sortedUsers.sort(Comparator.naturalOrder());
        if (sortedUsers.isEmpty()) {
            System.out.println();
        } else {
            System.out.println(sortedUsers.stream().map((x) -> x.toString()).collect(Collectors.joining("\n")));
        }

        System.out.println("\nTransactions");
        List<TransactionKey> sortedTransactions = snapshot.getTransactions();
        sortedTransactions.sort(Comparator.naturalOrder());
        if (sortedTransactions.isEmpty()) {
            System.out.println();
        } else {
            System.out.println(sortedTransactions.stream().map((x) -> x.toString()).collect(Collectors.joining("\n")));
        }

        System.out.println("\nTable");
        TreeMap<String, String> sortedTable = new TreeMap<>(snapshot.getTable());
        if (sortedTable.isEmpty()) {
            System.out.println();
        } else {
            sortedTable.forEach((x, y) -> System.out.println(x + ": " + y));
        }
    }


    public static void main(String args[] ) throws Exception {
        byte[] input = new InputParser().getStdin();
        WAL wal = new WAL(input);
        Snapshot snapshot = new Snapshot(wal);
        printReport(snapshot);
    }
}