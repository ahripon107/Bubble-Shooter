package com.tigersapp.bubbleshooter.arcade;

import android.graphics.Canvas;
import android.os.Bundle;

import com.tigersapp.bubbleshooter.activity.BmpWrap;

/**
 * Created by Ripon on 1/5/17.
 */

public class ArcadeCompressor {

    private BmpWrap compressor;
    private BmpWrap compressorHead;
    float height;
    int steps = 0;

    public ArcadeCompressor(BmpWrap compressorHead, BmpWrap compressor, float defaultHeight) {
        this.compressorHead = compressorHead;
        this.compressor = compressor;
        this.height = defaultHeight;
    }

    public void saveState(Bundle map) {
        map.putInt("compressor-steps", this.steps);
    }

    public void restoreState(Bundle map) {
        this.steps = map.getInt("compressor-steps");
    }

    public void moveDown() {
        this.steps++;
    }

    public void moveDown(float deltaHeight) {
        this.height += deltaHeight;
    }

    public void paint(Canvas c, double scale, int dx, int dy) {
        c.drawBitmap(this.compressorHead.bmp, (float) ((160.0d * scale) + ((double) dx)), (float) ((((((double) this.height) - 64.0d) + (28.0d * ((double) this.steps))) * scale) + ((double) dy)), null);
    }
}
