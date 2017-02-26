package com.clark.videotest;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoView = (VideoView) findViewById(R.id.videoview);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/lucky.mp4";
        /**
         *本地视频播放
         */
        mVideoView.setVideoPath(path);
        /**
         *网络视频播放
         */
//        mVideoView.setVideoURI(Uri.parse(""));
        /**
         *使用MediaController控制视频播放
         */
        MediaController controller = new MediaController(this);
        /**
         *设置VideoView与MediaController关联
         */
        mVideoView.setMediaController(controller);
        /**
         *设置MediaController与VideoView关联
         */
        controller.setMediaPlayer(mVideoView);
    }
}
