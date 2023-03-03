package org.neu.cs6650;

import java.util.Vector;

public class SwipeStatistics {
    private final Vector<String> likesList;

    public SwipeStatistics() {
        this.likesList = new Vector<>();;
    }

    public Vector<String> getLikesList() {
        return likesList;
    }


    @Override
    public String toString() {
        return "SwipeStatistics{" +
                "likesList=" + likesList +
                '}';
    }
}
