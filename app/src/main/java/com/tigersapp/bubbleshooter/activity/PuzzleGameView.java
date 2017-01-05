package com.tigersapp.bubbleshooter.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tigersapp.bubbleshooter.R;

import java.util.Vector;

/**
 * Created by Ripon on 1/5/17.
 */

public class PuzzleGameView extends SurfaceView implements SurfaceHolder.Callback {
    private Context mContext;
    Handler mHdl = new Handler();
    private GameThread thread;

    public class GameThread extends Thread {
        public static final int EXTENDED_GAMEFIELD_WIDTH = 640;
        private static final int FRAME_DELAY = 40;
        public static final int GAMEFIELD_HEIGHT = 480;
        public static final int GAMEFIELD_WIDTH = 300;
        public static final int STATE_ABOUT = 4;
        public static final int STATE_PAUSE = 2;
        public static final int STATE_RUNNING = 1;
        private static final double TOUCH_COEFFICIENT = 0.2d;
        private static final double TOUCH_FIRE_Y_THRESHOLD = 350.0d;
        private static final double TRACKBALL_COEFFICIENT = 5.0d;
        private BmpWrap mBackground;
        private Bitmap mBackgroundOrig;
        private BmpWrap mBubbleBlink;
        private Bitmap mBubbleBlinkOrig;
        private BmpWrap[] mBubbles;
        private BmpWrap[] mBubblesBlind;
        private Bitmap[] mBubblesOrig;
        private int mCanvasHeight = 1;
        private int mCanvasWidth = 1;
        private BmpWrap mCompressor;
        private BmpWrap mCompressorHead;
        private Bitmap mCompressorHeadOrig;
        private Bitmap mCompressorOrig;
        private int mDisplayDX;
        private int mDisplayDY;
        private double mDisplayScale;
        private boolean mFire = false;
        private BubbleFont mFont;
        private BmpWrap mFontImage;
        private Bitmap mFontImageOrig;
        private BmpWrap[] mFrozenBubbles;
        private Bitmap[] mFrozenBubblesOrig;
        private BubbleShooterGame mFrozenGame;
        private BmpWrap mGameLost;
        private Bitmap mGameLostOrig;
        private BmpWrap mGameWon;
        private Bitmap mGameWonOrig;
        private BmpWrap mHurry;
        private Bitmap mHurryOrig;
        Vector mImageList;
        private boolean mImagesReady = false;
        private long mLastTime;
        private Drawable mLauncher;
        private boolean mLeft = false;
        private LevelManager mLevelManager;
        private BmpWrap mLife;
        private Bitmap mLifeOrig;
        private HandleNextLevel mListener;
        private int mMode;
        private BmpWrap mPenguins;
        private Bitmap mPenguinsOrig;
        private boolean mRight = false;
        private boolean mRun = false;
        private SoundManager mSoundManager;
        private SurfaceHolder mSurfaceHolder;
        private boolean mSurfaceOK = false;
        private BmpWrap[] mTargetedBubbles;
        private Bitmap[] mTargetedBubblesOrig;
        private double[] mTouchDX = new double[]{318.0d, 410.0d};
        private boolean mTouchFire = false;
        private double mTrackballDX = 0.0d;
        private boolean mUp = false;
        private boolean mWasFire = false;
        private boolean mWasLeft = false;
        private boolean mWasRight = false;
        private boolean mWasUp = false;

        public BubbleShooterGame getBubbleShooterGame() {
            return this.mFrozenGame;
        }

        public int getCurrentLevelIndex() {
            int levelIndex;
            synchronized (this.mSurfaceHolder) {
                levelIndex = this.mLevelManager.getLevelIndex();
            }
            return levelIndex;
        }

        public void setParameters() {
        }

        private BmpWrap NewBmpWrap() {
            BmpWrap new_img = new BmpWrap(this.mImageList.size());
            this.mImageList.addElement(new_img);
            return new_img;
        }

