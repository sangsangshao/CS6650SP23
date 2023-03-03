package org.neu.cs6650;

import java.util.Vector;

public class SwipeStatistics {
    private int likes;
    private int dislikes;

    public SwipeStatistics() {
        likes = 0;
        dislikes = 0;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public synchronized void incrementLike() {
        likes += 1;
    }

    public synchronized void incrementDislike() {
        dislikes += 1;
    }

    @Override
    public String toString() {
        return "SwipeStatistics{" +
                "likes=" + likes +
                ", dislikes=" + dislikes +
                '}';
    }
}
