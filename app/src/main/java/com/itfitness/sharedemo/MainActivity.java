package com.itfitness.sharedemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_IMAGE = 1;
    private static final int REQUEST_VIDEO = 2;
    private String[] mPermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private EditText et;
    private Button btText;
    private Button btImg;
    private Button btVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        requestPermissions();
    }

    /**
     * 请求权限
     */
    private void requestPermissions() {
        if (PermissionsUtil.hasPermission(this, mPermissions)) {
            //有访问摄像头的权限
        } else {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {
                    //用户授予了权限
                }


                @Override
                public void permissionDenied(@NonNull String[] permissions) {
                    //用户拒绝了申请
                }
            }, mPermissions);
        }
    }

    private void initView() {
        et = (EditText) findViewById(R.id.et);
        btText = (Button) findViewById(R.id.bt_text);
        btImg = (Button) findViewById(R.id.bt_img);

        btText.setOnClickListener(this);
        btImg.setOnClickListener(this);
        btVideo = (Button) findViewById(R.id.bt_video);
        btVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_text:
                shareText();
                break;
            case R.id.bt_img:
                shareImg();
                break;
            case R.id.bt_video:
                shareVideo();
                break;
        }
    }

    /**
     * 分享视频
     */
    private void shareVideo() {
        try {
            /*打开相册*/
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("video/*");
            startActivityForResult(intent, REQUEST_VIDEO);
        } catch (Exception e) {

        }
    }

    /**
     * 分享图片
     */
    private void shareImg() {
        try {
            /*打开相册*/
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri img = data.getData();
            if (img != null) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*"); //设置MIME类型
                intent.putExtra(Intent.EXTRA_STREAM, img); //需要分享的文件URI
                startActivity(Intent.createChooser(intent, "分享图片"));
            }
        }else if(requestCode == REQUEST_VIDEO && resultCode == Activity.RESULT_OK){
            Uri video = data.getData();
            if (video != null) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("video/*"); //设置MIME类型
                intent.putExtra(Intent.EXTRA_STREAM, video); //需要分享的文件URI
                startActivity(Intent.createChooser(intent, "分享视频"));
            }
        }
    }

    /**
     * 分享文字
     */
    private void shareText() {
        String shareText = et.getText().toString().trim();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain"); //分享的是文本类型
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);//分享出去的内容
        startActivity(Intent.createChooser(shareIntent, "分享文本"));//后面是分享的标题
    }
}
