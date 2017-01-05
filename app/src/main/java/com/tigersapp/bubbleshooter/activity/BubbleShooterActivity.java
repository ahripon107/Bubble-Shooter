package com.tigersapp.bubbleshooter.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.tigersapp.bubbleshooter.MainApplication;
import com.tigersapp.bubbleshooter.R;
import com.tigersapp.bubbleshooter.utils.AdAdmobCommon;

/**
 * Created by Ripon on 1/5/17.
 */

public class BubbleShooterActivity extends AppCompatActivity implements HandleNextLevel {
    private static final String EDITORACTION = "BubbleShooterActivity";
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
    public static final int MENU_PASS_BY_CREDIT = 12;
    public static final int MENU_PICK_LEVEL = 13;
    public static final int MENU_RUSH_ME = 8;
    public static final int MENU_SOUND_OFF = 6;
    public static final int MENU_SOUND_ON = 5;
    public static final int NUM_SOUNDS = 9;
    private static final int PASS_LEVEL_NEED_SHOOT_NUMBER = 2000;
    public static final String PREFS_LEVEL_KEY_NAME = "level";
    public static final String PREFS_NAME = "frozenbubble";
    public static final String PREFS_UNLOCK_LEVEL_KEY_NAME = "Unlock_level";
    public static final String SHOOT_NUMBER_OF_SKIP_CHANCE_KEY = "shootNumberOfSkipChanceKey";
    public static final String SKIP_CHANCE_KEY = "skipChance";
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
    private static boolean soundOn = true;
    private boolean activityCustomStarted = false;
    private AdView adView = null;
    private boolean fullscreen = true;
    private PuzzleGameView.GameThread mGameThread;
    private PuzzleGameView mGameView;
    private InterstitialAd mInterstitialAds;
    private SharedPreferences sp;

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 9, 0, R.string.menu_new_game);
        menu.add(0, 5, 0, R.string.menu_sound_on);
        menu.add(0, 6, 0, R.string.menu_sound_off);
        menu.add(0, 12, 0, R.string.menu_pass_by_credit);
        menu.add(0, 13, 0, R.string.menu_pick_level);
        return true;
    }

    private void fullScreen(boolean enable) {
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        fullScreen(true);
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
        if (GameConfig.getInstance().get(SHOOT_NUMBER_OF_SKIP_CHANCE_KEY, 0) >= 2000) {
            GameConfig.getInstance().put(SHOOT_NUMBER_OF_SKIP_CHANCE_KEY, 0);
            GameConfig.getInstance().put(SKIP_CHANCE_KEY, 1);
        }
        int skipChance = GameConfig.getInstance().get(SKIP_CHANCE_KEY, 0);
        menu.findItem(12).setTitle(String.format(getString(R.string.menu_pass_by_credit), new Object[]{Integer.valueOf(skipChance)}));
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
                this.mGameThread.replayGame();
                return true;
            case 10:
                this.mGameView.getThread().setState(4);
                return true;
            case 11:
                startEditor();
                return true;
            case 12:
                int startingLevel = this.sp.getInt("level", 0);
                int maxLevel = this.sp.getInt("Unlock_level", 0);
                int chance = GameConfig.getInstance().get(SKIP_CHANCE_KEY, 0);
                long shootNumber = GameConfig.getInstance().get(SHOOT_NUMBER_OF_SKIP_CHANCE_KEY, 0);
                if (maxLevel > startingLevel || chance > 0) {
                    if (chance > 0 && maxLevel <= startingLevel) {
                        GameConfig.getInstance().put(SKIP_CHANCE_KEY, chance - 1);
                    }
                    this.mGameView.getThread().nextLevel();
                    return true;
                }
                showPointMessageBox(getResources().getString(R.string.app_point_dialog_lesspoint_msg_title), String.format(getResources().getString(R.string.app_point_dialog_lesspoint_msg_content), new Object[]{Integer.valueOf(2000), Long.valueOf(shootNumber)}));
                return true;
            case 13:
                startActivity(new Intent(this, LevelSelectorActivity.class));
                return true;
            default:
                return false;
        }
    }

    private void setFullscreen() {
        this.mGameView.requestLayout();
    }

    public static synchronized void setMode(int newMode) {
        synchronized (BubbleShooterActivity.class) {
            gameMode = newMode;
        }
    }

    public static synchronized int getMode() {
        int i;
        synchronized (BubbleShooterActivity.class) {
            i = gameMode;
        }
        return i;
    }

    public static synchronized boolean getSoundOn() {
        boolean isSoundOn;
        synchronized (BubbleShooterActivity.class) {
            isSoundOn = GameConfig.getInstance().isSoundOn();
        }
        return isSoundOn;
    }

    public static synchronized void setSoundOn(boolean so) {
        synchronized (BubbleShooterActivity.class) {
            GameConfig.getInstance().setSoundOn(so);
        }
    }

    public static synchronized boolean getDontRushMe() {
        boolean z;
        synchronized (BubbleShooterActivity.class) {
            z = dontRushMe;
        }
        return z;
    }

    public static synchronized void setDontRushMe(boolean dont) {
        synchronized (BubbleShooterActivity.class) {
            dontRushMe = dont;
        }
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        this.sp = getSharedPreferences(PREFS_NAME, 0);
        Intent i = getIntent();
        if (i == null || i.getExtras() == null || !i.getExtras().containsKey("levels")) {
            this.activityCustomStarted = false;
            setContentView(R.layout.main);
            this.mGameView = (PuzzleGameView) findViewById(R.id.game);
            initAdmobAds();
        } else {
            int startingLevel = this.sp.getInt("levelCustom", 0);
            int startingLevelIntent = i.getIntExtra("startingLevel", -2);
            if (startingLevelIntent != -2) {
                startingLevel = startingLevelIntent;
            }
            this.activityCustomStarted = true;
            initAdmobAds();
            this.mGameView = new PuzzleGameView(this, i.getExtras().getByteArray("levels"), startingLevel);
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
        GameConfig.getInstance().setGameMode(GameConfig.GameMode.PuzzleMode);
    }

    protected void onResume() {
        super.onResume();
        this.adView.resume();
        if (GameConfig.getInstance().get(SHOOT_NUMBER_OF_SKIP_CHANCE_KEY, 0) >= 2000) {
            GameConfig.getInstance().put(SHOOT_NUMBER_OF_SKIP_CHANCE_KEY, 0);
            GameConfig.getInstance().put(SKIP_CHANCE_KEY, 1);
        }
    }

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

    protected void onStop() {
        EasyTracker.getInstance().activityStop(this);
        super.onStop();
    }

    protected void onDestroy() {
        this.adView.destroy();
        super.onDestroy();
        if (this.mGameView != null) {
            this.mGameView.cleanUp();
        }
        this.mGameView = null;
        this.mGameThread = null;
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mGameThread.saveState(outState);
    }

    protected void onNewIntent(Intent intent) {
        if (intent != null && EDITORACTION.equals(intent.getAction()) && !this.activityCustomStarted) {
            this.activityCustomStarted = true;
            int startingLevel = getSharedPreferences(PREFS_NAME, 0).getInt("levelCustom", 0);
            int startingLevelIntent = intent.getIntExtra("startingLevel", -2);
            if (startingLevelIntent != -2) {
                startingLevel = startingLevelIntent;
            }
            this.mGameView = null;
            this.mGameView = new PuzzleGameView(this, intent.getExtras().getByteArray("levels"), startingLevel);
            setContentView(this.mGameView);
            this.mGameThread = this.mGameView.getThread();
            this.mGameThread.newGame();
            this.mGameView.requestFocus();
            setFullscreen();
        }
    }

    private void startEditor() {
        Intent i = new Intent();
        i.setClassName("sk.halmi.fbeditplus", "sk.halmi.fbeditplus.EditorActivity");
        try {
            startActivity(i);
            finish();
        } catch (ActivityNotFoundException e) {
            i.setClassName("sk.halmi.fbedit", "sk.halmi.fbedit.EditorActivity");
            try {
                startActivity(i);
                finish();
            } catch (ActivityNotFoundException e2) {
                try {
                    Toast.makeText(getApplicationContext(), R.string.install_editor, Toast.LENGTH_LONG).show();
                    Intent i2 = new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=frozen bubble level editor"));
                    try {
                        startActivity(i2);
                        i = i2;
                    } catch (Exception e3) {
                        i = i2;
                        Toast.makeText(getApplicationContext(), R.string.market_missing, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e4) {
                    Toast.makeText(getApplicationContext(), R.string.market_missing, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void showPointMessageBox(String title, String msg) {
        try {
            AlertDialog dialog = new AlertDialog.Builder(this).setTitle(title).setMessage(msg).create();
            dialog.setButton(1,getResources().getString(R.string.app_point_dialog_continue), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public void showAdsNextLevle() {
        runOnUiThread(new Runnable() {
            public void run() {
                AdAdmobCommon.showInterstitial(BubbleShooterActivity.this.mInterstitialAds);
            }
        });
    }
}