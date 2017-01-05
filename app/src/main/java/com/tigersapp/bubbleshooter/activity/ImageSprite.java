package com.tigersapp.bubbleshooter.activity;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;

import java.util.Vector;

/**
 * Created by Ripon on 1/5/17.
 */

public class ImageSprite extends Sprite {
    private BmpWrap displayedImage;

    public ImageSprite(Rect area, BmpWrap img) {
        super(area);
        this.displayedImage = img;
    }

    public void saveState(Bundle map, Vector savedSprites) {
        if (getSavedId() == -1) {
            super.saveState(map, savedSprites);
            map.putInt(String.format("%d-imageId", new Object[]{Integer.valueOf(getSavedId())}), this.displayedImage.id);
        }
    }

    public int getTypeId() {
        return Sprite.TYPE_IMAGE;
    }

    public void changeImage(BmpWrap img) {
        this.displayedImage = img;
    }

    public final void paint(Canvas c, double scale, int dx, int dy) {
        Point p = super.getSpritePosition();
        Sprite.drawImage(this.displayedImage, p.x, p.y, c, scale, dx, dy);
    }
}