package com.tigersapp.bubbleshooter.arcade;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.tigersapp.bubbleshooter.activity.BmpWrap;
import com.tigersapp.bubbleshooter.activity.BubbleArcadeActivity;
import com.tigersapp.bubbleshooter.activity.ImageSprite;
import com.tigersapp.bubbleshooter.activity.LaunchBubbleSprite;
import com.tigersapp.bubbleshooter.activity.PenguinSprite;
import com.tigersapp.bubbleshooter.activity.SoundManager;
import com.tigersapp.bubbleshooter.activity.Sprite;
import com.tigersapp.bubbleshooter.manager.BubbleManager;
import com.tigersapp.bubbleshooter.manager.PuzzleGameScreen;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.Vector;

/**
 * Created by Ripon on 1/5/17.
 */

public class ArcadeGame extends PuzzleGameScreen {
    public static final int FIRE = 1;
    public static final int HORIZONTAL_MOVE = 0;
    public static final int KEY_LEFT = 37;
    public static final int KEY_M = 77;
    public static final int KEY_RIGHT = 39;
    public static final int KEY_S = 83;
    public static final int KEY_SHIFT = 16;
    public static final int KEY_UP = 38;
    private static final int MIN_LINE_Y = 360;
    public static String PARAMETER_OFFLINE = "offline";
    public static String PARAMETER_PLAYER = "player";
    BmpWrap background;
    int blinkDelay;
    BmpWrap bubbleBlink;
    BubbleManager bubbleManager;
    ArcadeBubbleSprite[][] bubblePlay;
    BmpWrap[] bubbles;
    BmpWrap[] bubblesBlind;
    ArcadeCompressor compressor;
    int currentColor;
    boolean endOfGame;
    Vector falling;
    int fixedBubbles;
    BmpWrap[] frozenBubbles;
    boolean frozenify;
    int frozenifyX;
    int frozenifyY;
    BmpWrap gameLost;
    BmpWrap gameWon;
    ImageSprite hurrySprite;
    int hurryTime;
    Vector jumping;
    LaunchBubbleSprite launchBubble;
    double launchBubblePosition;
    Drawable launcher;
    boolean levelCompleted = false;
    ArcadeLevelManager levelManager;
    private int levelRows;
    boolean modeKeyPressed;
    double moveDown;
    private float moveDownDelta;
    ArcadeBubbleSprite movingBubble;
    int nbBubbles;
    ImageSprite nextBubble;
    int nextColor;
    PenguinSprite penguin;
    BmpWrap penguins;
    Random random = new Random(System.currentTimeMillis());
    boolean readyToFire;
    boolean soundKeyPressed;
    SoundManager soundManager;
    BmpWrap[] targetedBubbles;

    public enum gameState {
        RUNING,
        LOST,
        WIN
    }

