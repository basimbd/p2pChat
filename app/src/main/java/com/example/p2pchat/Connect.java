package com.example.p2pchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;

public class Connect extends AppCompatActivity {

    private Toolbar mTopToolbar;

    EditText ipEditText,portEdit;
    Button connect2;

    static String ipAddress = null;
    static int port = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        mTopToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mTopToolbar);

        ipEditText = (EditText) findViewById(R.id.ipEditText);
        portEdit = (EditText) findViewById(R.id.portEdit);
        connect2 = (Button) findViewById(R.id.connect2);

        connect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipAddress = ipEditText.getText().toString();
                port = Integer.parseInt(portEdit.getText().toString());
                Toast.makeText(Connect.this,"Connect Pressed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static int getPort(){
        return port;
    }

    public static String getIpAdress(){
        return ipAddress;
    }

    public void onEnterChatClicked(View view) {
        Intent intent = new Intent(Connect.this, ChatThread.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.changeTheme:{
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    startActivity(new Intent(getApplicationContext(),this.getClass()));
                    finish();
                    return true;
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    startActivity(new Intent(getApplicationContext(),this.getClass()));
                    finish();
                    return true;
                }
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
