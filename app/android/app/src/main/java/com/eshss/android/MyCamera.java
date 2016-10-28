package com.eshss.android;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

/**
 * Created by tandat on 10/27/16.
 */

public class MyCamera {
    private static final String CameraURL = "http://192.168.1.239:81/media/?user=admin&pwd=";
    private static final String CameraControlURL = CameraURL + "&action=cmd";
    private static final String CameraStreamURL = CameraURL + "&action=stream";
    public static final String UP = "&value=1";

    public static final String DOWN = "&value=2";
    public static final String LEFT = "&value=3";
    public static final String RIGHT = "&value=4";
    public static final String CameraTurnLeftURL = CameraControlURL + "&code=2" + LEFT;
    public static final String CameraTurnRightURL = CameraControlURL + "&code=2" + RIGHT;
    public static final String CameraStopTurnLeftURL = CameraControlURL + "&code=3" + LEFT;
    public static final String CameraStopTurnRightURL = CameraControlURL + "&code=3" + RIGHT;

    public static final String StreamURL = "http://192.168.1.101:5000";

    public boolean turn(String direction){
        String url = CameraControlURL + "&code=2"+ direction;
        boolean result = false;
        try {
            result = new ControlCamera().execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean stop(String direction){
        String url = CameraControlURL + "&code=3"+ direction;
        boolean result = false;
        try {
            result = new ControlCamera().execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    private class ControlCamera extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... urls) {
            return MyCamera.this.execute(urls[0]);
        }
    }

    public boolean execute(String url){
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            client.execute(httpGet);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
