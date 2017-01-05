package com.tigersapp.bubbleshooter.arcade;

/**
 * Created by Ripon on 1/5/17.
 */

public class ArcadeStatistics {

    public static int countJumpScore(int count) {
        return (count * 10) + ((count - 3) * 5);
    }

    public static int countFallScore(int count) {
        return count * 5;
    }
}
