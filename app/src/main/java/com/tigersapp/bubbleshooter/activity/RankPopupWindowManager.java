package com.tigersapp.bubbleshooter.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tigersapp.bubbleshooter.R;
import com.tigersapp.bubbleshooter.arcade.ScoreManager;
import com.tigersapp.bubbleshooter.utils.ScreenUtil;

/**
 * Created by Ripon on 1/5/17.
 */

public class RankPopupWindowManager {
    static AppCompatActivity act = null;
    private TextView mButtonHonor;
    private TextView mButtonLevelRank;
    private TextView mButtonScoreRank;
    private PopupWindow mPopupWindow;
    private View.OnClickListener onPopupItemClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            RankPopupWindowManager.this.mPopupWindow.dismiss();
            if (v == RankPopupWindowManager.this.mButtonScoreRank) {
                ScoreManager.getInstance().submitArcadeThenOpenLeadboardActivity(RankPopupWindowManager.act);
            } else if (v == RankPopupWindowManager.this.mButtonLevelRank) {
                ScoreManager.getInstance().submitPuzzleThenOpenLeadboardActivity(RankPopupWindowManager.act);
            } else if (v == RankPopupWindowManager.this.mButtonHonor) {
                ScoreManager.getInstance().checkHonor(RankPopupWindowManager.act);
                ScoreManager.getInstance().openHonor(RankPopupWindowManager.act);
            }
        }
    };

    public RankPopupWindowManager(AppCompatActivity act) {
        act = act;
    }

    public void setupPopup(Context mContext) {
        View view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.rank_popup_menu, null);
        this.mPopupWindow = new PopupWindow(view);
        this.mPopupWindow.setAnimationStyle(R.style.RankPopupAnimation);
        this.mPopupWindow.setOutsideTouchable(true);
        this.mPopupWindow.setWidth(ScreenUtil.dip2px(mContext, 320.0f));
        this.mPopupWindow.setHeight(ScreenUtil.dip2px(mContext, 250.0f));
        this.mButtonScoreRank = (TextView) view.findViewById(R.id.scorerank);
        this.mButtonLevelRank = (TextView) view.findViewById(R.id.levelrank);
        this.mButtonHonor = (TextView) view.findViewById(R.id.honor);
        this.mButtonScoreRank.setOnClickListener(this.onPopupItemClickListener);
        this.mButtonLevelRank.setOnClickListener(this.onPopupItemClickListener);
        this.mButtonHonor.setOnClickListener(this.onPopupItemClickListener);
    }

    public void showPopup(View v) {
        if (this.mPopupWindow.isShowing()) {
            this.mPopupWindow.dismiss();
        } else {
            this.mPopupWindow.showAtLocation(v, 0, 0, 0);
        }
    }

    public boolean isRankWindowShowing() {
        if (this.mPopupWindow != null) {
            return this.mPopupWindow.isShowing();
        }
        return false;
    }

    public void dissmissRankWindow() {
        if (this.mPopupWindow != null) {
            this.mPopupWindow.dismiss();
        }
    }
}
