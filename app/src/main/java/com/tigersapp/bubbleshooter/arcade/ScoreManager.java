package com.tigersapp.bubbleshooter.arcade;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.tigersapp.bubbleshooter.activity.BubbleShooterActivity;
import com.tigersapp.bubbleshooter.activity.GameConfig;

/**
 * Created by Ripon on 1/5/17.
 */

public class ScoreManager {

    public static final String PREF_HIGH_SCORE = "high_score";
    public static final String PREF_SCORE = "score";
    private static ScoreManager instance;
    private static boolean isInit = false;
    private long bestScore;
    private AppCompatActivity currentActivity;
    private long score;
    private long tempScore;

    public static final class HONOR {
        public static final String Champion = "2365";
        public static final String GXYM = "390483cb766fc94d";
        public static final String GrandMaster = "2369";
        public static final String LHCQ = "8bdb71a3dbdeb067";
        public static final String Master = "2367";
        public static final String ZCYP = "dba85bae0a4e33d3";

        public static void checkCXZL(AppCompatActivity act, int maxLevel, long score) {
        }

        public static void checkDTRS(AppCompatActivity act, int maxLevel, long score) {
            if (maxLevel >= 100) {
            }
        }

        public static void checkYZCS(AppCompatActivity act, int maxLevel, long score) {
            if (maxLevel >= 200) {
            }
        }

        public static void checkJRJJ(AppCompatActivity act, int maxLevel, long score) {
            if (maxLevel >= ArcadeGameView.GameThread.GAMEFIELD_WIDTH) {
            }
        }

        public static void checkLHCQ(AppCompatActivity act, int maxLevel, long score) {
            if (maxLevel >= 400) {
            }
        }

        public static void checkZCYP(AppCompatActivity act, int maxLevel, long score) {
            if (maxLevel >= 500) {
            }
        }

        public static void checkGXYM(AppCompatActivity act, int maxLevel, long score) {
            if (maxLevel >= 630) {
            }
        }
    }

    private ScoreManager() {
    }

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    public void init(AppCompatActivity activity) {
        this.currentActivity = activity;
        this.score = GameConfig.getInstance().get(PREF_SCORE, 0);
        this.bestScore = GameConfig.getInstance().get(PREF_HIGH_SCORE, 0);
        this.tempScore = 0;
    }

    public void init(Activity activity) {
        this.currentActivity = (AppCompatActivity) activity;
        this.score = GameConfig.getInstance().get(PREF_SCORE, 0);
        this.bestScore = GameConfig.getInstance().get(PREF_HIGH_SCORE, 0);
        this.tempScore = 0;
    }

    public void initGameCenter(AppCompatActivity act) {
        if (!isInit) {
            isInit = true;
        }
    }

    public void addTempScore(long s) {
        this.tempScore += s;
    }

    public void addToTotalScore() {
        addScore(this.tempScore);
        submitToBestScore();
    }

    public void clearTempScore() {
        this.tempScore = 0;
    }

    private void addScore(long s) {
        this.score += s;
    }

    public void reset() {
        clearTempScore();
        this.score = 0;
        save();
    }

    public long getScore() {
        return this.score + this.tempScore;
    }

    public long getBestScore() {
        return this.bestScore;
    }

    public void save() {
        GameConfig.getInstance().put(PREF_SCORE, this.score);
        GameConfig.getInstance().put(PREF_HIGH_SCORE, this.bestScore);
    }

    private void submitToBestScore() {
        if (this.score > this.bestScore) {
            this.bestScore = this.score;
        }
        save();
    }

    public void submitArcadeThenOpenLeadboardActivity(AppCompatActivity activity) {
        initGameCenter(activity);
        String leaderborderId = "";
        String leaderbordName = "";
    }

    public void submitPuzzleThenOpenLeadboardActivity(AppCompatActivity activity) {
        initGameCenter(activity);
        String leaderborderId = "";
        String leaderbordName = "";
        if (activity.getSharedPreferences(BubbleShooterActivity.PREFS_NAME, 0).getInt("Unlock_level", 0) <= 640) {
        }
    }

    public void checkHonor(AppCompatActivity act) {
        initGameCenter(act);
        int maxLevel = act.getSharedPreferences(BubbleShooterActivity.PREFS_NAME, 0).getInt("Unlock_level", 0);
        long score = getBestScore();
        HONOR.checkCXZL(act, maxLevel, score);
        HONOR.checkDTRS(act, maxLevel, score);
        HONOR.checkYZCS(act, maxLevel, score);
        HONOR.checkJRJJ(act, maxLevel, score);
        HONOR.checkLHCQ(act, maxLevel, score);
        HONOR.checkZCYP(act, maxLevel, score);
        HONOR.checkGXYM(act, maxLevel, score);
    }

    public void openHonor(AppCompatActivity activity) {
        initGameCenter(activity);
    }
}