    public ArcadeGame(BmpWrap background_arg, BmpWrap[] bubbles_arg, BmpWrap[] bubblesBlind_arg, BmpWrap[] frozenBubbles_arg, BmpWrap[] targetedBubbles_arg, BmpWrap bubbleBlink_arg, BmpWrap gameWon_arg, BmpWrap gameLost_arg, BmpWrap hurry_arg, BmpWrap penguins_arg, BmpWrap compressorHead_arg, BmpWrap compressor_arg, Drawable launcher_arg, SoundManager soundManager_arg, ArcadeLevelManager levelManager_arg) {
        this.launcher = launcher_arg;
        this.penguins = penguins_arg;
        this.background = background_arg;
        this.bubbles = bubbles_arg;
        this.bubblesBlind = bubblesBlind_arg;
        this.frozenBubbles = frozenBubbles_arg;
        this.targetedBubbles = targetedBubbles_arg;
        this.bubbleBlink = bubbleBlink_arg;
        this.gameWon = gameWon_arg;
        this.gameLost = gameLost_arg;
        this.soundManager = soundManager_arg;
        this.levelManager = levelManager_arg;
        this.levelRows = this.levelManager.getLevelRows();
        this.moveDownDelta = this.levelManager.getCurrentSpeed();
        this.launchBubblePosition = 20.0d;
        this.penguin = new PenguinSprite(penguins_arg, this.random);
        addSprite(this.penguin);
        this.compressor = new ArcadeCompressor(compressorHead_arg, compressor_arg, 380.0f - (28.0f * ((float) this.levelRows)));
        this.hurrySprite = new ImageSprite(new Rect(203, 265, 443, 355), hurry_arg);
        this.jumping = new Vector();
        this.falling = new Vector();
        this.bubblePlay = (ArcadeBubbleSprite[][]) Array.newInstance(ArcadeBubbleSprite.class, new int[]{8, this.levelRows + 1});
        this.bubbleManager = new BubbleManager(this.bubbles);
        byte[][] currentLevel = this.levelManager.getCurrentLevel();
        if (currentLevel != null) {
            int cols = this.levelManager.getWidth();
            for (int j = 0; j < this.levelRows; j++) {
                for (int i = j % 2; i < cols; i++) {
                    if (currentLevel[i][j] != (byte) -1) {
                        ArcadeBubbleSprite newOne = new ArcadeBubbleSprite(new Rect(((i * 32) + 190) - ((j % 2) * 16), 380 - ((this.levelRows - j) * 28), 32, 32), currentLevel[i][j], this.bubbles[currentLevel[i][j]], this.bubblesBlind[currentLevel[i][j]], this.frozenBubbles[currentLevel[i][j]], this.bubbleBlink, this.bubbleManager, this.soundManager, this);
                        this.bubblePlay[i][j] = newOne;
                        addSprite(newOne);
                    }
                }
            }
            this.currentColor = this.bubbleManager.nextBubbleIndex(this.random);
            this.nextColor = this.bubbleManager.nextBubbleIndex(this.random);
            if (BubbleArcadeActivity.getMode() == 0) {
                this.nextBubble = new ImageSprite(new Rect(302, 440, 334, 472), this.bubbles[this.nextColor]);
            } else {
                this.nextBubble = new ImageSprite(new Rect(302, 440, 334, 472), this.bubblesBlind[this.nextColor]);
            }
            addSprite(this.nextBubble);
            this.launchBubble = new LaunchBubbleSprite(this.currentColor, (int) this.launchBubblePosition, this.launcher, this.bubbles, this.bubblesBlind);
            spriteToBack(this.launchBubble);
            this.nbBubbles = 0;
        }
    }

    public void saveState(Bundle map) {
        int i;
        Vector savedSprites = new Vector();
        saveSprites(map, savedSprites);
        for (i = 0; i < this.jumping.size(); i++) {
            ((Sprite) this.jumping.elementAt(i)).saveState(map, savedSprites);
            map.putInt(String.format("jumping-%d", new Object[]{Integer.valueOf(i)}), ((Sprite) this.jumping.elementAt(i)).getSavedId());
        }
        map.putInt("numJumpingSprites", this.jumping.size());
        for (i = 0; i < this.falling.size(); i++) {
            ((Sprite) this.falling.elementAt(i)).saveState(map, savedSprites);
            map.putInt(String.format("falling-%d", new Object[]{Integer.valueOf(i)}), ((Sprite) this.falling.elementAt(i)).getSavedId());
        }
        map.putInt("numFallingSprites", this.falling.size());
        for (i = 0; i < 8; i++) {
            for (int j = 0; j < this.levelRows; j++) {
                if (this.bubblePlay[i][j] != null) {
                    this.bubblePlay[i][j].saveState(map, savedSprites);
                    map.putInt(String.format("play-%d-%d", new Object[]{Integer.valueOf(i), Integer.valueOf(j)}), this.bubblePlay[i][j].getSavedId());
                } else {
                    map.putInt(String.format("play-%d-%d", new Object[]{Integer.valueOf(i), Integer.valueOf(j)}), -1);
                }
            }
        }
        this.launchBubble.saveState(map, savedSprites);
        map.putInt("launchBubbleId", this.launchBubble.getSavedId());
        map.putDouble("launchBubblePosition", this.launchBubblePosition);
        this.penguin.saveState(map, savedSprites);
        this.compressor.saveState(map);
        map.putInt("penguinId", this.penguin.getSavedId());
        this.nextBubble.saveState(map, savedSprites);
        map.putInt("nextBubbleId", this.nextBubble.getSavedId());
        map.putInt("currentColor", this.currentColor);
        map.putInt("nextColor", this.nextColor);
        if (this.movingBubble != null) {
            this.movingBubble.saveState(map, savedSprites);
            map.putInt("movingBubbleId", this.movingBubble.getSavedId());
        } else {
            map.putInt("movingBubbleId", -1);
        }
        this.bubbleManager.saveState(map);
        map.putInt("fixedBubbles", this.fixedBubbles);
        map.putDouble("moveDown", this.moveDown);
        map.putInt("nbBubbles", this.nbBubbles);
        map.putInt("blinkDelay", this.blinkDelay);
        this.hurrySprite.saveState(map, savedSprites);
        map.putInt("hurryId", this.hurrySprite.getSavedId());
        map.putInt("hurryTime", this.hurryTime);
        map.putBoolean("readyToFire", this.readyToFire);
        map.putBoolean("endOfGame", this.endOfGame);
        map.putBoolean("frozenify", this.frozenify);
        map.putInt("frozenifyX", this.frozenifyX);
        map.putInt("frozenifyY", this.frozenifyY);
        map.putInt("numSavedSprites", savedSprites.size());
        for (i = 0; i < savedSprites.size(); i++) {
            ((Sprite) savedSprites.elementAt(i)).clearSavedId();
        }
    }

