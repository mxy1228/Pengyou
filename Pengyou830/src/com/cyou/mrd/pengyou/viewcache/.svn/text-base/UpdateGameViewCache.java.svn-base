package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

public class UpdateGameViewCache {

	private View mView;
	private ImageView imgGameIcon;
	private TextView txtGameName;
	private TextView txtPlayerCount;
	private Button btnDownloadGame;

	public UpdateGameViewCache(View view) {
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

	public TextView getTxtPlayerCount() {
		if (txtPlayerCount == null) {
			txtPlayerCount = (TextView) mView
					.findViewById(R.id.txt_playercount);
		}
		return txtPlayerCount;
	}

	public Button getBtnDownloadGame() {
		if (btnDownloadGame == null) {
			btnDownloadGame = (Button) mView.findViewById(R.id.btn_update);
		}
		return btnDownloadGame;
	}

}
