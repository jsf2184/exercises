package com.jsf2184.mlm;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GenericPlay {
    public static class Building {
        int numRooms;

        public Building(int numRooms) {
            this.numRooms = numRooms;
        }

        public int getNumRooms() {
            return numRooms;
        }

    }

    public static class House extends Building  {
        int numBathrooms;
        int numBedrooms;
        public House(int numBathrooms, int numBedrooms) {
            super(numBathrooms + numBathrooms);
            this.numBathrooms = numBathrooms;
            this.numBedrooms = numBedrooms;
        }

        public int getNumBathrooms() {
            return numBathrooms;
        }

        public int getNumBedrooms() {
            return numBedrooms;
        }
    }

    public static int getRoomTotal(List<? extends Building> buildings) {
        Optional<Integer> result = buildings.stream().map(Building::getNumRooms).reduce(Integer::sum);
        return  result.orElse(0);
    }

    @Test
    public void testGetRoomTotal() {
        Assert.assertEquals(6,  getRoomTotal(Arrays.asList(new Building(2), new Building(4))));
    }

    @Test
    public void testGetRoomTotalForHouses() {
        List<House> houses = Arrays.asList(new House(1, 1), new House(2, 2));
        // Note that originally this did not compile because a List<House> is not a List<Building>
        // However, when I changed getRoomTotal argument to List<? extends Building> it worked
        Assert.assertEquals(6, getRoomTotal(houses));
    }

}
