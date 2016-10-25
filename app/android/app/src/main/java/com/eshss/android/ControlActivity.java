package com.eshss.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.ExecutionException;


public class ControlActivity extends AppCompatActivity {
    private static final boolean DEBUG=false;
    private static final String TAG = "MJPEG";

    private Button btn_cam_up, btn_cam_down, btn_cam_left, btn_cam_right, btn_capture_image;
    private WebView webView;

    // for emulator android genymotion
    // TODO: fix for real device
    private String webViewURL = "http://192.168.67.101:5000";

    private String CameraStreamURL = "http://192.168.1.239:81/media/?user=admin&pwd=&action=stream",
            CameraControlURL = "http://192.168.1.239:81/media/?user=admin&pwd=&action=cmd",
            CameraCaptureURL = "http://192.168.1.239:81/media/?user=admin&pwd=&action=snapshot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        webView = (WebView) findViewById(R.id.webView);
        btn_cam_up = (Button) findViewById(R.id.button_top);
        btn_cam_down = (Button) findViewById(R.id.button_bot);
        btn_cam_left = (Button) findViewById(R.id.button_left);
        btn_cam_right = (Button) findViewById(R.id.button_right);
        btn_capture_image = (Button) findViewById(R.id.button_captureimages);

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
        btn_capture_image.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
//                    String URL = CameraCaptureURL + "&code=2&value=2";
//                    new HandlingData().execute(URL);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    String URL = CameraCaptureURL;
                    try {
                        Boolean isSaved = new GetImage().execute(URL).get();
                        if(isSaved == false){
                            Toast.makeText(getApplicationContext(), "Can not capture image...", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(getApplicationContext(), "Saved image", Toast.LENGTH_SHORT).show();
//                            sendBroadcast(new Intent(
//                                    Intent.ACTION_MEDIA_MOUNTED,
//                                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
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

    private class GetImage extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... urls) {
            Boolean response = false;
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urls[0]);
            try {
                HttpResponse execute = client.execute(httpGet);
                InputStream instream = execute.getEntity().getContent();
                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                File myDir = new File(root + "/saved_imagesx");
                myDir.mkdirs();
                String fname = "Image-" + System.currentTimeMillis() + ".jpg";
                File file = new File(myDir, fname);
                FileOutputStream fos = new FileOutputStream(file);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int len = 0;
                while ((len = instream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                fos.close();
                Log.d("Snapshot Filename: ", myDir + fname);
                response = true;
                // TODO: can't read images
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
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




