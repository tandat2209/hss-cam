package com.eshss.android;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * Created by tandat on 10/24/16.
 */

public class ConnectThread extends Thread{

    private BluetoothDevice bTDevice;
    private BluetoothSocket bTSocket;
    private BufferedReader mBufferedReader = null;
    private volatile boolean isConnected = false;

    public ConnectThread(BluetoothDevice bTDevice, UUID UUID) {
        BluetoothSocket tmp = null;
        this.bTDevice = bTDevice;

        try {
            tmp = this.bTDevice.createRfcommSocketToServiceRecord(UUID);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        bTSocket = tmp;
    }

    public boolean connect() {

        try {
            bTSocket.connect();
        } catch(IOException e) {
            e.printStackTrace();
            try {
                // try again
                bTSocket =(BluetoothSocket) bTDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(bTDevice,1);
                bTSocket.connect();
            } catch(Exception ex) {
                ex.printStackTrace();
                isConnected = false;
            }
            isConnected = true;
        }
        isConnected = true;
        return isConnected;
    }

    public boolean cancel() {
        isConnected = false;
        try {
            bTSocket.close();
        } catch(IOException e) {
            return false;
        }
        return true;
    }

//    @Override
//    public void run() {
//        super.run();
//        InputStream aStream = null;
//        InputStreamReader aReader = null;
//        try {
//            while(isConnected){
//                aStream = bTSocket.getInputStream();
//                aReader = new InputStreamReader( aStream );
//                mBufferedReader = new BufferedReader( aReader );
//                Log.e("CONNECT_THREAD", mBufferedReader.readLine());
//            }
//
//        } catch ( IOException e ) {
//            Log.e("CONNECT_THREAD", "Could not connect to device", e );
//            close( mBufferedReader );
//            close( aReader );
//            close( aStream );
//            close( bTSocket );
//            e.printStackTrace();
//        }
//    }

    private void close(Closeable aConnectedObject) {
        if ( aConnectedObject == null ) return;
        try {
            aConnectedObject.close();
        } catch ( IOException e ) {
        }
        aConnectedObject = null;
    }
}
