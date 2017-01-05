package com.tigersapp.bubbleshooter.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ripon on 1/5/17.
 */

public class LevelManager {

    public static final int MAX_LEVEL_NUM = 640;
    public final String LINE_SEPARATOR2 = new StringBuilder(String.valueOf(System.getProperty("line.separator"))).append(System.getProperty("line.separator")).toString();
    private final int PAGE_SIZE_OF_LEVEL = 10;
    private int currentLevel;
    private Map<String, byte[][]> lvlsMap = new HashMap();
    private Context mContext;
    private int maxFixedBubbleCount = 0;
    private int page;

    public int getMaxFixedBubbleCount() {
        if (this.maxFixedBubbleCount == 0) {
            if (this.currentLevel + 1 > 900) {
                this.maxFixedBubbleCount = 5;
            } else if (this.currentLevel + 1 > 800) {
                this.maxFixedBubbleCount = 6;
            } else if (this.currentLevel + 1 > 640) {
                this.maxFixedBubbleCount = 7;
            } else {
                this.maxFixedBubbleCount = 8;
            }
        }
        return this.maxFixedBubbleCount;
    }

    public void saveState(Bundle map) {
        map.putInt("LevelManager-currentLevel", this.currentLevel);
    }

    public void restoreState(Bundle map) {
        this.currentLevel = map.getInt("LevelManager-currentLevel");
    }

    private String getLevelFileName(int startingLevel) {
        this.page = ((startingLevel + 1) / 10) + 1;
        if ((startingLevel + 1) % 10 == 0) {
            this.page--;
        }
        return "lvl2/" + this.page + ".txt";
    }

    public LevelManager(Context mContext, int startingLevel) {
        this.currentLevel = startingLevel;
        this.mContext = mContext;
        load();
    }

    private void load() {
        this.lvlsMap.clear();
        try {
            InputStream is = this.mContext.getAssets().open(getLevelFileName(this.currentLevel));
            byte[] levels = new byte[is.available()];
            is.read(levels);
            is.close();
            parse(levels);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parse(byte[] levels) {
        String allLevels = new String(levels).replaceAll("\r", "");
        int nextLevel = allLevels.indexOf(this.LINE_SEPARATOR2);
        if (nextLevel == -1 && allLevels.trim().length() != 0) {
            nextLevel = allLevels.length();
        }
        int startNum = 0;
        while (nextLevel != -1) {
            String currentLevel = allLevels.substring(0, nextLevel).trim();
            this.lvlsMap.put(String.valueOf((((this.page - 1) * 10) + startNum)), getLevel(currentLevel));
            allLevels = allLevels.substring(nextLevel).trim();
            if (allLevels.length() == 0) {
                nextLevel = -1;
            } else {
                nextLevel = allLevels.indexOf(this.LINE_SEPARATOR2);
                if (nextLevel == -1) {
                    nextLevel = allLevels.length();
                }
            }
            startNum++;
        }
        if (this.currentLevel >= 640) {
            this.currentLevel = 0;
        }
    }

    private byte[][] getLevel(String data) {
        int i;
        byte[][] temp = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{8, 12});
        for (int j = 0; j < 12; j++) {
            for (i = 0; i < 8; i++) {
                temp[i][j] = (byte) -1;
            }
        }
        int tempX = 0;
        int tempY = 0;
        i = 0;
        while (i < data.length()) {
            if (data.charAt(i) >= '0' && data.charAt(i) <= '7') {
                temp[tempX][tempY] = (byte) (data.charAt(i) - 48);
                tempX++;
            } else if (data.charAt(i) == '-') {
                temp[tempX][tempY] = (byte) -1;
                tempX++;
            }
            if (tempX == 8) {
                tempY++;
                if (tempY == 12) {
                    break;
                }
                tempX = tempY % 2;
            }
            i++;
        }
        return temp;
    }

    public byte[][] getCurrentLevel() {
        if (this.lvlsMap == null || !this.lvlsMap.containsKey(new StringBuilder(String.valueOf(this.currentLevel)).toString())) {
            load();
        }
        if (this.currentLevel < 640) {
            return (byte[][]) this.lvlsMap.get(String.valueOf(this.currentLevel));
        }
        return null;
    }

    public void goToNextLevel() {
        SharedPreferences sp = this.mContext.getSharedPreferences(BubbleShooterActivity.PREFS_NAME, 0);
        this.currentLevel = sp.getInt("level", 0);
        int maxLevel = sp.getInt("Unlock_level", 0);
        this.currentLevel++;
        if (maxLevel <= this.currentLevel) {
            maxLevel = this.currentLevel;
        }
        sp.edit().putInt("level", this.currentLevel).putInt("Unlock_level", maxLevel).commit();
        if (this.currentLevel >= 640) {
            this.currentLevel = 0;
        }
    }

    public void goToFirstLevel() {
        this.currentLevel = 0;
    }

    public int getLevelIndex() {
        return this.currentLevel;
    }
}