    private Sprite restoreSprite(Bundle map, Vector imageList, int i) {
        int left = map.getInt(String.format("%d-left", new Object[]{Integer.valueOf(i)}));
        int right = map.getInt(String.format("%d-right", new Object[]{Integer.valueOf(i)}));
        int top = map.getInt(String.format("%d-top", new Object[]{Integer.valueOf(i)}));
        int bottom = map.getInt(String.format("%d-bottom", new Object[]{Integer.valueOf(i)}));
        int type = map.getInt(String.format("%d-type", new Object[]{Integer.valueOf(i)}));
        if (type == Sprite.TYPE_BUBBLE) {
            int color = map.getInt(String.format("%d-color", new Object[]{Integer.valueOf(i)}));
            return new ArcadeBubbleSprite(new Rect(left, top, right, bottom), color, map.getDouble(String.format("%d-moveX", new Object[]{Integer.valueOf(i)})), map.getDouble(String.format("%d-moveY", new Object[]{Integer.valueOf(i)})), map.getDouble(String.format("%d-realX", new Object[]{Integer.valueOf(i)})), map.getDouble(String.format("%d-realY", new Object[]{Integer.valueOf(i)})), map.getBoolean(String.format("%d-fixed", new Object[]{Integer.valueOf(i)})), map.getBoolean(String.format("%d-blink", new Object[]{Integer.valueOf(i)})), map.getBoolean(String.format("%d-released", new Object[]{Integer.valueOf(i)})), map.getBoolean(String.format("%d-checkJump", new Object[]{Integer.valueOf(i)})), map.getBoolean(String.format("%d-checkFall", new Object[]{Integer.valueOf(i)})), map.getInt(String.format("%d-fixedAnim", new Object[]{Integer.valueOf(i)})), map.getBoolean(String.format("%d-frozen", new Object[]{Integer.valueOf(i)})) ? this.frozenBubbles[color] : this.bubbles[color], this.bubblesBlind[color], this.frozenBubbles[color], this.targetedBubbles, this.bubbleBlink, this.bubbleManager, this.soundManager, this);
        } else if (type == Sprite.TYPE_IMAGE) {
            return new ImageSprite(new Rect(left, top, right, bottom), (BmpWrap) imageList.elementAt(map.getInt(String.format("%d-imageId", new Object[]{Integer.valueOf(i)}))));
        } else if (type == Sprite.TYPE_LAUNCH_BUBBLE) {
            return new LaunchBubbleSprite(map.getInt(String.format("%d-currentColor", new Object[]{Integer.valueOf(i)})), map.getInt(String.format("%d-currentDirection", new Object[]{Integer.valueOf(i)})), this.launcher, this.bubbles, this.bubblesBlind);
        } else if (type == Sprite.TYPE_PENGUIN) {
            return new PenguinSprite(this.penguins, this.random, map.getInt(String.format("%d-currentPenguin", new Object[]{Integer.valueOf(i)})), map.getInt(String.format("%d-count", new Object[]{Integer.valueOf(i)})), map.getInt(String.format("%d-finalState", new Object[]{Integer.valueOf(i)})), map.getInt(String.format("%d-nextPosition", new Object[]{Integer.valueOf(i)})));
        } else {
            Log.e("frozen-bubble", "Unrecognized sprite type: " + type);
            return null;
        }
    }

