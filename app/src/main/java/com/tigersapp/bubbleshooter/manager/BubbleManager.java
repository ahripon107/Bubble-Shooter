package com.tigersapp.bubbleshooter.manager;

import android.os.Bundle;

import com.tigersapp.bubbleshooter.activity.BmpWrap;

import java.util.Random;

/**
 * Created by Ripon on 1/5/17.
 */

public class BubbleManager {

    BmpWrap[] bubbles;
    int bubblesLeft = 0;
    int[] countBubbles;

    public BubbleManager(BmpWrap[] bubbles) {
        this.bubbles = bubbles;
        this.countBubbles = new int[bubbles.length];
    }

    public void saveState(Bundle map) {
        map.putInt("BubbleManager-bubblesLeft", this.bubblesLeft);
        map.putIntArray("BubbleManager-countBubbles", this.countBubbles);
    }

    public void restoreState(Bundle map) {
        this.bubblesLeft = map.getInt("BubbleManager-bubblesLeft");
        this.countBubbles = map.getIntArray("BubbleManager-countBubbles");
    }

    public void addBubble(BmpWrap bubble) {
        int i = findBubble(bubble);
        if (i > -1 && i < this.countBubbles.length) {
            int[] iArr = this.countBubbles;
            iArr[i] = iArr[i] + 1;
            this.bubblesLeft++;
        }
    }

    public void removeBubble(BmpWrap bubble) {
        int i = findBubble(bubble);
        if (i > -1 && i < this.countBubbles.length) {
            int[] iArr = this.countBubbles;
            int findBubble = findBubble(bubble);
            iArr[findBubble] = iArr[findBubble] - 1;
            this.bubblesLeft--;
        }
    }

    public int countBubbles() {
        return this.bubblesLeft;
    }

    public int nextBubbleIndex(Random rand) {
        int select = rand.nextInt() % this.bubbles.length;
        if (select < 0) {
            select = -select;
        }
        int count = -1;
        int position = -1;
        while (count != select) {
            position++;
            if (position == this.bubbles.length) {
                position = 0;
            }
            if (this.countBubbles[position] != 0) {
                count++;
            }
        }
        return position;
    }

    public BmpWrap nextBubble(Random rand) {
        return this.bubbles[nextBubbleIndex(rand)];
    }

    private int findBubble(BmpWrap bubble) {
        for (int i = 0; i < this.bubbles.length; i++) {
            if (this.bubbles[i] == bubble) {
                return i;
            }
        }
        return -1;
    }
}
