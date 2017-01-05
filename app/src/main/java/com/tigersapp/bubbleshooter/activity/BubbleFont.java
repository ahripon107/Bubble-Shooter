package com.tigersapp.bubbleshooter.activity;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Ripon on 1/5/17.
 */

public class BubbleFont {

    public int SEPARATOR_WIDTH;
    public int SPACE_CHAR_WIDTH;
    private char[] characters = new char[]{'!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '|', '{', '}', '[', ']', ' ', '\\', ' ', ' '};
    private Rect clipRect;
    private BmpWrap fontMap;
    private char[] numCharacters;
    private int[] numPosition;
    private int[] position;

    public BubbleFont(BmpWrap fontMap) {
        int[] iArr = new int[68];
        iArr[1] = 9;
        iArr[2] = 16;
        iArr[3] = 31;
        iArr[4] = 39;
        iArr[5] = 54;
        iArr[6] = 69;
        iArr[7] = 73;
        iArr[8] = 80;
        iArr[9] = 88;
        iArr[10] = 96;
        iArr[11] = 116;
        iArr[12] = 121;
        iArr[13] = 131;
        iArr[14] = 137;
        iArr[15] = 154;
        iArr[16] = 165;
        iArr[17] = 175;
        iArr[18] = 187;
        iArr[19] = 198;
        iArr[20] = 210;
        iArr[21] = 223;
        iArr[22] = 234;
        iArr[23] = 246;
        iArr[24] = 259;
        iArr[25] = 271;
        iArr[26] = 276;
        iArr[27] = 282;
        iArr[28] = 293;
        iArr[29] = 313;
        iArr[30] = 324;
        iArr[31] = 336;
        iArr[32] = 351;
        iArr[33] = 360;
        iArr[34] = 370;
        iArr[35] = 381;
        iArr[36] = 390;
        iArr[37] = WalletConstants.ERROR_CODE_SERVICE_UNAVAILABLE;
        iArr[38] = WalletConstants.ERROR_CODE_AUTHENTICATION_FAILURE;
        iArr[39] = 421;
        iArr[40] = 435;
        iArr[41] = 446;
        iArr[42] = 459;
        iArr[43] = 472;
        iArr[44] = 483;
        iArr[45] = 495;
        iArr[46] = 508;
        iArr[47] = 517;
        iArr[48] = 527;
        iArr[49] = 538;
        iArr[50] = 552;
        iArr[51] = 565;
        iArr[52] = 578;
        iArr[53] = 589;
        iArr[54] = 602;
        iArr[55] = 616;
        iArr[56] = 631;
        iArr[57] = 645;
        iArr[58] = 663;
        iArr[59] = 684;
        iArr[60] = 700;
        iArr[61] = 716;
        iArr[62] = 732;
        iArr[63] = 748;
        iArr[64] = 764;
        iArr[65] = 780;
        iArr[66] = 796;
        iArr[67] = 812;
        this.position = iArr;
        this.numCharacters = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        iArr = new int[11];
        iArr[1] = 12;
        iArr[2] = 21;
        iArr[3] = 32;
        iArr[4] = 42;
        iArr[5] = 53;
        iArr[6] = 63;
        iArr[7] = 74;
        iArr[8] = 83;
        iArr[9] = 93;
        iArr[10] = LocationRequest.PRIORITY_LOW_POWER;
        this.numPosition = iArr;
        this.SEPARATOR_WIDTH = 1;
        this.SPACE_CHAR_WIDTH = 6;
        this.fontMap = fontMap;
        this.clipRect = new Rect();
    }

    public final void print(String s, int x, int y, Canvas canvas, double scale, int dx, int dy) {
        for (int i = 0; i < s.length(); i++) {
            x += paintChar(s.charAt(i), x, y, canvas, scale, dx, dy);
        }
    }

    public final int paintChar(char c, int x, int y, Canvas canvas, double scale, int dx, int dy) {
        if (c == ' ') {
            return this.SPACE_CHAR_WIDTH + this.SEPARATOR_WIDTH;
        }
        int index = getCharIndex(c);
        if (index == -1) {
            return 0;
        }
        int imageWidth = this.numPosition[index + 1] - this.numPosition[index];
        this.clipRect.left = x;
        this.clipRect.right = x + imageWidth;
        this.clipRect.top = y;
        this.clipRect.bottom = y + 22;
        Sprite.drawImageClipped(this.fontMap, x - this.numPosition[index], y, this.clipRect, canvas, scale, dx, dy);
        return this.SEPARATOR_WIDTH + imageWidth;
    }

    private final int getCharIndex(char c) {
        for (int i = 0; i < this.numCharacters.length; i++) {
            if (this.numCharacters[i] == c) {
                return i;
            }
        }
        return -1;
    }
}
