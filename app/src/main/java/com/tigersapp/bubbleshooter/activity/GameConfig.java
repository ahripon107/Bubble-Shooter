package com.tigersapp.bubbleshooter.activity;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ripon on 1/4/17.
 */

public class GameConfig {

    public static final String PREF_GAME_MODE = "game_mode";
    public static final String PREF_ROOT = "com.lvstudios.marble.bubblespace.config";
    public static final String PREF_SOUND = "sound";
    public static GameConfig instance;
    private SharedPreferences sp;

    public enum GameMode {
        PuzzleMode,
        ArcadeMode
    }

    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    public void init(Context context) {
        this.sp = context.getSharedPreferences(PREF_ROOT, 0);
    }

    public GameMode getGameMode() {
        return GameMode.valueOf(this.sp.getString(PREF_GAME_MODE, GameMode.PuzzleMode.name()));
    }

    public void setGameMode(GameMode mode) {
        put(PREF_GAME_MODE, mode.name());
    }

    public boolean isSoundOn() {
        return get(PREF_SOUND, true);
    }

    public void setSoundOn(boolean val) {
        put(PREF_SOUND, val);
    }

    public int get(String key, int defaultVal) {
        return this.sp.getInt(key, defaultVal);
    }

    public void put(String key, int val) {
        this.sp.edit().putInt(key, val).commit();
    }

    public long get(String key, long defaultVal) {
        return this.sp.getLong(key, defaultVal);
    }

    public void put(String key, long val) {
        this.sp.edit().putLong(key, val).commit();
    }

    public boolean get(String key, boolean defaultVal) {
        return this.sp.getBoolean(key, defaultVal);
    }

    public void put(String key, boolean val) {
        this.sp.edit().putBoolean(key, val).commit();
    }

    public void put(String key, String val) {
        this.sp.edit().putString(key, val).commit();
    }
}
