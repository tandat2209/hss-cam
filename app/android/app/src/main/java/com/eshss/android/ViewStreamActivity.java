package com.eshss.android;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;


public class ViewStreamActivity extends AppCompatActivity {
    VideoView vidView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stream);
//        vidView = (VideoView)findViewById(R.id.videoView_control);
//        String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
//        Uri vidUri = Uri.parse(vidAddress);
//        vidView.setVideoURI(vidUri);
//        MediaController vidControl = new MediaController(this);
//        vidControl.setAnchorView(vidView);
//        vidView.setMediaController(vidControl);
//
//        vidView.start();
        VideoView videoView =(VideoView)findViewById(R.id.videoView);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        Uri uri=Uri.parse("http://www.phimmoi.net/download/1475052929284f1f77/UGhpbU1vaV8JYwcHL5C8zvNqFKCU0ynM4m8Ys0C7EvJSPSH8GqjM8AXujUaw8lhrW2drMnXuUg9nwEKBQkDTJkHG9fFKnoFfzi8amLUytucSqPv0O5QEcTn.%2AOnQmfFPEN51IUtN~plKK9se7AguOfn1~plKI86dOrs%21vZHvLmiv2I%21vZHXFJjMa69fxUFhe9%21vZHzda6bXH51IClDkwaAPi2YJPtkTV7msFQncZZLQw%21vZHTpt8XzoVf3IYySrHsfBVLrNQJYB.%2AOn~plKV50eufklCqd2WZcV9zxQ0vOsl6~plKbwmSyXBmDxoCYtD.%2AOnkPx4EJ1BrqhKJPuXsc3KL1K~plKVFjl3kaVo07PtcHZexj5tXSd~plKrChB9SyjEp8YN4qT72Hza5YePWug5eAzs4Jbgpg4~plKt55WOlr~plKwL6m6QBOHJ.%2AOnmbVcyU40nIoDaudVNxSGv~plKDsMKMQyqN9vvv6.%2AOnG~plK4MuJNaX.%2AOnwv~plKoiPXuB2Xvcvz~plKjEYQjPWVVlWLC6WPqlCn1vIN2Xp9jSlDjtxa8AF50r10f%21vZHhWnQkG.%2AOneyGJ06B54g4gn2iq4Hiooyt%21vZHAU~plKhFl5Vp7FLfHQUTl%21vZH81M21BjUvk%21vZHcQ4SMPgqleIT35Cvmi5v0fKl8Gz81ouYvg4XXgM015%21vZHhQA2FZ8V8JDjZ5cQen.%2AOnwZ0kb1GCm8jVQkx9oep7wTVHrCodrVVyWhxuQ1NQvttLaV6ouOTrEi7lDJPbrmHtiyyc2BD23775148b1475052929%40v1.3/PhimMoi.Net---Part.1-Pete.va.nguoi.ban.Rong-Petes.Dragon-2016-Vietsub-360p.mp4");
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
      public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent();
        intent.setClass(ViewStreamActivity.this, ControlActivity.class);
        startActivityForResult(intent, 0);

        return true;
    }
}