        public GameThread(SurfaceHolder surfaceHolder, byte[] customLevels, int startingLevel) {
            int i;
            this.mSurfaceHolder = surfaceHolder;
            Resources res = PuzzleGameView.this.mContext.getResources();
            setState(2);
            BitmapFactory.Options options = new BitmapFactory.Options();
            try {
                options.getClass().getField("inScaled").set(options, Boolean.FALSE);
            } catch (Exception e) {
            }
            this.mBackgroundOrig = BitmapFactory.decodeResource(res, R.drawable.background, options);
            this.mBubblesOrig = new Bitmap[8];
            this.mBubblesOrig[0] = BitmapFactory.decodeResource(res, R.drawable.bubble_1, options);
            this.mBubblesOrig[1] = BitmapFactory.decodeResource(res, R.drawable.bubble_2, options);
            this.mBubblesOrig[2] = BitmapFactory.decodeResource(res, R.drawable.bubble_3, options);
            this.mBubblesOrig[3] = BitmapFactory.decodeResource(res, R.drawable.bubble_4, options);
            this.mBubblesOrig[4] = BitmapFactory.decodeResource(res, R.drawable.bubble_5, options);
            this.mBubblesOrig[5] = BitmapFactory.decodeResource(res, R.drawable.bubble_6, options);
            this.mBubblesOrig[6] = BitmapFactory.decodeResource(res, R.drawable.bubble_7, options);
            this.mBubblesOrig[7] = BitmapFactory.decodeResource(res, R.drawable.bubble_8, options);
            this.mFrozenBubblesOrig = new Bitmap[8];
            this.mFrozenBubblesOrig[0] = BitmapFactory.decodeResource(res, R.drawable.frozen_1, options);
            this.mFrozenBubblesOrig[1] = BitmapFactory.decodeResource(res, R.drawable.frozen_2, options);
            this.mFrozenBubblesOrig[2] = BitmapFactory.decodeResource(res, R.drawable.frozen_3, options);
            this.mFrozenBubblesOrig[3] = BitmapFactory.decodeResource(res, R.drawable.frozen_4, options);
            this.mFrozenBubblesOrig[4] = BitmapFactory.decodeResource(res, R.drawable.frozen_5, options);
            this.mFrozenBubblesOrig[5] = BitmapFactory.decodeResource(res, R.drawable.frozen_6, options);
            this.mFrozenBubblesOrig[6] = BitmapFactory.decodeResource(res, R.drawable.frozen_7, options);
            this.mFrozenBubblesOrig[7] = BitmapFactory.decodeResource(res, R.drawable.frozen_8, options);
            this.mTargetedBubblesOrig = new Bitmap[6];
            this.mTargetedBubblesOrig[0] = BitmapFactory.decodeResource(res, R.drawable.fixed_1, options);
            this.mTargetedBubblesOrig[1] = BitmapFactory.decodeResource(res, R.drawable.fixed_2, options);
            this.mTargetedBubblesOrig[2] = BitmapFactory.decodeResource(res, R.drawable.fixed_3, options);
            this.mTargetedBubblesOrig[3] = BitmapFactory.decodeResource(res, R.drawable.fixed_4, options);
            this.mTargetedBubblesOrig[4] = BitmapFactory.decodeResource(res, R.drawable.fixed_5, options);
            this.mTargetedBubblesOrig[5] = BitmapFactory.decodeResource(res, R.drawable.fixed_6, options);
            this.mBubbleBlinkOrig = BitmapFactory.decodeResource(res, R.drawable.bubble_blink, options);
            this.mGameWonOrig = BitmapFactory.decodeResource(res, R.drawable.win_panel, options);
            this.mGameLostOrig = BitmapFactory.decodeResource(res, R.drawable.lose_panel, options);
            this.mHurryOrig = BitmapFactory.decodeResource(res, R.drawable.hurry, options);
            this.mPenguinsOrig = BitmapFactory.decodeResource(res, R.drawable.penguins, options);
            this.mCompressorHeadOrig = BitmapFactory.decodeResource(res, R.drawable.compressor, options);
            this.mCompressorOrig = BitmapFactory.decodeResource(res, R.drawable.compressor_body, options);
            this.mLifeOrig = BitmapFactory.decodeResource(res, R.drawable.life, options);
            this.mFontImageOrig = BitmapFactory.decodeResource(res, R.drawable.numfont, options);
            this.mImageList = new Vector();
            this.mBackground = NewBmpWrap();
            this.mBubbles = new BmpWrap[8];
            for (i = 0; i < this.mBubbles.length; i++) {
                this.mBubbles[i] = NewBmpWrap();
            }
            this.mBubblesBlind = new BmpWrap[8];
            for (i = 0; i < this.mBubblesBlind.length; i++) {
                this.mBubblesBlind[i] = NewBmpWrap();
            }
            this.mFrozenBubbles = new BmpWrap[8];
            for (i = 0; i < this.mFrozenBubbles.length; i++) {
                this.mFrozenBubbles[i] = NewBmpWrap();
            }
            this.mTargetedBubbles = new BmpWrap[6];
            for (i = 0; i < this.mTargetedBubbles.length; i++) {
                this.mTargetedBubbles[i] = NewBmpWrap();
            }
            this.mBubbleBlink = NewBmpWrap();
            this.mGameWon = NewBmpWrap();
            this.mGameLost = NewBmpWrap();
            this.mHurry = NewBmpWrap();
            this.mPenguins = NewBmpWrap();
            this.mCompressorHead = NewBmpWrap();
            this.mCompressor = NewBmpWrap();
            this.mLife = NewBmpWrap();
            this.mFontImage = NewBmpWrap();
            this.mFont = new BubbleFont(this.mFontImage);
            this.mLauncher = res.getDrawable(R.drawable.launcher);
            this.mSoundManager = new SoundManager(PuzzleGameView.this.mContext);
            if (customLevels == null) {
                this.mLevelManager = new LevelManager(PuzzleGameView.this.mContext, PuzzleGameView.this.mContext.getSharedPreferences(BubbleShooterActivity.PREFS_NAME, 0).getInt("level", 0));
            }
            this.mFrozenGame = new BubbleShooterGame(this.mBackground, this.mBubbles, this.mBubblesBlind, this.mFrozenBubbles, this.mTargetedBubbles, this.mBubbleBlink, this.mGameWon, this.mGameLost, this.mHurry, this.mPenguins, this.mCompressorHead, this.mCompressor, this.mLauncher, this.mSoundManager, this.mLevelManager);
        }

