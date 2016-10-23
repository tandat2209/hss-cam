package com.eshss.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.VideoView;


public class ViewStreamActivity extends AppCompatActivity {
    private MjpegView mv;
    MjpegInputStream inputStream;
    private String CameraURL, CameraControlURL;
    VideoView vidView;
    //private static DoRead mDoRead;
    //Hàm create main chính
    private static final String TAG = "MjpegActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stream);

        //sample public cam
//        String URL = "http://192.168.1.239:81/media/?action=stream";
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        mv = new MjpegView(this);
//        setContentView(mv);
//
//        new DoRead().execute(URL);

//        VideoView videoView =(VideoView)findViewById(R.id.Vid);
//        MediaController mediaController= new MediaController(this);
//        mediaController.setAnchorView(videoView);
//        Uri uri=Uri.parse("http://192.168.1.239:81/media/?action=stream&user=admin&pwd=");
//        videoView.setMediaController(mediaController);
//        videoView.setVideoURI(uri);
//        videoView.requestFocus();
//        videoView.start();


//        CameraURL = (String) getResources().getText(R.string.default_camURL);
//
//
//        mv = new MjpegView(this);
//        View stolenView = mv;
//
//        setContentView(R.layout.activity_view_stream);
//        View view = (findViewById(R.id.Vid));
//        ((ViewGroup) view).addView(stolenView);
//        mDoRead = new DoRead();
//        mDoRead.execute(CameraURL);
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        loadPref();
//    }

    public void onPause() {
        super.onPause();
        mv.stopPlayback();
    }

//    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
//        protected MjpegInputStream doInBackground(String... url) {
//            //TODO: if camera has authentication deal with it and don't just not work
//            HttpResponse res = null;
//            DefaultHttpClient httpclient = new DefaultHttpClient();
//            Log.d(TAG, "1. Sending http request");
//            try {
//                res = (HttpResponse) httpclient.execute((HttpUriRequest) new HttpGet(URI.create(url[0])));
//                Log.d(TAG, "2. Request finished, status = " + res.getStatusLine().getStatusCode());
//                if(res.getStatusLine().getStatusCode()==401){
//                    //You must turn off camera User Access Control before this will work
//                    return null;
//                }
//                return new MjpegInputStream(res.getEntity().getContent());
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//                Log.d(TAG, "Request failed-ClientProtocolException", e);
//                //Error connecting to camera
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.d(TAG, "Request failed-IOException", e);
//                //Error connecting to camera
//            }
//
//            return null;
//        }
//
//        protected void onPostExecute(MjpegInputStream result) {
//            mv.setSource(result);
//            mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
//            mv.showFps(true);
//        }
//    }


//    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
//        protected MjpegInputStream doInBackground(String... url) {
//            //TODO: if camera has authentication deal with it and don't just not work
//            HttpResponse res = null;
//            org.apache.http.impl.client.DefaultHttpClient httpclient = new org.apache.http.impl.client.DefaultHttpClient();
//            Log.d("helo", "1. Sending http request");
//            try {
//                res = httpclient.execute(new HttpGet(URI.create(url[0])));
//                Log.d("helo", "2. Request finished, status = " + res.getStatusLine().getStatusCode());
//                if (res.getStatusLine().getStatusCode() == 401) {
//                    //You must turn off camera User Access Control before this will work
//                    return null;
//                }
//                return new MjpegInputStream(res.getEntity().getContent());
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//                Log.d("helo", "Request failed-ClientProtocolException", e);
//                //Error connecting to camera
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.d("helo", "Request failed-IOException", e);
//                //Error connecting to camera
//            }
//            return null;
//        }
//
//        protected void onPostExecute(MjpegInputStream result) {
//            if (mv != null) {
//                mv.setSource(result);
//                mv.setDisplayMode(MjpegView.SIZE_STANDARD);   //SIZE_BEST_FIT
//                mv.showFps(true);
//            }
//        }
//
//    }



    //Xem video stream.
//    private void loadPref(){
//        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        CameraURL = mySharedPreferences.getString("pref_camURL", CameraURL);
//    }
//


    //Tạo nút setting 3 chấm phía trên header


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;

    }

    // WAY2
//    @Override
//    protected void onPause() {
//        if (mDoRead != null && !mDoRead.isCancelled()) {
//            mDoRead.cancel(true);
//            Log.d("TAG", "Cancel");
//        }
//        try {
//            Thread.sleep(501); // Don't do this!!! Only for testing purposes!!!
//        } catch (Exception e) {
//        }
//
//        super.onPause();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        Intent intent = new Intent();
//        intent.setClass(ViewStreamActivity.this, ControlActivity.class);
//        startActivityForResult(intent, 0);

        Intent intent = new Intent(ViewStreamActivity.this, ControlActivity.class);
        startActivity(intent);
        onPause();
        return true;

    }
}
