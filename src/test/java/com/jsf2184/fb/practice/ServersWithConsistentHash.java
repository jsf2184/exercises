package com.jsf2184.fb.practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class ServersWithConsistentHash {


    static MessageDigest md;

    @Before
    public void setup() throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("MD5");
    }

    public static int getMd5Hash(String s)  {
        md.reset();
        md.update(s.getBytes());
        final int result = Arrays.hashCode(md.digest());
        return result;
    }


    @Data
    public static class Server {
        int id;
        UUID[] uuids;

        public Server(int id, int numAliases) {
            this.id = id;
            uuids = new UUID[numAliases];
            IntStream.range(0, numAliases).boxed().forEach(i -> uuids[i] = UUID.randomUUID());
        }

        String getName() {
            return "s" + id;
        }

        int getHash(int alias) {
            final String fullName = getName() + "." + alias;
            final int md5Hash = getMd5Hash(fullName);
            return md5Hash;
        }



        int getHashWithUuid(int alias) {
            final int result = uuids[alias].hashCode();
            return result;
        }

    }

    @Data
    public static class Job {
        int id;
        UUID uuid;

        public Job(int id) {
            this.id = id;
            uuid = UUID.randomUUID();
        }

        String getName() {
            return "s" + id;
        }

        int getHash() {
            return getMd5Hash(getName());
        }
    }


    public static class ServerPool {
        TreeMap<Integer, Server > map;
        private int replication;

        public ServerPool(int replication) {
            this.replication = replication;
            map = new TreeMap<>();
        }

        public void addServer(Server s) {
            for (int i=0; i<replication; i++) {
                final int hashCode = s.getHash(i);
                map.put(hashCode, s);
            }
        }

        public void removeServer(Server s) {
            for (int i=0; i<replication; i++) {
                final int hashCode = s.getHash(i);
                map.remove(hashCode);
            }
        }


        public Server getServer(Job job) {
            if (map.isEmpty()) {
                return null;
            }
            final int hashCode = job.getHash();
            Server server = map.get(hashCode);
            if (server != null) {
                return server;
            }

            Map.Entry<Integer, Server> nextEntry = map.higherEntry(hashCode);
            if (nextEntry == null) {
                nextEntry = map.firstEntry();
            }
            server = nextEntry.getValue();
            return server;
        }
    }


    @Test
    public void testScenario() {
        ServerPool serverPool = new ServerPool(5); // Noe the 5 is a replication factor, not how many are in the pool.
        IntStream.range(0, 3).boxed().forEach(i -> addServerToPool(i, serverPool));
        final List<Job> jobs =
                IntStream.range(0, 20)
                         .boxed()
                         .map(Job::new)
                         .collect(Collectors.toList());
        final List<String> jobServers3 = getJobServers(jobs, serverPool);
        log.info("with 3 servers: {}", jobServers3);
        final Server server3 = addServerToPool(3, serverPool);
        final List<String> jobServers4 = getJobServers(jobs, serverPool);
        log.info("with 4 servers: {}", jobServers4);
        serverPool.removeServer(server3);
        final List<String> jobServers3Again = getJobServers(jobs, serverPool);
        log.info("with 3 servers: {}", jobServers3Again);


    }

    public Server addServerToPool(int id, ServerPool serverPool) {
        Server server = new Server(id, 5);
        serverPool.addServer(server);
        return server;
    }

    public List<String> getJobServers(List<Job> jobs, ServerPool serverPool) {
        final List<String> result = jobs.stream()
                                         .map(serverPool::getServer)
                                         .map(Server::getName)
                                         .collect(Collectors.toList());
        return result;
    }


}
