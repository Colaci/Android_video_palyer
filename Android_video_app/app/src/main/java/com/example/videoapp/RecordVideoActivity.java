package com.example.videoapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.videoapp.data.VideoResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecordVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private Uri videoUri;
    private static final int REQUEST_VIDEO_CAPTURE = 1;

    private static final int REQUEST_EXTERNAL_CAMERA = 101;

    private final static int REQUEST_PERMISSION = 0x123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);

        videoView = findViewById(R.id.img);
        videoView.setMediaController(new MediaController(this));
        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            if (!checkPermissionAllGranted(mPermissionsArrays)) {
                // 在这里申请相机、存储的权限
                ActivityCompat.requestPermissions(RecordVideoActivity.this,mPermissionsArrays,REQUEST_PERMISSION);
            } else {
                // 打开相机拍摄
                Intent takeVideoIntent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(takeVideoIntent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takeVideoIntent,REQUEST_VIDEO_CAPTURE);
                }
            }
        });

    }
    private String[] mPermissionsArrays = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private boolean checkPermissionAllGranted(String[] permissions) {
        // 6.0以下不需要
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("在RecordVideoActivity",requestCode+""+resultCode);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            // 播放刚才录制的视频
//            videoUri =intent.getData();
//            videoView.setVideoURI(videoUri);
//            videoView.start();
           //录制了文件不知道放在哪里，而且不知道怎么加到mDataSet中，怎么变成VideoResponse格式
            Log.d("uri",""+intent.getData());
            Log.d("以前有多少个",MyAdapter.mDataSet.size()+"");
            VideoResponse.Video temp=new VideoResponse.Video();
            temp.avatar=null;
            temp.description="我的视频";
            temp.id="1";
            temp.likeCount=0;
            temp.nickname="王二";
            temp.url=intent.getData()+"";
            Toast.makeText(this,"录制成功",Toast.LENGTH_SHORT).show();
            MyAdapter.addData(temp);
            Log.d("现在有多少个",MyAdapter.mDataSet.size()+"");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_CAMERA: {
                // 判断权限是否已经授予
                break;
            }
        }
    }
}
