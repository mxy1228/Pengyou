package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

/**
 * 卸载
 * 
 * @author wangkang
 * 
 */
public class InstallGameViewCache {

	private View mView;
	private ImageView imgGameIcon;
	private TextView txtGameName;
	private TextView txtGamesize;
	private Button btnDownloadGame;

	public InstallGameViewCache(View view) {
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

	public Button getBtnDownloadGame() {
		if (btnDownloadGame == null) {
			btnDownloadGame = (Button) mView.findViewById(R.id.btn_install);
		}
		return btnDownloadGame;
	}

	public TextView getTxtGamesize() {
		if (txtGamesize == null) {
			txtGamesize = (TextView) mView.findViewById(R.id.txt_gamesize);
		}
		return txtGamesize;
	}

}
