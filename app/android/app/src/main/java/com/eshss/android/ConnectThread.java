package com.eshss.android;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by tandat on 10/24/16.
 */

public class ConnectThread extends Thread{

    private BluetoothDevice bTDevice;
    private BluetoothSocket bTSocket;

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
                return false;
            }
            return true;
        }
        return true;
    }

    public boolean cancel() {
        try {
            bTSocket.close();
        } catch(IOException e) {
            return false;
        }
        return true;
    }

}
