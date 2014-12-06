package com.cyou.mrd.pengyou.ui;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.utils.FileUtil;

/**
 * 
 * @author tuozhonghua_zk
 *
 */
public class LargerImageViewActivity extends BaseActivity implements OnClickListener {
    
    private ImageView mImageView;
    private PhotoViewAttacher mAttacher;
    
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.enlarge_image);
        initView();
        mImageView = (ImageView)findViewById(R.id.img_add_source);
        String filePath = getIntent().getStringExtra("FILE_PATH");
        File file = new File(filePath);
        Bitmap bitmap = null;
        if (file.exists()) {
            bitmap = FileUtil.createBitMap(file);
        }
        mImageView.setImageBitmap(bitmap);
        // The MAGIC happens here!
        mAttacher = new PhotoViewAttacher(mImageView);
      //显示长图时改变scaletype
        if (bitmap != null) {
            if (bitmap.getHeight() >= bitmap.getWidth() && (bitmap.getHeight()/bitmap.getWidth()) >= ShowLargePhotoActivity.scaleType) {
                mAttacher.setScaleType(ImageView.ScaleType.CENTER);
            }else if (bitmap.getHeight() < bitmap.getWidth() && (bitmap.getWidth()/bitmap.getHeight()) >= ShowLargePhotoActivity.scaleType){
                mAttacher.setScaleType(ImageView.ScaleType.CENTER);
            }
        }
//        mAttacher.setMaxScale(15f);
//        mAttacher.setMidScale(5f);
//        mAttacher.setScaleType(ImageView.ScaleType.CENTER);
        // Lets attach some listeners, not required though!
//        mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
//        mAttacher.setOnPhotoTapListener(new PhotoTapListener());
    }
    
    private void initView () {
        View headerBar = findViewById(R.id.img_add_headerbar);
        ImageButton mBackBtn = (ImageButton) headerBar.findViewById(R.id.sub_header_bar_left_ibtn);
        Button mDeleteBtn = (Button) headerBar.findViewById(R.id.sub_header_bar_right_ibtn);
        mDeleteBtn.setBackgroundResource(R.drawable.header_btn_xbg);
        mDeleteBtn.setText(R.string.delete);
        mBackBtn.setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);
    }
    @Override
    protected void onDestroy() {
        this.finish();
        super.onDestroy();
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sub_header_bar_left_ibtn:
                finish();
                break;
            case R.id.sub_header_bar_right_ibtn:
                Intent intent = new Intent();
//                intent.putExtra(key, value);
                setResult(Activity.RESULT_OK, intent);
                finish();
            break;
            default:
                break;
        }
        
    }
}
