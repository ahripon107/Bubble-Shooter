package com.tigersapp.bubbleshooter.manager;

import android.graphics.Canvas;
import android.os.Bundle;

import com.tigersapp.bubbleshooter.activity.Sprite;

import java.util.Vector;

/**
 * Created by Ripon on 1/5/17.
 */

public abstract class PuzzleGameScreen {

    private Vector sprites = new Vector();

    public abstract boolean play(boolean z, boolean z2, boolean z3, double d, double[] dArr);

    public final void saveSprites(Bundle map, Vector savedSprites) {
        for (int i = 0; i < this.sprites.size(); i++) {
            ((Sprite) this.sprites.elementAt(i)).saveState(map, savedSprites);
            map.putInt(String.format("game-%d", new Object[]{Integer.valueOf(i)}), ((Sprite) this.sprites.elementAt(i)).getSavedId());
        }
        map.putInt("numGameSprites", this.sprites.size());
    }

    public final void restoreSprites(Bundle map, Vector savedSprites) {
        this.sprites = new Vector();
        int numSprites = map.getInt("numGameSprites");
        for (int i = 0; i < numSprites; i++) {
            this.sprites.addElement(savedSprites.elementAt(map.getInt(String.format("game-%d", new Object[]{Integer.valueOf(i)}))));
        }
    }

    public final void addSprite(Sprite sprite) {
        this.sprites.removeElement(sprite);
        this.sprites.addElement(sprite);
    }

    public final void removeSprite(Sprite sprite) {
        this.sprites.removeElement(sprite);
    }

    public final void spriteToBack(Sprite sprite) {
        this.sprites.removeElement(sprite);
        this.sprites.insertElementAt(sprite, 0);
    }

    public final void spriteToFront(Sprite sprite) {
        this.sprites.removeElement(sprite);
        this.sprites.addElement(sprite);
    }

    public void paint(Canvas c, double scale, int dx, int dy) {
        for (int i = 0; i < this.sprites.size(); i++) {
            ((Sprite) this.sprites.elementAt(i)).paint(c, scale, dx, dy);
        }
    }
}
