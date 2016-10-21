package com.eshss.android;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import org.apache.http.impl.client.DefaultHttpClient;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;


public class ControlActivity extends AppCompatActivity {
    Button btnCaptureimages, btnViewImage;
    VideoView vidView;
    private Button btn_cam_up, btn_cam_down, btn_cam_left, btn_cam_right;
    private MjpegView mv;
    MjpegInputStream inputStream;
    private String CameraStreamURL = "http://192.168.1.239:81/media/?user=admin&pwd=&action=stream",
            CameraControlURL = "http://192.168.1.239:81/media/?user=admin&pwd=&action=cmd";
    VideoView videoView;
    private static DoRead mDoRead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
//
//        vidView = (VideoView)findViewById(R.id.videoView);
//        String vidAddress = "http://iris.not.iac.es/axis-cgi/mjpg/video.cgi";
//        Uri vidUri = Uri.parse(vidAddress);
//        vidView.setVideoPath(vidAddress);
//        MediaController vidControl = new MediaController(this);
//        vidControl.setAnchorView(vidView);
//        vidView.setMediaController(vidControl);
//
//        vidView.start();

//
//https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4
//        VideoView videoView =(VideoView)findViewById(R.id.videoView);
//        MediaController mediaController= new MediaController(this);
//        mediaController.setAnchorView(videoView);
//        String urlComputerLocal = "http://192.168.137.190:5000",
//                urlCameraInternet = "http://iris.not.iac.es/axis-cgi/mjpg/video.cgi";
//        Uri uri = Uri.parse(urlComputerLocal);
//        videoView.setMediaController(mediaController);
//        videoView.setVideoURI(uri);
//        videoView.requestFocus();
//        videoView.start();

//        CameraURL = (String) getResources().getText(R.string.default_camURL);

//        loadPref();




//        mv = new MjpegView(this);
//        View stolenView = mv;
//        setContentView(R.layout.activity_view_stream);
//        View view =(findViewById(R.id.videoView));
//        ((ViewGroup) view).addView(stolenView);
//        new DoRead().execute(CameraStreamURL);





//        CameraURL = (String) getResources().getText(R.string.default_camURL);
//
//        loadPref();

//        mv = new MjpegView(this);
//        View stolenView = mv;

//        setContentView(R.layout.activity_view_stream);
//        View view = (findViewById(R.id.Vid));
//        ((ViewGroup) view).addView(stolenView);
//        mDoRead = new DoRead();
//        mDoRead.execute(CameraURL);



//        btnViewImage = (Button)findViewById(R.id.button_viewimages);
//
//        btnViewImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(ControlActivity.this, CaptureImageActivity.class);
//                startActivity(intent);
//            }
//        });

        btn_cam_up = (Button) findViewById(R.id.button_top);
        btn_cam_down = (Button) findViewById(R.id.button_bot);
        btn_cam_left = (Button) findViewById(R.id.button_left);
        btn_cam_right = (Button) findViewById(R.id.button_right);

        btn_cam_left.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    String URL = CameraControlURL + "&code=2&value=3";
                    new HandlingData().execute(URL);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    String URL = CameraControlURL + "&code=3&value=3";
                    new HandlingData().execute(URL);
                }
                return false;
            }
        });
        btn_cam_right.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    String URL = CameraControlURL + "&code=2&value=4";
                    new HandlingData().execute(URL);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    String URL = CameraControlURL + "&code=3&value=4";
                    new HandlingData().execute(URL);
                }
                return false;
            }
        });
        btn_cam_up.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    String URL = CameraControlURL + "&code=2&value=1";
                    new HandlingData().execute(URL);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    String URL = CameraControlURL + "&code=3&value=1";
                    new HandlingData().execute(URL);
                }
                return false;
            }
        });
        btn_cam_down.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    String URL = CameraControlURL + "&code=2&value=2";
                    new HandlingData().execute(URL);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    String URL = CameraControlURL + "&code=3&value=2";
                    new HandlingData().execute(URL);
                }
                return false;
            }
        });
    }
    // Xử lý URL
    private class HandlingData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
        }
    }

    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... url) {
            //TODO: if camera has authentication deal with it and don't just not work
            HttpResponse res = null;
            org.apache.http.impl.client.DefaultHttpClient httpclient = new org.apache.http.impl.client.DefaultHttpClient();
            Log.d("helo", "1. Sending http request");
            try {
                res = httpclient.execute(new HttpGet(URI.create(url[0])));
                Log.d("helo", "2. Request finished, status = " + res.getStatusLine().getStatusCode());
                if (res.getStatusLine().getStatusCode() == 401) {
                    //You must turn off camera User Access Control before this will work
                    return null;
                }
                return new MjpegInputStream(res.getEntity().getContent());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                Log.d("helo", "Request failed-ClientProtocolException", e);
                //Error connecting to camera
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("helo", "Request failed-IOException", e);
                //Error connecting to camera
            }
            return null;
        }

        protected void onPostExecute(MjpegInputStream result) {
            if (mv != null) {
                mv.setSource(result);
                mv.setDisplayMode(MjpegView.SIZE_STANDARD);   //SIZE_BEST_FIT
                mv.showFps(true);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity2_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(ControlActivity.this, ViewStreamActivity.class);
        startActivity(intent);
        return true;
    }
}




