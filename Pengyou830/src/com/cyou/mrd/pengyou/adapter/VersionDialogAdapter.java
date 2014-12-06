package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.GameDetailInfo;
import com.cyou.mrd.pengyou.entity.VersionInfo;
import com.cyou.mrd.pengyou.ui.GameCircleDetailActivity;
import com.cyou.mrd.pengyou.utils.Util;
public class VersionDialogAdapter extends BaseAdapter{
    private Context mContext;
    private List<VersionInfo> mData;
    private LayoutInflater mInflater;
    private ViewHolder viewholder = null;
    private String gameName;
    private ViewHolder new_viewholder;


    private GameCircleDetailActivity activity;

    int[] drawables={R.drawable.game_official,R.drawable.game_security,R.drawable.game_noads,R.drawable.game_inside_ad,R.drawable.game_inside_charge};
    
    public VersionDialogAdapter() {
        //super();
    }


    public VersionDialogAdapter(GameCircleDetailActivity activity, Context context ,List<VersionInfo> data,String gameName) {

    	this.activity = activity;
        this.gameName = gameName;
        this.mContext = context;
        this.mData = data;
        Log.v("---------------------------", "this.mData = " + this.mData.size());
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
    }

  
    public int getCount() {
        return mData.isEmpty() ? 0:mData.size();
    }
    public Object getItem(int position) {
        return mData.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
		// if (convertView == null) {
		convertView = mInflater.inflate(R.layout.dialoglv_list_item, null);
		viewholder = new ViewHolder(convertView);
		convertView.setTag(viewholder);
		// } else {
		// viewholder = (ViewHolder) convertView.getTag();
		// }

		viewholder.getVersion_tv().setText("V"+mData.get(position).getVersion());
		viewholder.getVersion_tv().setVisibility(View.VISIBLE);
	 //   boolean flag=true;
		for (GameDetailInfo info : mData.get(position).getSrclist()) {
			
			 View row_layout = mInflater.inflate(R.layout.row_layout, null);
			new_viewholder = new ViewHolder(row_layout);
//			if(flag){
//				new_viewholder.getFirstline().setVisibility(View.VISIBLE);
//				flag=false;
//			}
             if(TextUtils.isEmpty(info.getSource())){
            	 new_viewholder.getSource().setText(mContext.getResources().getString(R.string.unkown_source));
			}else{
			new_viewholder.getSource().setText(info.getSource());
			}
			new_viewholder.getSize().setText(Util.getGameSize(String.valueOf(info.getFullsize()))+ "MB");					
			int i = 0;
		
			if (info.getSecurityinfo() != null && info.getSecurityinfo().size()>0) {			
				String official = info.getSecurityinfo().get("official").toString();
				String safe = info.getSecurityinfo().get("security").toString();
				String isad = info.getSecurityinfo().get("adsdisplay").toString();
				String charge = info.getSecurityinfo().get("feetype").toString();						
					if (official.equals("1")) {
						if (i < 3) {
							i++;
							new_viewholder.getImageview1().setImageResource(drawables[0]);
							new_viewholder.getImageview1().setVisibility(View.VISIBLE);
						}
					}
					if (safe.equals("0")) {
						if (i < 3) {
							i++;
							new_viewholder.getImageview2().setImageResource(drawables[1]);
							new_viewholder.getImageview2().setVisibility(View.VISIBLE);
						}
					}
					if (isad.equals("0")) {
						if (i < 3) {
							i++;
							new_viewholder.getImageview3().setImageResource(drawables[2]);
							new_viewholder.getImageview3().setVisibility(View.VISIBLE);
						}
					} else if (isad.equals("1")) {
						if (i < 3) {
							i++;
							new_viewholder.getImageview3().setImageResource(drawables[3]);
							new_viewholder.getImageview3().setVisibility(View.VISIBLE);
						}
					}
					if (charge.equals("1")) {
						if (i < 3) {
							i++;
							new_viewholder.getImageview4().setImageResource(drawables[0]);
							new_viewholder.getImageview4().setVisibility(View.VISIBLE);
						}
					}
				}
	
		
		  
			final GameDetailInfo tmp = info;
			row_layout.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
				//	Log.v("----------", ""+position);
				
				   // new_viewholder.getLy().setBackgroundColor(0xffffffff);
					//new_viewholder.getLine().setBackgroundColor(mContext.getResources().getColor(R.color.blue));
				//	getViewHolder((View) v.getParent()).getLine().setBackgroundColor(mContext.getResources().getColor(R.color.security_green));
					Intent intent = new Intent();										
					intent.setClass(mContext, GameCircleDetailActivity.class);
					intent.putExtra(Params.INTRO.GAME_CODE,tmp.getGamecode());										 
					intent.putExtra(Params.INTRO.GAME_NAME, gameName);
					intent.putExtra(Params.INTRO.GAME_DISPFLAG, true);
					intent.putExtra(Params.INTRO.GAME_CIRCLE, false);
					mContext.startActivity(intent);
					activity.finish();
				}
			});
//			row_layout.setOnTouchListener(new OnTouchListener() {									
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					// TODO Auto-generated method stub
//					getViewHolder((View) v.getParent()).getLine().setBackgroundColor(mContext.getResources().getColor(R.color.security_green));
//				//	LayoutParams lp3 = (LayoutParams) tv_stars3.getLayoutParams();
//				//	lp3.width = width3;
//				//	tv_stars3.setLayoutParams(lp3);		
//					LayoutParams lp = (LayoutParams) getViewHolder((View) v.getParent()).getLine().getLayoutParams();
//					lp.width=6;
//					getViewHolder((View) v.getParent()).getLine().setLayoutParams(lp);
//					return false;
//				}
//			});
//			row_layout.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener(){
//				public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus==false){
//				getViewHolder((View) v.getParent()).getLine().setBackgroundColor(mContext.getResources().getColor(R.color.game_detail_table_line));
//                }
//			}
//		});

			viewholder.getTablelayout().addView(row_layout);
		}
         
        return convertView;
    }

//	public ViewHolder getViewHolder(View v) {
//		if (v.getTag() == null) {
//			return getViewHolder((View) v.getParent());
//		}
//		return (ViewHolder) v.getTag();
//	}
}


