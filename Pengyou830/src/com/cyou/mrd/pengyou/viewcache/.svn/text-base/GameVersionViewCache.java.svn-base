package com.cyou.mrd.pengyou.viewcache;

import com.cyou.mrd.pengyou.R;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameVersionViewCache {
    private TextView versionNameTV;
    private TextView gameVersionFromTV;
    private TextView gameVersionDataSizeTV;
    private LinearLayout gameVersionLayout;
    private TextView gameVersionInfo;
    private View mView;
    public GameVersionViewCache (View mView) {
        this.mView = mView;
    }
    public TextView getVersionNameTV() {
        if (versionNameTV == null){
            versionNameTV = (TextView) mView.findViewById(R.id.game_version_tv);
        }
        return versionNameTV;
    }

//    public TextView getGameVersionFromTV() {
//        if (gameVersionFromTV == null ){
//            gameVersionFromTV = (TextView) mView.findViewById(R.id.game_version_from);
//        }
//        return gameVersionFromTV;
//    }
//
//    public TextView getGameVersionDataSizeTV() {
//        if (gameVersionDataSizeTV == null ){
//            gameVersionDataSizeTV = (TextView) mView.findViewById(R.id.game_version_datasize);
//        }
//        return gameVersionDataSizeTV;
//    }
//    public LinearLayout getGameVersionLayout() {
//        if (gameVersionLayout == null) {
//            gameVersionLayout = (LinearLayout) mView.findViewById(R.id.game_versiondata_ll);
//        }
//        return gameVersionLayout;
//    }
    public TextView getGameVersionInfo() {
        if (gameVersionInfo == null) {
            gameVersionInfo = (TextView) mView.findViewById(R.id.game_version_info);
        }
        return gameVersionInfo;
    }
   
}
