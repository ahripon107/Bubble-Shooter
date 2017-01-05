package com.tigersapp.bubbleshooter.activity;

import android.graphics.Canvas;
import android.os.Bundle;

/**
 * Created by Ripon on 1/5/17.
 */

public class Compressor {

    private BmpWrap compressor;
    private BmpWrap compressorHead;
    int steps = 0;

    public Compressor(BmpWrap compressorHead, BmpWrap compressor) {
        this.compressorHead = compressorHead;
        this.compressor = compressor;
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

    public void paint(Canvas c, double scale, int dx, int dy) {
        for (int i = 0; i < this.steps; i++) {
            c.drawBitmap(this.compressor.bmp, (float) ((250.0d * scale) + ((double) dx)), (float) ((((double) ((i * 28) - 4)) * scale) + ((double) dy)), null);
            c.drawBitmap(this.compressor.bmp, (float) ((370.0d * scale) + ((double) dx)), (float) ((((double) ((i * 28) - 4)) * scale) + ((double) dy)), null);
        }
        c.drawBitmap(this.compressorHead.bmp, (float) ((160.0d * scale) + ((double) dx)), (float) ((((double) ((this.steps * 28) - 28)) * scale) + ((double) dy)), null);
    }
}
