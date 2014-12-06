package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

public class RankingGameViewCache {

	private View mView;
	private ImageView imgGameIcon;
	private TextView txtGameName;
	private TextView txtPlayerCount;
	private Button btnDownloadGame;
	private RatingBar ratingBar;
	//private TextView txtRanking;
	private TextView txtRecdesc;

	public RankingGameViewCache(View view) {
		this.mView = view;
	}
	
	public View getmView() {
		return mView;
	}
	

	public ImageView getImgGameIcon() {
		if (imgGameIcon == null) {
			imgGameIcon = (ImageView) mView.findViewById(R.id.img_gameicon);
		}
		return imgGameIcon;
	}

	public TextView getTxtGameName() {
		if (txtGameName == null) {
			txtGameName = (TextView) mView.findViewById(R.id.txt_gamename);
		}
		return txtGameName;
	}
	
	/*public TextView getTxtRanking() {
		if (txtRanking == null) {
			txtRanking = (TextView) mView.findViewById(R.id.txt_ranking_number);
		}
		return txtRanking;
	}*/

	public TextView getTxtPlayerCount() {
		if (txtPlayerCount == null) {
			txtPlayerCount = (TextView) mView
					.findViewById(R.id.txt_playercount);
		}
		return txtPlayerCount;
	}

	public RatingBar getRatingBar() {
		if (ratingBar == null) {
			ratingBar = (RatingBar) mView.findViewById(R.id.rb_game);
		}
		return ratingBar;
	}

	public Button getBtnDownloadGame() {
		if (btnDownloadGame == null) {
			btnDownloadGame = (Button) mView
					.findViewById(R.id.btn_playgame);
		}
		return btnDownloadGame;
	}
	
	public TextView getTxtRecdesc() {
		if (txtRecdesc == null) {
			txtRecdesc = (TextView) mView
					.findViewById(R.id.txt_guess_recommend);
		}
		return txtRecdesc;
	}


}
