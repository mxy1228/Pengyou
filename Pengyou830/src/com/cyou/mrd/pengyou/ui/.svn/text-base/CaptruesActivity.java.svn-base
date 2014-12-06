package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GalleryAdapter;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.widget.CYGallery;
import com.cyou.mrd.pengyou.widget.PageIndicatorView;

public class CaptruesActivity extends BaseActivity {

    private CYLog log = CYLog.getInstance();

    private CYGallery mGallery;
    private PageIndicatorView mIndicator;
    private  ArrayList<String> bigimgurl,smallimgurl;
   // private int mAnim;// 0-无动画,1-有动画
    private ProgressBar progressBar;
   

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.captrues);
        overridePendingTransition(R.anim.captures_zoomin, 
                0); 
        initView();
        initData();
    }

    private void initView() {
        this.mIndicator = (PageIndicatorView) findViewById(R.id.captrues_indicator);
        this.mGallery = (CYGallery) findViewById(R.id.captrues_gallery);
        this.progressBar=(ProgressBar)findViewById(R.id.captrues_pb);
        this.mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
               // mIndicator.setmCurrentPage(arg2);
            if(bigimgurl!=null && !bigimgurl.isEmpty()){
                mIndicator.setmCurrentPage(arg2%bigimgurl.size());
            }
            }
        });
        mGallery.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                CaptruesActivity.this.finish();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        bigimgurl=new ArrayList<String>();
        smallimgurl=new ArrayList<String>();
        bigimgurl = intent
                .getStringArrayListExtra(Params.CAPTRUE.BIGCAPTRUE);
        smallimgurl = intent
                .getStringArrayListExtra(Params.CAPTRUE.SMALLCAPTRUE);
        //this.mAnim = intent.getIntExtra(Params.CAPTRUE.ANIM, 0);
     //   if(bigimgurl!=null && !bigimgurl.isEmpty()&&smallimgurl!=null && !smallimgurl.isEmpty()){
        GalleryAdapter adapter = new GalleryAdapter(this, bigimgurl,smallimgurl,progressBar);
    //    log.d("bigimgurl  smallimgurl  is not null");
        mGallery.setAdapter(adapter);
    //    }else{
     //   	GalleryAdapter adapter = new GalleryAdapter(this, null,null,progressBar);

     //       mGallery.setAdapter(adapter);
     //   }
        int currentPage = intent.getIntExtra(Params.CAPTRUE.CURRENT_PAGE, 0);
        this.mGallery.setSelection(currentPage);
        if(bigimgurl!=null && !bigimgurl.isEmpty()){
        	 mGallery.setTotalSize(bigimgurl.size());
             this.mIndicator.setmTotalPage(bigimgurl.size());
        }
       
    }

    // @Override
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    // if(mAnim == 1){
    // if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
    // this.finish();
    // // overridePendingTransition(R.anim.activity_null,
    // R.anim.activity_slide_out);
    // }
    // }
    // this.finish();
    // return false;
    // }
}
