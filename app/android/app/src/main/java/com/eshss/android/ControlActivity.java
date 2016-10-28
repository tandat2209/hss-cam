package com.eshss.android;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.UUID;


public class ControlActivity extends AppCompatActivity {
    private static final boolean DEBUG=false;
    private static final String TAG = "MJPEG";

    private ProgressDialog mProgressDialog;

    BluetoothAdapter mBluetoothAdapter = null;

    BluetoothSocket mBluetoothSocket = null;

    BluetoothDevice bluetoothDevice = null;

    private boolean isBTConnected = false;


    private ImageButton btn_cam_up, btn_cam_down, btn_cam_left, btn_cam_right, btn_fullscreen;
    private ImageButton btn_cam_up_b, btn_cam_down_b, btn_cam_left_b, btn_cam_right_b;
    private WebView webView;
    private Switch swichMode;

    // for emulator android genymotion
    // TODO: fix for real device
    private String webViewURL = MyCamera.StreamURL;

    private String mAddress;

    private boolean isAutoMode = false;

    private MyCamera camera = new MyCamera();
    private ReadDataFromBT readDataFromBT = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_control);

        Intent newIntent = getIntent();
        mAddress = newIntent.getStringExtra(DeviceListFragment.EXTRA_ADDRESS);
        if(mAddress != null) new ConnectBT().execute();
        webView = (WebView) findViewById(R.id.webView);
        btn_cam_up = (ImageButton) findViewById(R.id.button_top);
        btn_cam_down = (ImageButton) findViewById(R.id.button_bot);
        btn_cam_left = (ImageButton) findViewById(R.id.button_left);
        btn_cam_right = (ImageButton) findViewById(R.id.button_right);
        btn_fullscreen = (ImageButton) findViewById(R.id.btn_fullscreen);
        swichMode = (Switch) findViewById(R.id.switch_auto);

        // load video inside webview
        webView.setWebViewClient(new WebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        webView.loadUrl(webViewURL);

        btn_cam_left.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(!isAutoMode){
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        camera.turn(MyCamera.LEFT);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        camera.stop(MyCamera.LEFT);
                    }
                }
                return true;
            }
        });
        btn_cam_right.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(!isAutoMode){
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        camera.turn(MyCamera.RIGHT);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        camera.stop(MyCamera.RIGHT);
                    }
                }
                return true;
            }
        });
        btn_cam_up.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(!isAutoMode){
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        camera.turn(MyCamera.UP);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        camera.stop(MyCamera.UP);
                    }
                }
                return true;
            }
        });
        btn_cam_down.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(!isAutoMode){
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        camera.turn(MyCamera.DOWN);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        camera.stop(MyCamera.DOWN);
                    }
                }
                return true;
            }
        });
//
        btn_fullscreen.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(ControlActivity.this, ViewStreamActivity.class);
                startActivity(intent);
                return true;
            }
        });
//
//
        swichMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("SWITCH", isChecked+"");
                if(isChecked) {
                    if(!isBTConnected) {
                        Intent intent = new Intent(ControlActivity.this, BluetoothActivity.class);
                        startActivity(intent);
                    } else{
                        if(readDataFromBT == null || readDataFromBT.isCancelled()){
                            readDataFromBT = new ReadDataFromBT();
                            readDataFromBT.execute();
                        }
                        isAutoMode = true;
                        swichMode.setChecked(true);
                    }

                } else {
                    if(readDataFromBT != null && !readDataFromBT.isCancelled()){
                        readDataFromBT.cancel(true);
                    }
                    swichMode.setChecked(false);
                    isAutoMode = false;
                }
            }
        });


    }

//
//    // Xử lý URL
//    private class HandlingData extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//            String response = "";
//            for (String url : urls) {
//                DefaultHttpClient client = new DefaultHttpClient();
//                HttpGet httpGet = new HttpGet(url);
//                try {
//                    HttpResponse execute = client.execute(httpGet);
//                    InputStream content = execute.getEntity().getContent();
//
//                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
//                    String s = "";
//                    while ((s = buffer.readLine()) != null) {
//                        response += s;
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            //textView.setText(result);
//        }
//    }

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


    private class ConnectBT extends AsyncTask<Void, Void, Void> {

        private boolean isConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(ControlActivity.this, "Connecting...", "Please wait!!!");
        }

        @Override
        protected Void doInBackground(Void... devices) {

            if (mBluetoothSocket == null || !isBTConnected) {
                try {

                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    bluetoothDevice = mBluetoothAdapter.getRemoteDevice(mAddress);
                    UUID uuid = UUID.fromString("fb36491d-7c21-40ef-9f67-a63237b5bbea");
                    mBluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBluetoothSocket.connect();

                } catch (IOException e) {
                    try {
                        // try again
                        mBluetoothSocket =(BluetoothSocket) bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(bluetoothDevice,1);
                        mBluetoothSocket.connect();
                    } catch(Exception ex) {
                        ex.printStackTrace();
                        isConnectSuccess = false;
                    }
                    isConnectSuccess = true;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!isConnectSuccess) {
                Toast.makeText(getApplicationContext(),"Connection Failed. Is it a SPP Bluetooth? Try again", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth connected", Toast.LENGTH_LONG).show();
                isBTConnected = true;
                readDataFromBT = new ReadDataFromBT();
                readDataFromBT.execute();
                isAutoMode = true;
                swichMode.setChecked(true);
            }
            mProgressDialog.dismiss();
        }
    }


    private class ReadDataFromBT extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            byte[] buffer = new byte[1024];
            int bytes;
            String msgReceived = "";
            while (!isCancelled()) {
                try {
                    InputStream input = mBluetoothSocket.getInputStream();
                    DataInputStream inputStream = new DataInputStream(input);
                    bytes = inputStream.read(buffer);
                    String strReceived = new String(buffer, 0, bytes);
                    msgReceived = String.valueOf(bytes) +
                            " bytes received:\n"
                            + strReceived;
                    Log.d("CONTROLACTIVITY", msgReceived);
                    if(isAutoMode){
                        switch (strReceived){
                            case "L":
                                camera.execute(MyCamera.CameraTurnLeftURL);
                                break;
                            case "R":
                                camera.execute(MyCamera.CameraTurnRightURL);
                                break;
                            default:
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isAutoMode = false;
                    isBTConnected = false;
                    cancel(true);
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(!isAutoMode){
                swichMode.setChecked(isAutoMode);
            }
        }
    }
}




