package com.jsf2184.esight.beads;

import org.junit.Test;

import java.util.*;

public class BeadRoutes {

    // Here tangled beads are represented by multiple paths into a bead
    // and/or multiple paths out of a bead
    //
    public static class Route {
        int b0;
        int b1;

        public Route(int b0, int b1) {
            this.b0 = b0;
            this.b1 = b1;
        }
    };

    public static class Strand {
        List<Integer> beads;

        public Strand(List<Integer> beads) {
            this.beads = beads;
        }

        public Strand() {
            this (new ArrayList<>());
        }

        public Strand(Strand src) {
            this(new ArrayList<>(src.beads));
        }

        public void add(Integer bead) {
            beads.add(bead);
        }
        public Integer getLast() {
            final int lastIndex = beads.size() - 1;
            if (lastIndex >= 0) {
                return  beads.get(lastIndex);
            }
            return null;
        }
    }

    public Set<Strand> process(List<Route> routes) {
        Set<Strand> result = new HashSet<>();
        if (routes == null || routes.size() == 0) {
            return result;
        }
        Integer start = null;
        Map<Integer, List<Integer>> forks = new HashMap<>();
        for(Route route : routes) {
            if (start == null || route.b0 < start ) {
                start = route.b0;
            }
            final List<Integer> dests = forks.computeIfAbsent(route.b0, origin -> new ArrayList<>());
            dests.add(route.b1);
        }

        Queue<Strand> queue = new LinkedList<>();
        Strand seed = new Strand(Collections.singletonList(start));
        queue.add(seed);
        while (!queue.isEmpty()) {
            final Strand strand = queue.remove();
            final Integer lastBead = strand.getLast();
            final List<Integer> alternatives = forks.get(lastBead);
            if (alternatives == null || alternatives.size() == 0) {
                // dead end
                result.add(strand);
                continue;
            }
            for (int next : alternatives) {
                Strand clone = new Strand(strand);
                clone.add((next));;
                queue.add(clone);
            }
        }
        return result;
    }

    @Test
    public void testRoutes() {
        List<Route> routes = Arrays.asList(
                new Route(0, 1),
                new Route(1, 2),
                new Route(1, 3),
                new Route(2, 4),
                new Route(3, 4),
                new Route(4, 5),
                new Route(4, 6)
                );

        final Set<Strand> strands = process(routes);
        System.out.printf("%d", strands.size());

    }
}