    public void restoreState(Bundle map, Vector imageList) {
        int i;
        Bundle bundle;
        Vector savedSprites = new Vector();
        int numSavedSprites = map.getInt("numSavedSprites");
        for (i = 0; i < numSavedSprites; i++) {
            savedSprites.addElement(restoreSprite(map, imageList, i));
        }
        restoreSprites(map, savedSprites);
        this.jumping = new Vector();
        int numJumpingSprites = map.getInt("numJumpingSprites");
        for (i = 0; i < numJumpingSprites; i++) {
            bundle = map;
            this.jumping.addElement(savedSprites.elementAt(bundle.getInt(String.format("jumping-%d", new Object[]{Integer.valueOf(i)}))));
        }
        this.falling = new Vector();
        int numFallingSprites = map.getInt("numFallingSprites");
        for (i = 0; i < numFallingSprites; i++) {
            bundle = map;
            this.falling.addElement(savedSprites.elementAt(bundle.getInt(String.format("falling-%d", new Object[]{Integer.valueOf(i)}))));
        }
        this.bubblePlay = (ArcadeBubbleSprite[][]) Array.newInstance(ArcadeBubbleSprite.class, new int[]{8, this.levelRows + 1});
        for (i = 0; i < 8; i++) {
            for (int j = 0; j < this.levelRows; j++) {
                bundle = map;
                int spriteIdx = bundle.getInt(String.format("play-%d-%d", new Object[]{Integer.valueOf(i), Integer.valueOf(j)}));
                if (spriteIdx != -1) {
                    this.bubblePlay[i][j] = (ArcadeBubbleSprite) savedSprites.elementAt(spriteIdx);
                } else {
                    this.bubblePlay[i][j] = null;
                }
            }
        }
        this.launchBubble = (LaunchBubbleSprite) savedSprites.elementAt(map.getInt("launchBubbleId"));
        this.launchBubblePosition = map.getDouble("launchBubblePosition");
        this.penguin = (PenguinSprite) savedSprites.elementAt(map.getInt("penguinId"));
        this.compressor.restoreState(map);
        this.nextBubble = (ImageSprite) savedSprites.elementAt(map.getInt("nextBubbleId"));
        this.currentColor = map.getInt("currentColor");
        this.nextColor = map.getInt("nextColor");
        int movingBubbleId = map.getInt("movingBubbleId");
        if (movingBubbleId == -1) {
            this.movingBubble = null;
        } else {
            this.movingBubble = (ArcadeBubbleSprite) savedSprites.elementAt(movingBubbleId);
        }
        this.bubbleManager.restoreState(map);
        this.fixedBubbles = map.getInt("fixedBubbles");
        this.moveDown = map.getDouble("moveDown");
        this.nbBubbles = map.getInt("nbBubbles");
        this.blinkDelay = map.getInt("blinkDelay");
        this.hurrySprite = (ImageSprite) savedSprites.elementAt(map.getInt("hurryId"));
        this.hurryTime = map.getInt("hurryTime");
        this.readyToFire = map.getBoolean("readyToFire");
        this.endOfGame = map.getBoolean("endOfGame");
        this.frozenify = map.getBoolean("frozenify");
        this.frozenifyX = map.getInt("frozenifyX");
        this.frozenifyY = map.getInt("frozenifyY");
    }

    private void initFrozenify() {
        ImageSprite freezeLaunchBubble = new ImageSprite(new Rect(301, 389, 34, 42), this.frozenBubbles[this.currentColor]);
        ImageSprite freezeNextBubble = new ImageSprite(new Rect(301, 439, 34, 42), this.frozenBubbles[this.nextColor]);
        addSprite(freezeLaunchBubble);
        addSprite(freezeNextBubble);
        this.frozenifyX = 7;
        this.frozenifyY = this.levelRows;
        this.frozenify = true;
    }

    private void frozenify() {
        this.frozenifyX--;
        if (this.frozenifyX < 0) {
            this.frozenifyX = 7;
            this.frozenifyY--;
            if (this.frozenifyY < 0) {
                this.frozenify = false;
                addSprite(new ImageSprite(new Rect(152, 190, 337, 116), this.gameLost));
                this.soundManager.playSound(8);
                return;
            }
        }
        while (this.bubblePlay[this.frozenifyX][this.frozenifyY] == null && this.frozenifyY >= 0) {
            this.frozenifyX--;
            if (this.frozenifyX < 0) {
                this.frozenifyX = 7;
                this.frozenifyY--;
                if (this.frozenifyY < 0) {
                    this.frozenify = false;
                    addSprite(new ImageSprite(new Rect(152, 190, 337, 116), this.gameLost));
                    this.soundManager.playSound(8);
                    return;
                }
            }
        }
        spriteToBack(this.bubblePlay[this.frozenifyX][this.frozenifyY]);
        this.bubblePlay[this.frozenifyX][this.frozenifyY].frozenify();
        spriteToBack(this.launchBubble);
    }

