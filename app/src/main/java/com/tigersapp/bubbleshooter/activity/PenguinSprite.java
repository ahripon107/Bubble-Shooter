package com.tigersapp.bubbleshooter.activity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;

import java.util.Random;
import java.util.Vector;

/**
 * Created by Ripon on 1/5/17.
 */

public class PenguinSprite extends Sprite {
    public static final int[][] LOST_SEQUENCE;
    private static final int P_X = 340;
    private static final int P_Y = 410;
    public static final int STATE_FIRE = 2;
    public static final int STATE_GAME_LOST = 5;
    public static final int STATE_GAME_WON = 4;
    public static final int STATE_TURN_LEFT = 0;
    public static final int STATE_TURN_RIGHT = 1;
    public static final int STATE_VOID = 3;
    public static final int[][] WON_SEQUENCE;
    private int count;
    private int currentPenguin;
    private int finalState;
    private int nextPosition;
    private Random rand;
    private BmpWrap spritesImage;

    static {
        int[][] r0 = new int[8][];
        int[] iArr = new int[]{1, 7};
        r0[1] = new int[]{2, 8};
        r0[2] = new int[]{3, 9};
        r0[3] = new int[]{4, 10};
        r0[4] = new int[]{5, 11};
        r0[5] = new int[]{6, 12};
        r0[6] = new int[]{7, 13};
        r0[7] = new int[]{5, 14};
        LOST_SEQUENCE = r0;
        r0 = new int[8][];
        iArr = new int[]{1, 5};
        r0[1] = new int[]{2, 7};
        r0[2] = new int[]{3, 6};
        r0[3] = new int[]{4, 15};
        r0[4] = new int[]{5, 16};
        r0[5] = new int[]{6, 17};
        r0[6] = new int[]{7, 18};
        r0[7] = new int[]{4, 19};
        WON_SEQUENCE = r0;
    }

    public PenguinSprite(BmpWrap sprites, Random rand) {
        super(new Rect(P_X, 410, 420, 473));
        this.spritesImage = sprites;
        this.rand = rand;
        this.currentPenguin = 0;
        this.finalState = 3;
        this.nextPosition = 0;
    }

    public PenguinSprite(BmpWrap sprites, Random rand, int currentPenguin, int count, int finalState, int nextPosition) {
        super(new Rect(361, 436, 441, 499));
        this.spritesImage = sprites;
        this.rand = rand;
        this.currentPenguin = currentPenguin;
        this.count = count;
        this.finalState = finalState;
        this.nextPosition = nextPosition;
    }

    public void saveState(Bundle map, Vector saved_sprites) {
        if (getSavedId() == -1) {
            super.saveState(map, saved_sprites);
            map.putInt(String.format("%d-currentPenguin", new Object[]{Integer.valueOf(getSavedId())}), this.currentPenguin);
            map.putInt(String.format("%d-count", new Object[]{Integer.valueOf(getSavedId())}), this.count);
            map.putInt(String.format("%d-finalState", new Object[]{Integer.valueOf(getSavedId())}), this.finalState);
            map.putInt(String.format("%d-nextPosition", new Object[]{Integer.valueOf(getSavedId())}), this.nextPosition);
        }
    }

    public int getTypeId() {
        return Sprite.TYPE_PENGUIN;
    }

    public void updateState(int state) {
        if (this.finalState != 3) {
            this.count++;
            if (this.count % 6 != 0) {
                return;
            }
            if (this.finalState == 5) {
                this.currentPenguin = LOST_SEQUENCE[this.nextPosition][1];
                this.nextPosition = LOST_SEQUENCE[this.nextPosition][0];
                return;
            } else if (this.finalState == 4) {
                this.currentPenguin = WON_SEQUENCE[this.nextPosition][1];
                this.nextPosition = WON_SEQUENCE[this.nextPosition][0];
                return;
            } else {
                return;
            }
        }
        this.count++;
        switch (state) {
            case 0:
                this.count = 0;
                this.currentPenguin = 3;
                break;
            case 1:
                this.count = 0;
                this.currentPenguin = 2;
                break;
            case 2:
                this.count = 0;
                this.currentPenguin = 1;
                break;
            case 3:
                if (this.currentPenguin < 4 || this.currentPenguin > 7) {
                    this.currentPenguin = 0;
                    break;
                }
            case 4:
            case 5:
                this.count = 0;
                this.finalState = state;
                this.currentPenguin = 0;
                return;
        }
        if (this.count > 100) {
            this.currentPenguin = 7;
        } else if (this.count % 15 == 0 && this.count > 25) {
            this.currentPenguin = (this.rand.nextInt() % 3) + 4;
            if (this.currentPenguin < 4) {
                this.currentPenguin = 0;
            }
        }
    }

    public void paint(Canvas c, double scale, int dx, int dy) {
        Sprite.drawImageClipped(this.spritesImage, 340 - ((this.currentPenguin % 4) * 82), 410 - ((this.currentPenguin / 4) * 65), getSpriteArea(), c, scale, dx, dy);
    }
}