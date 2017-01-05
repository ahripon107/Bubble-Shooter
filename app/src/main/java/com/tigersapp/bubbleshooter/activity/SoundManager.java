package com.tigersapp.bubbleshooter.activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.tigersapp.bubbleshooter.R;

/**
 * Created by Ripon on 1/5/17.
 */

public class SoundManager {

    Context context;
    private int[] sm = new int[9];
    private SoundPool soundPool = new SoundPool(4, 3, 0);

    public SoundManager(Context context) {
        this.context = context;
        this.sm[0] = this.soundPool.load(context, R.raw.applause, 1);
        this.sm[1] = this.soundPool.load(context, R.raw.lose, 1);
        this.sm[2] = this.soundPool.load(context, R.raw.launch, 1);
        this.sm[3] = this.soundPool.load(context, R.raw.destroy_group, 1);
        this.sm[4] = this.soundPool.load(context, R.raw.rebound, 1);
        this.sm[5] = this.soundPool.load(context, R.raw.stick, 1);
        this.sm[6] = this.soundPool.load(context, R.raw.hurry, 1);
        this.sm[7] = this.soundPool.load(context, R.raw.newroot_solo, 1);
        this.sm[8] = this.soundPool.load(context, R.raw.noh, 1);
    }

    public final void playSound(int sound) {
        if (BubbleShooterActivity.getSoundOn()) {
            AudioManager mgr = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
            float volume = ((float) mgr.getStreamVolume(3)) / ((float) mgr.getStreamMaxVolume(3));
            this.soundPool.play(this.sm[sound], volume, volume, 1, 0, TextTrackStyle.DEFAULT_FONT_SCALE);
        }
    }

    public final void cleanUp() {
        this.sm = null;
        this.context = null;
        this.soundPool.release();
        this.soundPool = null;
    }
}
