package com.tigersapp.bubbleshooter.arcade;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tigersapp.bubbleshooter.activity.BubbleArcadeActivity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ripon on 1/5/17.
 */

public class ArcadeLevelManager {

    public static final int DEFAULT_MAX_COL = 8;
    public static final int DEFAULT_MAX_ROW = 12;
    public static final int MAX_LEVEL_NUM = 100;
    private int currentLevel;
    private Map<Integer, ArcadeLevel> levelMap = new HashMap();
    private Context mContext;

    class ArcadeLevel {
        private byte[][] level;
        private int mHeight = 12;
        private float mSpeed = 0.4f;
        private int mWidth = 8;

        public ArcadeLevel(int height, String[] entry) {
            this.mSpeed = Float.valueOf(entry[0]).floatValue();
            this.mHeight = height;
            initLevel(entry[1]);
        }

        public int getHeight() {
            return this.mHeight;
        }

        public byte[][] getOriginLevel() {
            return this.level;
        }

        public float getSpeed() {
            return this.mSpeed;
        }

        public int getWidth() {
            return this.mWidth;
        }

        private void initLevel(String data) {
            int i;
            this.level = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{this.mWidth, this.mHeight});
            for (int j = 0; j < this.mHeight; j++) {
                for (i = 0; i < this.mWidth; i++) {
                    this.level[i][j] = (byte) -1;
                }
            }
            int tempX = 0;
            int tempY = 0;
            i = 0;
            while (i < data.length()) {
                if (data.charAt(i) >= '0' && data.charAt(i) <= '7') {
                    this.level[tempX][tempY] = (byte) (data.charAt(i) - 48);
                    tempX++;
                } else if (data.charAt(i) == '-') {
                    this.level[tempX][tempY] = (byte) -1;
                    tempX++;
                }
                if (tempX == 8) {
                    tempY++;
                    if (tempY != this.mHeight) {
                        tempX = tempY % 2;
                    } else {
                        return;
                    }
                }
                i++;
            }
        }
    }

    public void saveState(Bundle map) {
        map.putInt("LevelManager-currentLevel", this.currentLevel);
    }

    public void restoreState(Bundle map) {
        this.currentLevel = map.getInt("LevelManager-currentLevel");
    }

    public ArcadeLevelManager(Context mContext, int startingLevel) {
        this.currentLevel = startingLevel;
        this.mContext = mContext;
        load();
    }

    private void load() {
        this.levelMap.clear();
        try {
            InputStream is = this.mContext.getAssets().open("levels_arcade/levels.txt");
            byte[] levels = new byte[is.available()];
            is.read(levels);
            is.close();
            parse(levels);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parse(byte[] levels) {
        String[] levelArray = new String(levels).split("\n");
        for (int index = 0; index < levelArray.length; index++) {
            this.levelMap.put(Integer.valueOf(index), new ArcadeLevel(levelArray[index].length() / 8, levelArray[index].split(" ")));
        }
        if (this.currentLevel >= 100) {
            this.currentLevel = 0;
        }
    }

    public byte[][] getCurrentLevel() {
        if (this.levelMap == null || !this.levelMap.containsKey(Integer.valueOf(this.currentLevel))) {
            load();
        }
        if (this.currentLevel < 100) {
            return ((ArcadeLevel) this.levelMap.get(Integer.valueOf(this.currentLevel))).getOriginLevel();
        }
        return null;
    }

    public void goToNextLevel() {
        SharedPreferences sp = this.mContext.getSharedPreferences(BubbleArcadeActivity.PREFS_NAME, 0);
        this.currentLevel = sp.getInt("level", 0);
        int maxLevel = sp.getInt("Unlock_level", 0);
        this.currentLevel++;
        if (maxLevel <= this.currentLevel) {
            maxLevel = this.currentLevel;
        }
        sp.edit().putInt("level", this.currentLevel).putInt("Unlock_level", maxLevel).commit();
        if (this.currentLevel >= 100) {
            this.currentLevel = 0;
        }
    }

    public void goToFirstLevel() {
        this.currentLevel = 0;
    }

    public int getLevelIndex() {
        return this.currentLevel;
    }

    public int getLevelRows() {
        return ((ArcadeLevel) this.levelMap.get(Integer.valueOf(this.currentLevel))).getHeight();
    }

    public float getCurrentSpeed() {
        return ((ArcadeLevel) this.levelMap.get(Integer.valueOf(this.currentLevel))).getSpeed();
    }

    public int getWidth() {
        return ((ArcadeLevel) this.levelMap.get(Integer.valueOf(this.currentLevel))).getWidth();
    }
}