        public void setListener(HandleNextLevel listener) {
            this.mListener = listener;
        }

        private void scaleFrom(BmpWrap image, Bitmap bmp) {
            if (!(image.bmp == null || image.bmp == bmp)) {
                image.bmp.recycle();
            }
            if (this.mDisplayScale <= 0.99999d || this.mDisplayScale >= 1.00001d) {
                image.bmp = Bitmap.createScaledBitmap(bmp, (int) (((double) bmp.getWidth()) * this.mDisplayScale), (int) (((double) bmp.getHeight()) * this.mDisplayScale), true);
            } else {
                image.bmp = bmp;
            }
        }

        private void resizeBitmaps() {
            int i;
            scaleFrom(this.mBackground, this.mBackgroundOrig);
            for (i = 0; i < this.mBubblesOrig.length; i++) {
                scaleFrom(this.mBubbles[i], this.mBubblesOrig[i]);
            }
            for (i = 0; i < this.mFrozenBubbles.length; i++) {
                scaleFrom(this.mFrozenBubbles[i], this.mFrozenBubblesOrig[i]);
            }
            for (i = 0; i < this.mTargetedBubbles.length; i++) {
                scaleFrom(this.mTargetedBubbles[i], this.mTargetedBubblesOrig[i]);
            }
            scaleFrom(this.mBubbleBlink, this.mBubbleBlinkOrig);
            scaleFrom(this.mGameWon, this.mGameWonOrig);
            scaleFrom(this.mGameLost, this.mGameLostOrig);
            scaleFrom(this.mHurry, this.mHurryOrig);
            scaleFrom(this.mPenguins, this.mPenguinsOrig);
            scaleFrom(this.mCompressorHead, this.mCompressorHeadOrig);
            scaleFrom(this.mCompressor, this.mCompressorOrig);
            scaleFrom(this.mLife, this.mLifeOrig);
            scaleFrom(this.mFontImage, this.mFontImageOrig);
            this.mImagesReady = true;
        }

