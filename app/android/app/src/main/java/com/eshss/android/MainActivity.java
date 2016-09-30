package com.eshss.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnConnect;
    ListView listView=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=new ListView(this);
        String[] items= {"Robot", "Arduino"," Arduino2","Arduino3","Arduino4","Arduino5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtitem,items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup vg = (ViewGroup) view;
                TextView txt = (TextView) vg.findViewById(R.id.txtitem);
                Toast.makeText(MainActivity.this, txt.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });


//
//        TextView textv = (TextView) findViewById(R.id.textView1);
//        textv.setShadowLayer(1, 3, 3, Color.GRAY);
//
//        btnConnect = (Button) findViewById(R.id.button_connect);
//        btnConnect.setOnClickListener((View.OnClickListener) this);
    }

    public void showDialogListView(View view){

        AlertDialog.Builder builder=new
                AlertDialog.Builder(MainActivity.this);

        builder.setCancelable(true);

//        builder.setPositiveButton("OK",null);
        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // Write your code here to execute after
                        // dialog

                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // Write your code here to execute after
                        // dialog

                        Intent intent_buttons = new Intent(MainActivity.this, ViewStreamActivity.class);
                        startActivity(intent_buttons);
                    }
                });



        builder.setView(listView);

        AlertDialog dialog=builder.create();

        dialog.show();



    }

//
//    public void onClick(View v) {
//        Intent intent_buttons = new Intent(this, ViewStreamActivity.class);
//        startActivity(intent_buttons);
//
//
//        Button showPopUpButton = (Button) findViewById(R.id.button_connect);
//        showPopUpButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                showPopUp3();
//            }
//        });
//
//    }
//
//
//    private void showPopUp2() {
//
//        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
//        helpBuilder.setTitle("Please Connect");
//        helpBuilder.setMessage("Choose Divices ");
//        helpBuilder.setPositiveButton("Positive",
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Do nothing but close the dialog
//                    }
//                });
//
//        helpBuilder.setNegativeButton("Negative", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Do nothing
//            }
//        });
//
//        helpBuilder.setNeutralButton("Neutral", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Do nothing
//            }
//        });
//
//        // Remember, create doesn't show the dialog
//        AlertDialog helpDialog = helpBuilder.create();
//        helpDialog.show();
//
//    }
//
//    private void showPopUp3() {
//
//        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
//        helpBuilder.setTitle("Please Connect");
//        helpBuilder.setMessage("Choose Divices");
//
//        LayoutInflater inflater = getLayoutInflater();
//        View checkboxLayout = inflater.inflate(R.layout.popuplayout, null);
//        helpBuilder.setView(checkboxLayout);
//
//        helpBuilder.setPositiveButton("Connect",
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Do nothing but close the dialog
//                    }
//                });
//
//        // Remember, create doesn't show the dialog
//        AlertDialog helpDialog = helpBuilder.create();
//        helpDialog.show();
//
//    }
}