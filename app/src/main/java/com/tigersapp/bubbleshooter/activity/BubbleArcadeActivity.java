package com.tigersapp.bubbleshooter.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.tigersapp.bubbleshooter.MainApplication;
import com.tigersapp.bubbleshooter.R;
import com.tigersapp.bubbleshooter.arcade.ArcadeGameView;
import com.tigersapp.bubbleshooter.arcade.ScoreManager;
import com.tigersapp.bubbleshooter.utils.AdAdmobCommon;

/**
 * Created by Ripon on 1/4/17.
 */

public class BubbleArcadeActivity extends AppCompatActivity implements HandleNextLevel {

    private static final String EDITORACTION = "com.likeapp.game.bubbleshooter.GAME";
    public static final int GAME_COLORBLIND = 1;
    public static final int GAME_NORMAL = 0;
    public static final int MENU_ABOUT = 10;
    public static final int MENU_COLORBLIND_MODE_OFF = 2;
    public static final int MENU_COLORBLIND_MODE_ON = 1;
    public static final int MENU_DONT_RUSH_ME = 7;
    public static final int MENU_EDITOR = 11;
    public static final int MENU_FULLSCREEN_OFF = 4;
    public static final int MENU_FULLSCREEN_ON = 3;
    public static final int MENU_NEW_GAME = 9;
    public static final int MENU_RUSH_ME = 8;
    public static final int MENU_SOUND_OFF = 6;
    public static final int MENU_SOUND_ON = 5;
    public static final int NUM_SOUNDS = 9;
    public static final String PREFS_LEVEL_KEY_NAME = "level";
    public static final String PREFS_NAME = "frozenbubble_arcade";
    public static final String PREFS_UNLOCK_LEVEL_KEY_NAME = "Unlock_level";
    public static final int SOUND_DESTROY = 3;
    public static final int SOUND_HURRY = 6;
    public static final int SOUND_LAUNCH = 2;
    public static final int SOUND_LOST = 1;
    public static final int SOUND_NEWROOT = 7;
    public static final int SOUND_NOH = 8;
    public static final int SOUND_REBOUND = 4;
    public static final int SOUND_STICK = 5;
    public static final int SOUND_WON = 0;
    private static boolean dontRushMe = false;
    private static int gameMode = 0;
    private boolean activityCustomStarted = false;
    private AdView adView = null;
    private boolean fullscreen = true;
    private ArcadeGameView.GameThread mGameThread;
    private ArcadeGameView mGameView;
    private InterstitialAd mInterstitialAds;

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 9, 0, R.string.menu_new_game);
        menu.add(0, 5, 0, R.string.menu_sound_on);
        menu.add(0, 6, 0, R.string.menu_sound_off);
        return true;
    }

    private void fullScreen(boolean enable) {
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        fullScreen(true);
    }

    private void initAdmobAds() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.adLayout);
        this.adView = new AdView(this);
        this.adView.setAdSize(AdSize.SMART_BANNER);
        this.adView.setAdUnitId(MainApplication.getAppContext().getResources().getString(R.string.ads_id_banner));
        layout.addView(this.adView);
        AdAdmobCommon.setBanner(this.adView);
        this.adView.setBackgroundColor(getResources().getColor(R.color.transparent));
        this.mInterstitialAds = new InterstitialAd(this);
        AdAdmobCommon.setInterstitialAd(this.mInterstitialAds, this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean z;
        super.onPrepareOptionsMenu(menu);
        MenuItem findItem = menu.findItem(5);
        if (getSoundOn()) {
            z = false;
        } else {
            z = true;
        }
        findItem.setVisible(z);
        menu.findItem(6).setVisible(getSoundOn());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                setMode(1);
                return true;
            case 2:
                setMode(0);
                return true;
            case 3:
                this.fullscreen = true;
                setFullscreen();
                return true;
            case 4:
                this.fullscreen = false;
                setFullscreen();
                return true;
            case 5:
                setSoundOn(true);
                return true;
            case 6:
                setSoundOn(false);
                return true;
            case 7:
                setDontRushMe(true);
                return true;
            case 8:
                setDontRushMe(false);
                return true;
            case 9:
                handleNewGame();
                return true;
            case 10:
                this.mGameView.getThread().setState(4);
                return true;
            case 11:
                return true;
            default:
                return false;
        }
    }

    private void handleNewGame() {
        new AlertDialog.Builder(this).setTitle(R.string.new_game_dialog_title).setMessage(R.string.new_game_dialog_content).setPositiveButton(R.string.new_game_dialog_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                BubbleArcadeActivity.this.mGameThread.newGame();
            }
        }).setNegativeButton(R.string.new_game_dialog_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    private void setFullscreen() {
        this.mGameView.requestLayout();
    }

    public static synchronized void setMode(int newMode) {
        synchronized (BubbleArcadeActivity.class) {
            gameMode = newMode;
        }
    }

    public static synchronized int getMode() {
        int i;
        synchronized (BubbleArcadeActivity.class) {
            i = gameMode;
        }
        return i;
    }

    public static synchronized boolean getSoundOn() {
        boolean isSoundOn;
        synchronized (BubbleArcadeActivity.class) {
            isSoundOn = GameConfig.getInstance().isSoundOn();
        }
        return isSoundOn;
    }

    public static synchronized void setSoundOn(boolean so) {
        synchronized (BubbleArcadeActivity.class) {
            GameConfig.getInstance().setSoundOn(so);
        }
    }

    public static synchronized boolean getDontRushMe() {
        boolean z;
        synchronized (BubbleArcadeActivity.class) {
            z = dontRushMe;
        }
        return z;
    }

    public static synchronized void setDontRushMe(boolean dont) {
        synchronized (BubbleArcadeActivity.class) {
            dontRushMe = dont;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        Intent i = getIntent();
        if (i == null || i.getExtras() == null || !i.getExtras().containsKey("levels")) {
            this.activityCustomStarted = false;
            setContentView(R.layout.arcade_main);
            this.mGameView = (ArcadeGameView) findViewById(R.id.game);
            initAdmobAds();
        } else {
            int startingLevel = getSharedPreferences(PREFS_NAME, 0).getInt("levelCustom", 0);
            int startingLevelIntent = i.getIntExtra("startingLevel", -2);
            if (startingLevelIntent != -2) {
                startingLevel = startingLevelIntent;
            }
            this.activityCustomStarted = true;
            this.mGameView = new ArcadeGameView(this, i.getExtras().getByteArray("levels"), startingLevel);
            setContentView(this.mGameView);
        }
        this.mGameThread = this.mGameView.getThread();
        this.mGameThread.setListener(this);
        if (savedInstanceState != null) {
            this.mGameThread.restoreState(savedInstanceState);
        }
        this.mGameView.requestFocus();
        setFullscreen();
        GameConfig.getInstance().init(this);
        GameConfig.getInstance().setGameMode(GameConfig.GameMode.ArcadeMode);
        ScoreManager.getInstance().init(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mGameThread.saveState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null && EDITORACTION.equals(intent.getAction()) && !this.activityCustomStarted) {
            this.activityCustomStarted = true;
            int startingLevel = getSharedPreferences(PREFS_NAME, 0).getInt("levelCustom", 0);
            int startingLevelIntent = intent.getIntExtra("startingLevel", -2);
            if (startingLevelIntent != -2) {
                startingLevel = startingLevelIntent;
            }
            this.mGameView = null;
            this.mGameView = new ArcadeGameView(this, intent.getExtras().getByteArray("levels"), startingLevel);
            setContentView(this.mGameView);
            this.mGameThread = this.mGameView.getThread();
            this.mGameThread.newGame();
            this.mGameView.requestFocus();
            setFullscreen();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.adView.resume();
    }

    @Override
    protected void onDestroy() {
        this.adView.destroy();
        super.onDestroy();
        if (this.mGameView != null) {
            this.mGameView.cleanUp();
        }
        this.mGameView = null;
        this.mGameThread = null;
        ScoreManager.getInstance().save();
    }

    @Override
    protected void onPause() {
        this.adView.pause();
        super.onPause();
        this.mGameView.getThread().pause();
        SharedPreferences.Editor editor;
        if (getIntent() == null || !this.activityCustomStarted) {
            SharedPreferences sp = getSharedPreferences(PREFS_NAME, 0);
            int unLockLevel = sp.getInt("Unlock_level", 0);
            int level = this.mGameThread.getCurrentLevelIndex();
            editor = sp.edit();
            editor.putInt("level", this.mGameThread.getCurrentLevelIndex());
            if (level > unLockLevel) {
                editor.putInt("Unlock_level", level);
            }
            editor.commit();
            return;
        }
        editor = getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putInt("levelCustom", this.mGameThread.getCurrentLevelIndex());
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public void showAdsNextLevle() {
        runOnUiThread(new Runnable() {
            public void run() {
                AdAdmobCommon.showInterstitial(BubbleArcadeActivity.this.mInterstitialAds);
            }
        });
    }
}
