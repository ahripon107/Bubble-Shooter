package com.tigersapp.bubbleshooter.activity;

import android.graphics.Canvas;
import android.graphics.Rect;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import java.util.Vector;

/**
 * Created by Ripon on 1/5/17.
 */

public class LaunchBubbleSprite extends Sprite {
    private BmpWrap[] bubbles;
    private BmpWrap[] colorblindBubbles;
    private int currentColor;
    private int currentDirection;
    private Drawable launcher;
    private double x_center = 318.0d;
    private double y_center = 406.0d;

    public LaunchBubbleSprite(int initialColor, int initialDirection, Drawable launcher, BmpWrap[] bubbles, BmpWrap[] colorblindBubbles) {
        super(new Rect(276, 362, 362, 438));
        this.currentColor = initialColor;
        this.currentDirection = initialDirection;
        this.launcher = launcher;
        this.bubbles = bubbles;
        this.colorblindBubbles = colorblindBubbles;
    }

    public void saveState(Bundle map, Vector saved_sprites) {
        if (getSavedId() == -1) {
            super.saveState(map, saved_sprites);
            map.putInt(String.format("%d-currentColor", new Object[]{Integer.valueOf(getSavedId())}), this.currentColor);
            map.putInt(String.format("%d-currentDirection", new Object[]{Integer.valueOf(getSavedId())}), this.currentDirection);
        }
    }

    public int getTypeId() {
        return Sprite.TYPE_LAUNCH_BUBBLE;
    }

    public void changeColor(int newColor) {
        this.currentColor = newColor;
    }

    public void changeDirection(int newDirection) {
        this.currentDirection = newDirection;
    }

    public double getXCenter() {
        return this.x_center;
    }

    public double getYCenter() {
        return this.y_center;
    }

    public final void paint(Canvas c, double scale, int dx, int dy) {
        if (BubbleShooterActivity.getMode() == 0) {
            Sprite.drawImage(this.bubbles[this.currentColor], 302, 390, c, scale, dx, dy);
        } else {
            Sprite.drawImage(this.colorblindBubbles[this.currentColor], 302, 390, c, scale, dx, dy);
        }
        c.save();
        this.x_center = (((double) 318) * scale) + ((double) dx);
        //this.y_center = (((double) WalletConstants.ERROR_CODE_SPENDING_LIMIT_EXCEEDED) * scale) + ((double) dy);
        this.y_center = (((double) 200) * scale) + ((double) dy);
        c.rotate((float) (4.5d * ((double) (this.currentDirection - 20))), (float) this.x_center, (float) this.y_center);
        this.launcher.setBounds((int) ((((double) 268) * scale) + ((double) dx)), (int) ((((double) 356) * scale) + ((double) dy)), (int) ((((double) 368) * scale) + ((double) dx)), (int) ((((double) 456) * scale) + ((double) dy)));
        this.launcher.draw(c);
        c.restore();
    }
}