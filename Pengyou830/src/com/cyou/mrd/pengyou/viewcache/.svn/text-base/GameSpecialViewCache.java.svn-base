package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.widget.GameGridView;

public class GameSpecialViewCache {

	private View mView;
	private TextView txtGameSpecialName;
	private TextView txtGameSpecialTime;
	private ImageView imgGameSpecialPicture;
	private GameGridView gameSpecialIconView;
	private TextView txtGameSpecialDesc;
	

	public GameSpecialViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}


	public TextView getTxtGameSpecialName() {
		if (txtGameSpecialName == null) {
			txtGameSpecialName = (TextView) mView.findViewById(R.id.game_special_name);
		}
		return txtGameSpecialName;
	}

	public TextView getTxtGameSpecialTime() {
		if (txtGameSpecialTime == null) {
			txtGameSpecialTime = (TextView) mView.findViewById(R.id.game_special_time);
		}
		return txtGameSpecialTime;
	}

	public ImageView getImgGameSpecialPicture() {
		if (imgGameSpecialPicture == null) {
			imgGameSpecialPicture = (ImageView) mView.findViewById(R.id.game_special_picture);
		}
		return imgGameSpecialPicture;
	}

	public GameGridView getGameSpecialIconView() {
		if (gameSpecialIconView == null) {
			gameSpecialIconView = (GameGridView) mView.findViewById(R.id.game_special_gridview);
		}
		return gameSpecialIconView;
	}

	public TextView getTxtGameSpecialDesc() {
		if (txtGameSpecialDesc == null) {
			txtGameSpecialDesc = (TextView) mView.findViewById(R.id.game_special_desc);
		}
		return txtGameSpecialDesc;
	}



}
