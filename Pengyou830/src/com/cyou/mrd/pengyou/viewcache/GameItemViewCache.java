package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

public class GameItemViewCache {

	private View mView;
	private ImageView imgGameIcon;
	private TextView txtGameName;
	private TextView txtPlayerCount;
	//private ImageButton btnDownloadGame;
	private Button btnDownloadGame;
	private RatingBar ratingBar;
	private TextView txtGameInfo;
	private TextView txtDownload;
	private TextView txtGameSize;
	private TextView txtSecurity;
	private TextView txtAdsdisplay;
	private TextView txtFeetype;
	private TextView txtOfficial;
	private TextView txtNoads;
	public GameItemViewCache(View view) {
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
	
	public TextView getTxtGameSize() {
		if (txtGameSize == null) {
			txtGameSize = (TextView) mView.findViewById(R.id.txt_gamesize);
		}
		return txtGameSize;
	}
	/*
	public TextView getTxtPlayerCount() {
		if (txtPlayerCount == null) {
			txtPlayerCount = (TextView) mView
					.findViewById(R.id.txt_playercount);
		}
		return txtPlayerCount;
	}
	*/

	public RatingBar getRatingBar() {
		if (ratingBar == null) {
			ratingBar = (RatingBar) mView.findViewById(R.id.rb_game);
		}
		return ratingBar;
	}
	/*
	public ImageButton getBtnDownloadGame() {
		if (btnDownloadGame == null) {
			btnDownloadGame = (ImageButton) mView
					.findViewById(R.id.btn_playgame);
		}
		return btnDownloadGame;
	}
	*/
	
	public Button getBtnDownloadGame() {
		if (btnDownloadGame == null) {
			btnDownloadGame = (Button) mView.findViewById(R.id.btn_playgame);
		}
		return btnDownloadGame;
	}

	public TextView getTxtGameInfo() {
		if(null==txtGameInfo){
			txtGameInfo=(TextView) mView.findViewById(R.id.txt_gameinfo);
		}
		return txtGameInfo;
	}
	/*
	public TextView getTxtDownload() {
		if (txtDownload == null) {
			txtDownload = (TextView) mView.findViewById(R.id.txt_download);
		}
		return txtDownload;
	}
	*/
	
	public TextView getTxtSecurity() {
		if (txtSecurity == null) {
			txtSecurity = (TextView) mView.findViewById(R.id.txt_game_security);
		}
		return txtSecurity;
	}
	public TextView getTxtAdsdisplay() {
		if (txtAdsdisplay == null) {
			txtAdsdisplay = (TextView) mView.findViewById(R.id.txt_game_adsdisplay);
		}
		return txtAdsdisplay;
	}
	public TextView getTxtFeetype() {
		if (txtFeetype == null) {
			txtFeetype = (TextView) mView.findViewById(R.id.txt_game_feetype);
		}
		return txtFeetype;
	}
	public TextView getTxtOfficial() {
		if (txtOfficial == null) {
			txtOfficial = (TextView) mView.findViewById(R.id.txt_game_official);
		}
		return txtOfficial;
	}
	public TextView getTxtNoads() {
		if (txtNoads == null) {
			txtNoads = (TextView) mView.findViewById(R.id.txt_game_noads);
		}
		return txtNoads;
	}

	
}
