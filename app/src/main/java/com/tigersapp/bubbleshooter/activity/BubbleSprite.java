package com.tigersapp.bubbleshooter.activity;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;

import com.tigersapp.bubbleshooter.manager.BubbleManager;

import java.util.Vector;

/**
 * Created by Ripon on 1/5/17.
 */

public class BubbleSprite extends Sprite {
    private static double FALL_SPEED = 1.0d;
    private static double MAX_BUBBLE_SPEED = 8.0d;
    private static double MINIMUM_DISTANCE = 841.0d;
    private boolean blink;
    private BmpWrap bubbleBlindFace;
    private BmpWrap bubbleBlink;
    private BmpWrap bubbleFace;
    private BmpWrap[] bubbleFixed;
    private BubbleManager bubbleManager;
    private boolean checkFall;
    private boolean checkJump;
    private int color;
    private boolean fixed;
    private int fixedAnim;
    private BubbleShooterGame frozen;
    private BmpWrap frozenFace;
    private double moveX;
    private double moveY;
    private double realX;
    private double realY;
    private boolean released;
    private SoundManager soundManager;

    public void saveState(Bundle map, Vector savedSprites) {
        boolean z = true;
        if (getSavedId() == -1) {
            super.saveState(map, savedSprites);
            map.putInt(String.format("%d-color", new Object[]{Integer.valueOf(getSavedId())}), this.color);
            map.putDouble(String.format("%d-moveX", new Object[]{Integer.valueOf(getSavedId())}), this.moveX);
            map.putDouble(String.format("%d-moveY", new Object[]{Integer.valueOf(getSavedId())}), this.moveY);
            map.putDouble(String.format("%d-realX", new Object[]{Integer.valueOf(getSavedId())}), this.realX);
            map.putDouble(String.format("%d-realY", new Object[]{Integer.valueOf(getSavedId())}), this.realY);
            map.putBoolean(String.format("%d-fixed", new Object[]{Integer.valueOf(getSavedId())}), this.fixed);
            map.putBoolean(String.format("%d-blink", new Object[]{Integer.valueOf(getSavedId())}), this.blink);
            map.putBoolean(String.format("%d-released", new Object[]{Integer.valueOf(getSavedId())}), this.released);
            map.putBoolean(String.format("%d-checkJump", new Object[]{Integer.valueOf(getSavedId())}), this.checkJump);
            map.putBoolean(String.format("%d-checkFall", new Object[]{Integer.valueOf(getSavedId())}), this.checkFall);
            map.putInt(String.format("%d-fixedAnim", new Object[]{Integer.valueOf(getSavedId())}), this.fixedAnim);
            String format = String.format("%d-frozen", new Object[]{Integer.valueOf(getSavedId())});
            if (this.bubbleFace != this.frozenFace) {
                z = false;
            }
            map.putBoolean(format, z);
        }
    }

    public int getTypeId() {
        return Sprite.TYPE_BUBBLE;
    }

    public BubbleSprite(Rect area, int color, double moveX, double moveY, double realX, double realY, boolean fixed, boolean blink, boolean released, boolean checkJump, boolean checkFall, int fixedAnim, BmpWrap bubbleFace, BmpWrap bubbleBlindFace, BmpWrap frozenFace, BmpWrap[] bubbleFixed, BmpWrap bubbleBlink, BubbleManager bubbleManager, SoundManager soundManager, BubbleShooterGame frozen) {
        super(area);
        this.color = color;
        this.moveX = moveX;
        this.moveY = moveY;
        this.realX = realX;
        this.realY = realY;
        this.fixed = fixed;
        this.blink = blink;
        this.released = released;
        this.checkJump = checkJump;
        this.checkFall = checkFall;
        this.fixedAnim = fixedAnim;
        this.bubbleFace = bubbleFace;
        this.bubbleBlindFace = bubbleBlindFace;
        this.frozenFace = frozenFace;
        this.bubbleFixed = bubbleFixed;
        this.bubbleBlink = bubbleBlink;
        this.bubbleManager = bubbleManager;
        this.soundManager = soundManager;
        this.frozen = frozen;
    }

    public BubbleSprite(Rect area, int direction, int color, BmpWrap bubbleFace, BmpWrap bubbleBlindFace, BmpWrap frozenFace, BmpWrap[] bubbleFixed, BmpWrap bubbleBlink, BubbleManager bubbleManager, SoundManager soundManager, BubbleShooterGame frozen) {
        super(area);
        this.color = color;
        this.bubbleFace = bubbleFace;
        this.bubbleBlindFace = bubbleBlindFace;
        this.frozenFace = frozenFace;
        this.bubbleFixed = bubbleFixed;
        this.bubbleBlink = bubbleBlink;
        this.bubbleManager = bubbleManager;
        this.soundManager = soundManager;
        this.frozen = frozen;
        this.moveX = MAX_BUBBLE_SPEED * (-Math.cos((((double) direction) * 3.141592653589793d) / 40.0d));
        this.moveY = MAX_BUBBLE_SPEED * (-Math.sin((((double) direction) * 3.141592653589793d) / 40.0d));
        this.realX = (double) area.left;
        this.realY = (double) area.top;
        this.fixed = false;
        this.fixedAnim = -1;
    }