        public void pause() {
            synchronized (this.mSurfaceHolder) {
                if (this.mMode == 1) {
                    setState(2);
                }
            }
        }

        public void newGame() {
            synchronized (this.mSurfaceHolder) {
                this.mLevelManager.goToFirstLevel();
                this.mFrozenGame = new BubbleShooterGame(this.mBackground, this.mBubbles, this.mBubblesBlind, this.mFrozenBubbles, this.mTargetedBubbles, this.mBubbleBlink, this.mGameWon, this.mGameLost, this.mHurry, this.mPenguins, this.mCompressorHead, this.mCompressor, this.mLauncher, this.mSoundManager, this.mLevelManager);
            }
        }

        public void replayGame() {
            synchronized (this.mSurfaceHolder) {
                this.mListener.showAdsNextLevle();
                this.mFrozenGame = new BubbleShooterGame(this.mBackground, this.mBubbles, this.mBubblesBlind, this.mFrozenBubbles, this.mTargetedBubbles, this.mBubbleBlink, this.mGameWon, this.mGameLost, this.mHurry, this.mPenguins, this.mCompressorHead, this.mCompressor, this.mLauncher, this.mSoundManager, this.mLevelManager);
            }
        }

        public void nextLevel() {
            synchronized (this.mSurfaceHolder) {
                this.mListener.showAdsNextLevle();
                this.mLevelManager.goToNextLevel();
                this.mFrozenGame = new BubbleShooterGame(this.mBackground, this.mBubbles, this.mBubblesBlind, this.mFrozenBubbles, this.mTargetedBubbles, this.mBubbleBlink, this.mGameWon, this.mGameLost, this.mHurry, this.mPenguins, this.mCompressorHead, this.mCompressor, this.mLauncher, this.mSoundManager, this.mLevelManager);
            }
        }

