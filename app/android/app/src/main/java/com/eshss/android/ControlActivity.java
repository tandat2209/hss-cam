package com.eshss.android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;


public class ControlActivity extends AppCompatActivity {
    private static final boolean DEBUG=false;
    private static final String TAG = "MJPEG";

    private ImageButton btn_cam_up, btn_cam_down, btn_cam_left, btn_cam_right;
    private ImageButton btn_cam_up_b, btn_cam_down_b, btn_cam_left_b, btn_cam_right_b;
    private WebView webView;
    private Switch swichMode;

    // for emulator android genymotion
    // TODO: fix for real device
    private String webViewURL = "http://10.0.3.2:5000";

    private String CameraStreamURL = "http://192.168.1.239:81/media/?user=admin&pwd=&action=stream",
            CameraControlURL = "http://192.168.1.239:81/media/?user=admin&pwd=&action=cmd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        webView = (WebView) findViewById(R.id.webView);
        btn_cam_up = (Button) findViewById(R.id.button_top);
        btn_cam_down = (Button) findViewById(R.id.button_bot);
        btn_cam_left = (Button) findViewById(R.id.button_left);
        btn_cam_right = (Button) findViewById(R.id.button_right);

        // load video inside webview
//        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(webViewURL);

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

        swichMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    btn_cam_up.setEnabled(false);
                    btn_cam_down.setEnabled(false);
                    btn_cam_left.setEnabled(false);
                    btn_cam_right.setEnabled(false);
                } else {
                    btn_cam_up.setEnabled(true);
                    btn_cam_down.setEnabled(true);
                    btn_cam_left.setEnabled(true);
                    btn_cam_right.setEnabled(true);
                }
            }
        });


    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
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




