package com.jsf2184.esight.beads;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class BeadMembers {
    /*
     * Beads are numbered 0 thru N
     * Some beads are members of more than one necklace, others are not
     * On each necklace, the bead number increase in order
     * Membership is conveyed by Pairs (number, strand)
     *
     */

    public static class BeadMember {
        int beadNum;
        String strand;

        public BeadMember(int beadNum, String strand) {
            this.beadNum = beadNum;
            this.strand = strand;
        }
    }

    public static class Strand {
        String label;
        List<Integer> beads;

        public Strand(String label) {
            this.label = label;
            beads = new ArrayList<>();
        }

        public Strand(String label, List<Integer> beads) {
            this.label = label;
            this.beads = beads;
        }

        public void addBead(int beadNum) {
            beads.add(beadNum);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Strand)) return false;

            Strand strand = (Strand) o;

            if (!label.equals(strand.label)) return false;
            return beads.equals(strand.beads);
        }

        @Override
        public int hashCode() {
            int result = label.hashCode();
            result = 31 * result + beads.hashCode();
            return result;
        }
    }

    public Map<String, Strand> process(List<BeadMember> beadMembers) {

        // Suppose these are our beadMembers:
        // Bead Strand
        //----- ------
        //  1      A   - Bead 1 is only on Strand A
        //
        //  2      A   - Bead 2 is on Strands A and B
        //  2      B
        //
        //  3      B   - Bead 3 is on Strand B
        //
        //  4      A   - Bead 4 is on Strand A and B
        //  4      B

        Map<String, Strand> result = new HashMap<>();
        if (beadMembers == null || beadMembers.size() == 0) {
            return result;
        }

        // Find the maxBead number (in our example 4)
        int maxBead = -1;
        for (BeadMember beadMember : beadMembers) {
            maxBead = Math.max(maxBead, beadMember.beadNum);
        }
        // For every bead, create a list of the strands that it is on. First create the
        // structure.
        List<String>[] beadToStrands = new List[maxBead+1];
        for (int i=0; i<=maxBead; i++) {
            beadToStrands[i] = new ArrayList<>();
        }
        // Now get the strand identifiers on the list. Building something like this
        // Bead Strand
        //----- ------
        //  [0]  ->
        //  [1]  ->  A
        //  [2]  ->  A, B
        //  [3]  ->  B
        //  [4]  ->  A, B
        for (BeadMember beadMember : beadMembers) {
            beadToStrands[beadMember.beadNum].add(beadMember.strand);
        }

        // Place the beads on the strands where they belong. Building the following
        // [A] -> A: 1, 2, 4
        // [B] -> B: 2, 3, 4

        for (int beadNum=0; beadNum<=maxBead; beadNum++) {
            final List<String> strandsForBead = beadToStrands[beadNum];
            for (String strandLabel : strandsForBead) {
                final Strand strand = result.computeIfAbsent(strandLabel, Strand::new);
                strand.addBead(beadNum);
            }
        }
        return result;
    }

    @Test
    public void testProcess() {
        List<BeadMember> beadMembers = Arrays.asList(
                new BeadMember(1, "A"),
                new BeadMember(2, "A"),
                new BeadMember(2, "B"),
                new BeadMember(3, "B"),
                new BeadMember(4, "A"),
                new BeadMember(4, "B")
                );
        final Map<String, Strand> result = process(beadMembers);
        Assert.assertEquals(2, result.size());
        final Strand aStrand = result.get("A");
        final Strand bStrand = result.get("B");

        Strand expectedA = new Strand("A", Arrays.asList(1, 2, 4));
        Strand expectedB = new Strand("B", Arrays.asList(2, 3, 4));

        Assert.assertEquals(expectedA, aStrand);
        Assert.assertEquals(expectedB, bStrand);

    }

}