        public void run() {
            while (this.mRun) {
                long now = System.currentTimeMillis();
                long delay = (40 + this.mLastTime) - now;
                if (delay > 0) {
                    try {
                        sleep(delay);
                    } catch (InterruptedException e) {
                    }
                }
                this.mLastTime = now;
                Canvas c = null;
                try {
                    if (surfaceOK()) {
                        c = this.mSurfaceHolder.lockCanvas(null);
                        if (c != null) {
                            synchronized (this.mSurfaceHolder) {
                                if (this.mRun && this.mMode != 4) {
                                    if (this.mMode == 1) {
                                        updateGameState();
                                    }
                                    doDraw(c);
                                }
                            }
                        }
                    }
                    if (c != null) {
                        this.mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                } catch (Throwable th) {
                    if (c != null) {
                        this.mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        public Bundle saveState(Bundle map) {
            synchronized (this.mSurfaceHolder) {
                if (map != null) {
                    this.mFrozenGame.saveState(map);
                    this.mLevelManager.saveState(map);
                }
            }
            return map;
        }

        public synchronized void restoreState(Bundle map) {
            synchronized (this.mSurfaceHolder) {
                setState(2);
                this.mFrozenGame.restoreState(map, this.mImageList);
                this.mLevelManager.restoreState(map);
            }
        }

        public void setRunning(boolean b) {
            this.mRun = b;
        }

        public void setState(int mode) {
            synchronized (this.mSurfaceHolder) {
                this.mMode = mode;
            }
        }

        public void setSurfaceOK(boolean ok) {
            synchronized (this.mSurfaceHolder) {
                this.mSurfaceOK = ok;
            }
        }

        public boolean surfaceOK() {
            boolean z;
            synchronized (this.mSurfaceHolder) {
                z = this.mSurfaceOK;
            }
            return z;
        }

        public void setSurfaceSize(int width, int height) {
            synchronized (this.mSurfaceHolder) {
                this.mCanvasWidth = width;
                this.mCanvasHeight = height;
                if ((((float) width) * TextTrackStyle.DEFAULT_FONT_SCALE) / ((float) height) >= 0.625f) {
                    this.mDisplayScale = (((double) height) * 1.0d) / 480.0d;
                    this.mDisplayDX = (int) ((((double) width) - (this.mDisplayScale * 640.0d)) / 2.0d);
                    this.mDisplayDY = 0;
                } else {
                    this.mDisplayScale = (((double) width) * 1.0d) / 300.0d;
                    this.mDisplayDX = (int) (((-this.mDisplayScale) * 340.0d) / 2.0d);
                    this.mDisplayDY = (int) ((((double) height) - (this.mDisplayScale * 480.0d)) / 2.0d);
                }
                resizeBitmaps();
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        boolean doKeyDown(int r4, android.view.KeyEvent r5) {
            /*
            r3 = this;
            r0 = 1;
            r1 = r3.mSurfaceHolder;
            monitor-enter(r1);
            r2 = r3.mMode;	 Catch:{ all -> 0x0028 }
            if (r2 == r0) goto L_0x000c;
        L_0x0008:
            r2 = 1;
            r3.setState(r2);	 Catch:{ all -> 0x0028 }
        L_0x000c:
            r2 = r3.mMode;	 Catch:{ all -> 0x0028 }
            if (r2 != r0) goto L_0x0043;
        L_0x0010:
            r2 = 21;
            if (r4 != r2) goto L_0x001c;
        L_0x0014:
            r2 = 1;
            r3.mLeft = r2;	 Catch:{ all -> 0x0028 }
            r2 = 1;
            r3.mWasLeft = r2;	 Catch:{ all -> 0x0028 }
            monitor-exit(r1);	 Catch:{ all -> 0x0028 }
        L_0x001b:
            return r0;
        L_0x001c:
            r2 = 22;
            if (r4 != r2) goto L_0x002b;
        L_0x0020:
            r2 = 1;
            r3.mRight = r2;	 Catch:{ all -> 0x0028 }
            r2 = 1;
            r3.mWasRight = r2;	 Catch:{ all -> 0x0028 }
            monitor-exit(r1);	 Catch:{ all -> 0x0028 }
            goto L_0x001b;
        L_0x0028:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0028 }
            throw r0;
        L_0x002b:
            r2 = 23;
            if (r4 != r2) goto L_0x0037;
        L_0x002f:
            r2 = 1;
            r3.mFire = r2;	 Catch:{ all -> 0x0028 }
            r2 = 1;
            r3.mWasFire = r2;	 Catch:{ all -> 0x0028 }
            monitor-exit(r1);	 Catch:{ all -> 0x0028 }
            goto L_0x001b;
        L_0x0037:
            r2 = 19;
            if (r4 != r2) goto L_0x0043;
        L_0x003b:
            r2 = 1;
            r3.mUp = r2;	 Catch:{ all -> 0x0028 }
            r2 = 1;
            r3.mWasUp = r2;	 Catch:{ all -> 0x0028 }
            monitor-exit(r1);	 Catch:{ all -> 0x0028 }
            goto L_0x001b;
        L_0x0043:
            monitor-exit(r1);	 Catch:{ all -> 0x0028 }
            r0 = 0;
            goto L_0x001b;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.pop.bubble.deluxe.activity.PuzzleGameView.GameThread.doKeyDown(int, android.view.KeyEvent):boolean");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        boolean doKeyUp(int r5, android.view.KeyEvent r6) {
            /*
            r4 = this;
            r0 = 1;
            r1 = 0;
            r2 = r4.mSurfaceHolder;
            monitor-enter(r2);
            r3 = r4.mMode;	 Catch:{ all -> 0x001b }
            if (r3 != r0) goto L_0x0030;
        L_0x0009:
            r3 = 21;
            if (r5 != r3) goto L_0x0012;
        L_0x000d:
            r1 = 0;
            r4.mLeft = r1;	 Catch:{ all -> 0x001b }
            monitor-exit(r2);	 Catch:{ all -> 0x001b }
        L_0x0011:
            return r0;
        L_0x0012:
            r3 = 22;
            if (r5 != r3) goto L_0x001e;
        L_0x0016:
            r1 = 0;
            r4.mRight = r1;	 Catch:{ all -> 0x001b }
            monitor-exit(r2);	 Catch:{ all -> 0x001b }
            goto L_0x0011;
        L_0x001b:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x001b }
            throw r0;
        L_0x001e:
            r3 = 23;
            if (r5 != r3) goto L_0x0027;
        L_0x0022:
            r1 = 0;
            r4.mFire = r1;	 Catch:{ all -> 0x001b }
            monitor-exit(r2);	 Catch:{ all -> 0x001b }
            goto L_0x0011;
        L_0x0027:
            r3 = 19;
            if (r5 != r3) goto L_0x0030;
        L_0x002b:
            r1 = 0;
            r4.mUp = r1;	 Catch:{ all -> 0x001b }
            monitor-exit(r2);	 Catch:{ all -> 0x001b }
            goto L_0x0011;
        L_0x0030:
            monitor-exit(r2);	 Catch:{ all -> 0x001b }
            r0 = r1;
            goto L_0x0011;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.pop.bubble.deluxe.activity.PuzzleGameView.GameThread.doKeyUp(int, android.view.KeyEvent):boolean");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        boolean doTrackballEvent(android.view.MotionEvent r9) {
            /*
            r8 = this;
            r0 = 1;
            r1 = r8.mSurfaceHolder;
            monitor-enter(r1);
            r2 = r8.mMode;	 Catch:{ all -> 0x0029 }
            if (r2 == r0) goto L_0x000c;
        L_0x0008:
            r2 = 1;
            r8.setState(r2);	 Catch:{ all -> 0x0029 }
        L_0x000c:
            r2 = r8.mMode;	 Catch:{ all -> 0x0029 }
            if (r2 != r0) goto L_0x0026;
        L_0x0010:
            r2 = r9.getAction();	 Catch:{ all -> 0x0029 }
            r3 = 2;
            if (r2 != r3) goto L_0x0026;
        L_0x0017:
            r2 = r8.mTrackballDX;	 Catch:{ all -> 0x0029 }
            r4 = r9.getX();	 Catch:{ all -> 0x0029 }
            r4 = (double) r4;	 Catch:{ all -> 0x0029 }
            r6 = 4617315517961601024; // 0x4014000000000000 float:0.0 double:5.0;
            r4 = r4 * r6;
            r2 = r2 + r4;
            r8.mTrackballDX = r2;	 Catch:{ all -> 0x0029 }
            monitor-exit(r1);	 Catch:{ all -> 0x0029 }
        L_0x0025:
            return r0;
        L_0x0026:
            monitor-exit(r1);	 Catch:{ all -> 0x0029 }
            r0 = 0;
            goto L_0x0025;
        L_0x0029:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0029 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.pop.bubble.deluxe.activity.PuzzleGameView.GameThread.doTrackballEvent(android.view.MotionEvent):boolean");
        }

        boolean doTouchEvent(MotionEvent event) {
            synchronized (this.mSurfaceHolder) {
                if (this.mMode != 1) {
                    setState(1);
                }
                double x = (double) event.getX();
                double y = (double) event.getY();
                if (event.getAction() == 0) {
                    this.mTouchDX = new double[]{x, y};
                    this.mTouchFire = true;
                }
            }
            return true;
        }

        private void drawBackground(Canvas c) {
            Sprite.drawImage(this.mBackground, 0, 0, c, this.mDisplayScale, this.mDisplayDX, this.mDisplayDY);
        }

        private void drawLevelNumber(Canvas canvas) {
            int level = this.mLevelManager.getLevelIndex() + 1;
            if (level < 10) {
                this.mFont.paintChar(Character.forDigit(level, 10), 195, 433, canvas, this.mDisplayScale, this.mDisplayDX, this.mDisplayDY);
            } else if (level < 100) {
                this.mFont.paintChar(Character.forDigit(level % 10, 10), 192 + this.mFont.paintChar(Character.forDigit(level / 10, 10), 192, 433, canvas, this.mDisplayScale, this.mDisplayDX, this.mDisplayDY), 433, canvas, this.mDisplayScale, this.mDisplayDX, this.mDisplayDY);
            } else {
                int x = 186 + this.mFont.paintChar(Character.forDigit(level / 100, 10), 186, 433, canvas, this.mDisplayScale, this.mDisplayDX, this.mDisplayDY);
                level -= (level / 100) * 100;
                this.mFont.paintChar(Character.forDigit(level % 10, 10), x + this.mFont.paintChar(Character.forDigit(level / 10, 10), x, 433, canvas, this.mDisplayScale, this.mDisplayDX, this.mDisplayDY), 433, canvas, this.mDisplayScale, this.mDisplayDX, this.mDisplayDY);
            }
        }

        private void doDraw(Canvas canvas) {
            if (this.mImagesReady) {
                if (this.mDisplayDX > 0 || this.mDisplayDY > 0) {
                    canvas.drawRGB(0, 0, 0);
                }
                drawBackground(canvas);
                drawLevelNumber(canvas);
                this.mFrozenGame.paint(canvas, this.mDisplayScale, this.mDisplayDX, this.mDisplayDY);
            }
        }

        private void updateGameState() {
            BubbleShooterGame bubbleShooterGame = this.mFrozenGame;
            boolean z = this.mLeft || this.mWasLeft;
            boolean z2 = this.mRight || this.mWasRight;
            boolean z3 = this.mFire || this.mUp || this.mWasFire || this.mWasUp || this.mTouchFire;
            if (bubbleShooterGame.play(z, z2, z3, this.mTrackballDX, this.mTouchDX)) {
                this.mListener.showAdsNextLevle();
                this.mFrozenGame = new BubbleShooterGame(this.mBackground, this.mBubbles, this.mBubblesBlind, this.mFrozenBubbles, this.mTargetedBubbles, this.mBubbleBlink, this.mGameWon, this.mGameLost, this.mHurry, this.mPenguins, this.mCompressorHead, this.mCompressor, this.mLauncher, this.mSoundManager, this.mLevelManager);
            }
            this.mWasLeft = false;
            this.mWasRight = false;
            this.mWasFire = false;
            this.mWasUp = false;
            this.mTrackballDX = 0.0d;
            this.mTouchFire = false;
            this.mTouchDX = null;
        }

        public void cleanUp() {
            boolean imagesScaled = false;
            synchronized (this.mSurfaceHolder) {
                int i;
                this.mImagesReady = false;
                if (this.mBackgroundOrig == this.mBackground.bmp) {
                    imagesScaled = true;
                }
                this.mBackgroundOrig.recycle();
                this.mBackgroundOrig = null;
                for (i = 0; i < this.mBubblesOrig.length; i++) {
                    this.mBubblesOrig[i].recycle();
                    this.mBubblesOrig[i] = null;
                }
                this.mBubblesOrig = null;
                for (i = 0; i < this.mFrozenBubblesOrig.length; i++) {
                    this.mFrozenBubblesOrig[i].recycle();
                    this.mFrozenBubblesOrig[i] = null;
                }
                this.mFrozenBubblesOrig = null;
                for (i = 0; i < this.mTargetedBubblesOrig.length; i++) {
                    this.mTargetedBubblesOrig[i].recycle();
                    this.mTargetedBubblesOrig[i] = null;
                }
                this.mTargetedBubblesOrig = null;
                this.mBubbleBlinkOrig.recycle();
                this.mBubbleBlinkOrig = null;
                this.mGameWonOrig.recycle();
                this.mGameWonOrig = null;
                this.mGameLostOrig.recycle();
                this.mGameLostOrig = null;
                this.mHurryOrig.recycle();
                this.mHurryOrig = null;
                this.mPenguinsOrig.recycle();
                this.mPenguinsOrig = null;
                this.mCompressorHeadOrig.recycle();
                this.mCompressorHeadOrig = null;
                this.mCompressorOrig.recycle();
                this.mCompressorOrig = null;
                this.mLifeOrig.recycle();
                this.mLifeOrig = null;
                if (imagesScaled) {
                    this.mBackground.bmp.recycle();
                    for (BmpWrap bmpWrap : this.mBubbles) {
                        bmpWrap.bmp.recycle();
                    }
                    for (BmpWrap bmpWrap2 : this.mBubblesBlind) {
                        bmpWrap2.bmp.recycle();
                    }
                    for (BmpWrap bmpWrap22 : this.mFrozenBubbles) {
                        bmpWrap22.bmp.recycle();
                    }
                    for (BmpWrap bmpWrap222 : this.mTargetedBubbles) {
                        bmpWrap222.bmp.recycle();
                    }
                    this.mBubbleBlink.bmp.recycle();
                    this.mGameWon.bmp.recycle();
                    this.mGameLost.bmp.recycle();
                    this.mHurry.bmp.recycle();
                    this.mPenguins.bmp.recycle();
                    this.mCompressorHead.bmp.recycle();
                    this.mCompressor.bmp.recycle();
                    this.mLife.bmp.recycle();
                }
                this.mBackground.bmp = null;
                this.mBackground = null;
                for (i = 0; i < this.mBubbles.length; i++) {
                    this.mBubbles[i].bmp = null;
                    this.mBubbles[i] = null;
                }
                this.mBubbles = null;
                for (i = 0; i < this.mBubblesBlind.length; i++) {
                    this.mBubblesBlind[i].bmp = null;
                    this.mBubblesBlind[i] = null;
                }
                this.mBubblesBlind = null;
                for (i = 0; i < this.mFrozenBubbles.length; i++) {
                    this.mFrozenBubbles[i].bmp = null;
                    this.mFrozenBubbles[i] = null;
                }
                this.mFrozenBubbles = null;
                for (i = 0; i < this.mTargetedBubbles.length; i++) {
                    this.mTargetedBubbles[i].bmp = null;
                    this.mTargetedBubbles[i] = null;
                }
                this.mTargetedBubbles = null;
                this.mBubbleBlink.bmp = null;
                this.mBubbleBlink = null;
                this.mGameWon.bmp = null;
                this.mGameWon = null;
                this.mGameLost.bmp = null;
                this.mGameLost = null;
                this.mHurry.bmp = null;
                this.mHurry = null;
                this.mPenguins.bmp = null;
                this.mPenguins = null;
                this.mCompressorHead.bmp = null;
                this.mCompressorHead = null;
                this.mCompressor.bmp = null;
                this.mCompressor = null;
                this.mLife.bmp = null;
                this.mLife = null;
                this.mImageList = null;
                this.mSoundManager.cleanUp();
                this.mSoundManager = null;
                this.mLevelManager = null;
                this.mFrozenGame = null;
            }
        }
    }

    public PuzzleGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        this.thread = new GameThread(holder, null, 0);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.thread.setRunning(true);
        this.thread.start();
    }

    public PuzzleGameView(Context context, byte[] levels, int startingLevel) {
        super(context);
        this.mContext = context;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        this.thread = new GameThread(holder, levels, startingLevel);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.thread.setRunning(true);
        this.thread.start();
    }

    public GameThread getThread() {
        return this.thread;
    }

    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        return this.thread.doKeyDown(keyCode, msg);
    }

    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        return this.thread.doKeyUp(keyCode, msg);
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return this.thread.doTrackballEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.thread.doTouchEvent(event);
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) {
            this.thread.pause();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.thread.setSurfaceSize(width, height);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.thread.setSurfaceOK(true);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.thread.setSurfaceOK(false);
    }

    public void cleanUp() {
        this.thread.cleanUp();
        this.mContext = null;
    }
}