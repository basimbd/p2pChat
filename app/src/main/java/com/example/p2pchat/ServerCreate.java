package com.example.p2pchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerCreate extends AppCompatActivity {


    private Toolbar mTopToolbar;

    Button startButton;
    TextView portTextView,ip_portTextView;
    EditText portEditText;

    static int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_create);

        //Toolbar instantiate
        mTopToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mTopToolbar);

        startButton = (Button) findViewById(R.id.startButton);
        portTextView = (TextView) findViewById(R.id.portTextView);
        portEditText = (EditText) findViewById(R.id.portEditText);
        ip_portTextView = (TextView) findViewById(R.id.ip_portTextView);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                port = Integer.parseInt(portEditText.getText().toString());
                ip_portTextView.setText("IP: " + getIpAdd() + "\nPort: " + port);
            }
        });
    }

    public String getIpAdd(){
        WifiManager wifiManager = (WifiManager) ServerCreate.this.getApplicationContext().getSystemService(WIFI_SERVICE);
        String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        return ipAddress;
    }

    public static int getPort(){
        return port;
    }

    public void onEnterChatClicked(View view) {
        Intent intent = new Intent(ServerCreate.this, ChatThread.class);
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
