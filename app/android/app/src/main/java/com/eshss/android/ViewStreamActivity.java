package com.eshss.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ViewStreamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stream);
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