    public ArcadeBubbleSprite[][] getGrid() {
        return this.bubblePlay;
    }

    public void addFallingBubble(ArcadeBubbleSprite sprite) {
        spriteToFront(sprite);
        this.falling.addElement(sprite);
    }

    public void deleteFallingBubble(ArcadeBubbleSprite sprite) {
        removeSprite(sprite);
        this.falling.removeElement(sprite);
    }

    public void addJumpingBubble(ArcadeBubbleSprite sprite) {
        spriteToFront(sprite);
        this.jumping.addElement(sprite);
    }

    public void deleteJumpingBubble(ArcadeBubbleSprite sprite) {
        removeSprite(sprite);
        this.jumping.removeElement(sprite);
    }

    public Random getRandom() {
        return this.random;
    }

    public double getMoveDown() {
        return this.moveDown;
    }

    private void moveDown() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < this.levelRows; j++) {
                if (this.bubblePlay[i][j] != null) {
                    this.bubblePlay[i][j].moveDown(this.moveDownDelta);
                    if (this.bubblePlay[i][j].getSpritePosition().y >= MIN_LINE_Y) {
                        gameLost();
                    }
                }
            }
        }
        this.moveDown += (double) this.moveDownDelta;
        this.compressor.moveDown(this.moveDownDelta);
    }

    private void gameLost() {
        this.penguin.updateState(5);
        this.endOfGame = true;
        initFrozenify();
        ScoreManager.getInstance().clearTempScore();
        this.soundManager.playSound(1);
    }

    private void gameWin() {
        this.penguin.updateState(4);
        addSprite(new ImageSprite(new Rect(152, 190, 337, 116), this.gameWon));
        this.levelCompleted = true;
        this.endOfGame = true;
        this.soundManager.playSound(0);
        ScoreManager.getInstance().addToTotalScore();
        ScoreManager.getInstance().clearTempScore();
    }

    private void blinkLine(int number) {
        int column = (number + 1) >> 1;
        for (int i = number % 2; i < this.levelRows + 1; i++) {
            if (this.bubblePlay[column][i] != null) {
                this.bubblePlay[column][i].blink();
            }
        }
    }

    public boolean play(boolean key_left, boolean key_right, boolean key_fire, double trackball_dx, double[] touch_dx) {
        int i;
        int[] move = new int[2];
        if (key_left && !key_right) {
            move[0] = 37;
        } else if (!key_right || key_left) {
            move[0] = 0;
        } else {
            move[0] = 39;
        }
        if (key_fire) {
            move[1] = 38;
        } else {
            move[1] = 0;
        }
        if (move[1] == 0) {
            this.readyToFire = true;
        }
        if (BubbleArcadeActivity.getDontRushMe()) {
            this.hurryTime = 1;
        }
        if (key_fire && touch_dx != null) {
            double x_r = -(touch_dx[0] - this.launchBubble.getXCenter());
            double y_r = touch_dx[1] - this.launchBubble.getYCenter();
            this.launchBubblePosition = (Math.acos(x_r / Math.sqrt((x_r * x_r) + (y_r * y_r))) * 40.0d) / 3.141592653589793d;
            if (this.launchBubblePosition < 1.0d) {
                this.launchBubblePosition = 1.0d;
            }
            if (this.launchBubblePosition > 39.0d) {
                this.launchBubblePosition = 39.0d;
            }
            this.launchBubble.changeDirection((int) this.launchBubblePosition);
        }
        if (!this.endOfGame) {
            if (move[1] != 38 && this.hurryTime <= 480) {
                double dx = 0.0d;
                if (move[0] == 37) {
                    dx = 0.0d - 1.0d;
                }
                if (move[0] == 39) {
                    dx += 1.0d;
                }
                dx += trackball_dx;
                this.launchBubblePosition += dx;
                if (this.launchBubblePosition < 1.0d) {
                    this.launchBubblePosition = 1.0d;
                }
                if (this.launchBubblePosition > 39.0d) {
                    this.launchBubblePosition = 39.0d;
                }
                this.launchBubble.changeDirection((int) this.launchBubblePosition);
                if (dx < 0.0d) {
                    this.penguin.updateState(0);
                } else if (dx > 0.0d) {
                    this.penguin.updateState(1);
                } else {
                    this.penguin.updateState(3);
                }
            } else if (this.movingBubble == null && this.readyToFire) {
                this.nbBubbles++;
                this.movingBubble = new ArcadeBubbleSprite(new Rect(302, 390, 32, 32), (int) this.launchBubblePosition, this.currentColor, this.bubbles[this.currentColor], this.bubblesBlind[this.currentColor], this.frozenBubbles[this.currentColor], this.targetedBubbles, this.bubbleBlink, this.bubbleManager, this.soundManager, this);
                addSprite(this.movingBubble);
                this.currentColor = this.nextColor;
                this.nextColor = this.bubbleManager.nextBubbleIndex(this.random);
                if (BubbleArcadeActivity.getMode() == 0) {
                    this.nextBubble.changeImage(this.bubbles[this.nextColor]);
                } else {
                    this.nextBubble.changeImage(this.bubblesBlind[this.nextColor]);
                }
                this.launchBubble.changeColor(this.currentColor);
                this.penguin.updateState(2);
                this.soundManager.playSound(2);
                this.readyToFire = false;
                this.hurryTime = 0;
                removeSprite(this.hurrySprite);
            } else {
                this.penguin.updateState(3);
            }
            moveDown();
        } else if (move[1] == 38 && this.readyToFire) {
            if (this.levelCompleted) {
                this.levelManager.goToNextLevel();
            }
            return true;
        } else {
            this.penguin.updateState(3);
            if (this.frozenify) {
                frozenify();
            }
        }
        if (this.movingBubble != null) {
            this.movingBubble.move(this.levelRows);
            if (this.movingBubble.fixed()) {
                if (this.movingBubble.getSpritePosition().y >= MIN_LINE_Y && !this.movingBubble.released()) {
                    gameLost();
                } else if (this.bubbleManager.countBubbles() == 0) {
                    gameWin();
                } else {
                    this.fixedBubbles++;
                    this.blinkDelay = 0;
                    if (this.fixedBubbles == 8) {
                        this.fixedBubbles = 0;
                    }
                }
                this.movingBubble = null;
            }
            if (this.movingBubble != null) {
                this.movingBubble.move(this.levelRows);
                if (this.movingBubble.fixed()) {
                    if (this.movingBubble.getSpritePosition().y >= MIN_LINE_Y && !this.movingBubble.released()) {
                        gameLost();
                    } else if (this.bubbleManager.countBubbles() == 0) {
                        gameWin();
                    } else {
                        this.fixedBubbles++;
                        this.blinkDelay = 0;
                        if (this.fixedBubbles == 8) {
                            this.fixedBubbles = 0;
                        }
                    }
                    this.movingBubble = null;
                }
            }
        }
        if (this.movingBubble == null && !this.endOfGame) {
            this.hurryTime++;
            if (this.hurryTime == 2) {
                removeSprite(this.hurrySprite);
            }
            if (this.hurryTime >= 240) {
                if (this.hurryTime % 40 == 10) {
                    addSprite(this.hurrySprite);
                    this.soundManager.playSound(6);
                } else if (this.hurryTime % 40 == 35) {
                    removeSprite(this.hurrySprite);
                }
            }
        }
        if (this.fixedBubbles == 6) {
            if (this.blinkDelay < 15) {
                blinkLine(this.blinkDelay);
            }
            this.blinkDelay++;
            if (this.blinkDelay == 40) {
                this.blinkDelay = 0;
            }
        } else if (this.fixedBubbles == 7) {
            if (this.blinkDelay < 15) {
                blinkLine(this.blinkDelay);
            }
            this.blinkDelay++;
            if (this.blinkDelay == 25) {
                this.blinkDelay = 0;
            }
        }
        for (i = 0; i < this.falling.size(); i++) {
            ((ArcadeBubbleSprite) this.falling.elementAt(i)).fall();
        }
        for (i = 0; i < this.jumping.size(); i++) {
            ((ArcadeBubbleSprite) this.jumping.elementAt(i)).jump();
        }
        return false;
    }

    public void paint(Canvas c, double scale, int dx, int dy) {
        this.compressor.paint(c, scale, dx, dy);
        this.nextBubble.changeImage(this.bubbles[this.nextColor]);
        super.paint(c, scale, dx, dy);
    }

    public boolean getIsEnd() {
        if (this.endOfGame && this.levelCompleted) {
            return true;
        }
        return false;
    }
}