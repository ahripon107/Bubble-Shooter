package com.tigersapp.bubbleshooter.activity;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;

import java.util.Vector;

/**
 * Created by Ripon on 1/5/17.
 */

public abstract class Sprite {

    public static int TYPE_BUBBLE = 1;
    public static int TYPE_IMAGE = 2;
    public static int TYPE_LAUNCH_BUBBLE = 3;
    public static int TYPE_PENGUIN = 4;
    private int saved_id = -1;
    private Rect spriteArea;

    public abstract int getTypeId();

    public abstract void paint(Canvas canvas, double d, int i, int i2);

    public Sprite(Rect spriteArea) {
        this.spriteArea = spriteArea;
    }

    public void saveState(Bundle map, Vector saved_sprites) {
        if (this.saved_id == -1) {
            this.saved_id = saved_sprites.size();
            saved_sprites.addElement(this);
            map.putInt(String.format("%d-left", new Object[]{Integer.valueOf(this.saved_id)}), this.spriteArea.left);
            map.putInt(String.format("%d-right", new Object[]{Integer.valueOf(this.saved_id)}), this.spriteArea.right);
            map.putInt(String.format("%d-top", new Object[]{Integer.valueOf(this.saved_id)}), this.spriteArea.top);
            map.putInt(String.format("%d-bottom", new Object[]{Integer.valueOf(this.saved_id)}), this.spriteArea.bottom);
            map.putInt(String.format("%d-type", new Object[]{Integer.valueOf(this.saved_id)}), getTypeId());
        }
    }

    public final int getSavedId() {
        return this.saved_id;
    }

    public final void clearSavedId() {
        this.saved_id = -1;
    }

    public void changeSpriteArea(Rect newArea) {
        this.spriteArea = newArea;
    }

    public final void relativeMove(Point p) {
        this.spriteArea = new Rect(this.spriteArea);
        this.spriteArea.offset(p.x, p.y);
    }

    public final void relativeMove(int x, int y) {
        this.spriteArea = new Rect(this.spriteArea);
        this.spriteArea.offset(x, y);
    }

    public final void absoluteMove(Point p) {
        this.spriteArea = new Rect(this.spriteArea);
        this.spriteArea.offsetTo(p.x, p.y);
    }

    public final Point getSpritePosition() {
        return new Point(this.spriteArea.left, this.spriteArea.top);
    }

    public final Rect getSpriteArea() {
        return this.spriteArea;
    }

    public static void drawImage(BmpWrap image, int x, int y, Canvas c, double scale, int dx, int dy) {
        c.drawBitmap(image.bmp, (float) ((((double) x) * scale) + ((double) dx)), (float) ((((double) y) * scale) + ((double) dy)), null);
    }

    public static void drawImageClipped(BmpWrap image, int x, int y, Rect clipr, Canvas c, double scale, int dx, int dy) {
        c.save(Canvas.CLIP_SAVE_FLAG);
        c.clipRect((float) ((((double) clipr.left) * scale) + ((double) dx)), (float) ((((double) clipr.top) * scale) + ((double) dy)), (float) ((((double) clipr.right) * scale) + ((double) dx)), (float) ((((double) clipr.bottom) * scale) + ((double) dy)), Region.Op.REPLACE);
        c.drawBitmap(image.bmp, (float) ((((double) x) * scale) + ((double) dx)), (float) ((((double) y) * scale) + ((double) dy)), null);
        c.restore();
    }
}