    public BubbleSprite(Rect area, int color, BmpWrap bubbleFace, BmpWrap bubbleBlindFace, BmpWrap frozenFace, BmpWrap bubbleBlink, BubbleManager bubbleManager, SoundManager soundManager, BubbleShooterGame frozen) {
        super(area);
        this.color = color;
        this.bubbleFace = bubbleFace;
        this.bubbleBlindFace = bubbleBlindFace;
        this.frozenFace = frozenFace;
        this.bubbleBlink = bubbleBlink;
        this.bubbleManager = bubbleManager;
        this.soundManager = soundManager;
        this.frozen = frozen;
        this.realX = (double) area.left;
        this.realY = (double) area.top;
        this.fixed = true;
        this.fixedAnim = -1;
        bubbleManager.addBubble(bubbleFace);
    }

    Point currentPosition() {
        int posY = (int) Math.floor(((this.realY - 28.0d) - this.frozen.getMoveDown()) / 28.0d);
        int posX = (int) Math.floor(((this.realX - 174.0d) / 32.0d) + (0.5d * ((double) (posY % 2))));
        if (posX > 7) {
            posX = 7;
        }
        if (posX < 0) {
            posX = 0;
        }
        if (posY < 0) {
            posY = 0;
        }
        return new Point(posX, posY);
    }

    public void removeFromManager() {
        this.bubbleManager.removeBubble(this.bubbleFace);
    }

    public boolean fixed() {
        return this.fixed;
    }

    public boolean checked() {
        return this.checkFall;
    }

    public boolean released() {
        return this.released;
    }

    public void moveDown() {
        if (this.fixed) {
            this.realY += 28.0d;
        }
        super.absoluteMove(new Point((int) this.realX, (int) this.realY));
    }

    public void moveDown(float moveDelta) {
        if (this.fixed) {
            this.realY += (double) moveDelta;
        }
        super.absoluteMove(new Point((int) this.realX, (int) this.realY));
    }

    public void move() {
        this.realX += this.moveX;
        if (this.realX >= 414.0d) {
            this.moveX = -this.moveX;
            this.realX += 414.0d - this.realX;
            this.soundManager.playSound(4);
        } else if (this.realX <= 190.0d) {
            this.moveX = -this.moveX;
            this.realX += 190.0d - this.realX;
            this.soundManager.playSound(4);
        }
        this.realY += this.moveY;
        Point currentPosition = currentPosition();
        Vector neighbors = getNeighbors(currentPosition);
        if (checkCollision(neighbors) || this.realY < 44.0d + this.frozen.getMoveDown()) {
            this.realX = (190.0d + ((double) (currentPosition.x * 32))) - ((double) ((currentPosition.y % 2) * 16));
            this.realY = (44.0d + ((double) (currentPosition.y * 28))) + this.frozen.getMoveDown();
            this.fixed = true;
            Vector checkJump = new Vector();
            checkJump(checkJump, neighbors);
            BubbleSprite[][] grid = this.frozen.getGrid();
            if (checkJump.size() >= 3) {
                int i;
                this.released = true;
                for (i = 0; i < checkJump.size(); i++) {
                    BubbleSprite current = (BubbleSprite) checkJump.elementAt(i);
                    Point currentPoint = current.currentPosition();
                    this.frozen.addJumpingBubble(current);
                    if (i > 0) {
                        current.removeFromManager();
                    }
                    grid[currentPoint.x][currentPoint.y] = null;
                }
                for (i = 0; i < 8; i++) {
                    if (grid[i][0] != null) {
                        grid[i][0].checkFall();
                    }
                }
                i = 0;
                while (i < 8) {
                    int j = 0;
                    while (j < 12) {
                        if (!(grid[i][j] == null || grid[i][j].checked())) {
                            this.frozen.addFallingBubble(grid[i][j]);
                            grid[i][j].removeFromManager();
                            grid[i][j] = null;
                        }
                        j++;
                    }
                    i++;
                }
                this.soundManager.playSound(3);
            } else {
                this.bubbleManager.addBubble(this.bubbleFace);
                grid[currentPosition.x][currentPosition.y] = this;
                this.moveX = 0.0d;
                this.moveY = 0.0d;
                this.fixedAnim = 0;
                this.soundManager.playSound(5);
            }
        }
        super.absoluteMove(new Point((int) this.realX, (int) this.realY));
    }

    Vector getNeighbors(Point p) {
        BubbleSprite[][] grid = this.frozen.getGrid();
        Vector list = new Vector();
        if (p.y % 2 == 0) {
            if (p.x > 0) {
                list.addElement(grid[p.x - 1][p.y]);
            }
            if (p.x < 7) {
                list.addElement(grid[p.x + 1][p.y]);
                if (p.y > 0) {
                    list.addElement(grid[p.x][p.y - 1]);
                    list.addElement(grid[p.x + 1][p.y - 1]);
                }
                if (p.y < 12) {
                    list.addElement(grid[p.x][p.y + 1]);
                    list.addElement(grid[p.x + 1][p.y + 1]);
                }
            } else {
                if (p.y > 0) {
                    list.addElement(grid[p.x][p.y - 1]);
                }
                if (p.y < 12) {
                    list.addElement(grid[p.x][p.y + 1]);
                }
            }
        } else {
            if (p.x < 7) {
                list.addElement(grid[p.x + 1][p.y]);
            }
            if (p.x > 0) {
                list.addElement(grid[p.x - 1][p.y]);
                if (p.y > 0) {
                    list.addElement(grid[p.x][p.y - 1]);
                    list.addElement(grid[p.x - 1][p.y - 1]);
                }
                if (p.y < 12) {
                    list.addElement(grid[p.x][p.y + 1]);
                    list.addElement(grid[p.x - 1][p.y + 1]);
                }
            } else {
                if (p.y > 0) {
                    list.addElement(grid[p.x][p.y - 1]);
                }
                if (p.y < 12) {
                    list.addElement(grid[p.x][p.y + 1]);
                }
            }
        }
        return list;
    }

    void checkJump(Vector jump, BmpWrap compare) {
        if (!this.checkJump) {
            this.checkJump = true;
            if (this.bubbleFace == compare) {
                checkJump(jump, getNeighbors(currentPosition()));
            }
        }
    }

    void checkJump(Vector jump, Vector neighbors) {
        jump.addElement(this);
        for (int i = 0; i < neighbors.size(); i++) {
            BubbleSprite current = (BubbleSprite) neighbors.elementAt(i);
            if (current != null) {
                current.checkJump(jump, this.bubbleFace);
            }
        }
    }

    public void checkFall() {
        if (!this.checkFall) {
            this.checkFall = true;
            Vector v = getNeighbors(currentPosition());
            for (int i = 0; i < v.size(); i++) {
                BubbleSprite current = (BubbleSprite) v.elementAt(i);
                if (current != null) {
                    current.checkFall();
                }
            }
        }
    }

    boolean checkCollision(Vector neighbors) {
        for (int i = 0; i < neighbors.size(); i++) {
            BubbleSprite current = (BubbleSprite) neighbors.elementAt(i);
            if (current != null && checkCollision(current)) {
                return true;
            }
        }
        return false;
    }

    boolean checkCollision(BubbleSprite sprite) {
        return ((((double) sprite.getSpriteArea().left) - this.realX) * (((double) sprite.getSpriteArea().left) - this.realX)) + ((((double) sprite.getSpriteArea().top) - this.realY) * (((double) sprite.getSpriteArea().top) - this.realY)) < MINIMUM_DISTANCE;
    }

    public void jump() {
        if (this.fixed) {
            this.moveX = -6.0d + (this.frozen.getRandom().nextDouble() * 12.0d);
            this.moveY = -5.0d - (this.frozen.getRandom().nextDouble() * 10.0d);
            this.fixed = false;
        }
        this.moveY += FALL_SPEED;
        this.realY += this.moveY;
        this.realX += this.moveX;
        super.absoluteMove(new Point((int) this.realX, (int) this.realY));
        if (this.realY >= 680.0d) {
            this.frozen.deleteJumpingBubble(this);
        }
    }

    public void fall() {
        if (this.fixed) {
            this.moveY = this.frozen.getRandom().nextDouble() * 5.0d;
        }
        this.fixed = false;
        this.moveY += FALL_SPEED;
        this.realY += this.moveY;
        super.absoluteMove(new Point((int) this.realX, (int) this.realY));
        if (this.realY >= 680.0d) {
            this.frozen.deleteFallingBubble(this);
        }
    }

    public void blink() {
        this.blink = true;
    }

    public void frozenify() {
        changeSpriteArea(new Rect(getSpritePosition().x - 1, getSpritePosition().y - 1, 34, 42));
        this.bubbleFace = this.frozenFace;
    }

    public final void paint(Canvas c, double scale, int dx, int dy) {
        this.checkJump = false;
        this.checkFall = false;
        Point p = getSpritePosition();
        if (this.blink && this.bubbleFace != this.frozenFace) {
            this.blink = false;
            Sprite.drawImage(this.bubbleBlink, p.x, p.y, c, scale, dx, dy);
        } else if (BubbleShooterActivity.getMode() == 0 || this.bubbleFace == this.frozenFace) {
            Sprite.drawImage(this.bubbleFace, p.x, p.y, c, scale, dx, dy);
        } else {
            Sprite.drawImage(this.bubbleBlindFace, p.x, p.y, c, scale, dx, dy);
        }
        if (this.fixedAnim != -1) {
            Sprite.drawImage(this.bubbleFixed[this.fixedAnim], p.x, p.y, c, scale, dx, dy);
            this.fixedAnim++;
            if (this.fixedAnim == 6) {
                this.fixedAnim = -1;
            }
        }
    }
}