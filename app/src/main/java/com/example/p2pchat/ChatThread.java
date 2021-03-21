package com.example.p2pchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatThread extends AppCompatActivity {



    static ConstraintLayout constraintLayout;

    String fileMessage ="";

    private Toolbar mTopToolbar;

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    public List<com.example.p2pchat.Message> messageList = new ArrayList<>();

    SendReceive sendReceive;


    ServerClassAsync serverClass;
    ConnectAsync clientClass;

    EditText messageEditText;

    static final int MESSAGE_READ=1;

    //Handler
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_READ:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg=new String(readBuff,0,msg.arg1);

                    String[] messages=tempMsg.split("###",0);
                    if(messages[0].equals("normal")){
                        fileMessage = fileMessage +"\n"+"Other Person: "+messages[1];
                        messageList.add(new com.example.p2pchat.Message(messages[1],"otherMessage"));
                        mMessageRecycler.setAdapter(mMessageAdapter);  //must do this for instant message showing
                    }
                    else if(messages[0].equals("file")){
                        writeToFile("p2p_chat_file", messages[1]);
                    }
                    else if(messages[0].equals("image")){
                        changeBackground(messages[1]);
                    }
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_thread);

        //Constraint Layout
        constraintLayout= (ConstraintLayout) findViewById(R.id.chatThread_layout);


        //Toolbar instantiating
        mTopToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mTopToolbar);


        //Server Creation And Connecting
        if(Connect.getIpAdress() == null && Connect.getPort() == 0){
            serverClass = new ServerClassAsync(ServerCreate.getPort());
            serverClass.execute();
        } else if(ServerCreate.getPort() == 0){
            clientClass = new ConnectAsync(Connect.getIpAdress(),Connect.getPort());
            clientClass.execute();
        }


        //View initializing
        mMessageRecycler = (RecyclerView) findViewById(R.id.recyclerView_message_list);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);

        messageEditText = findViewById(R.id.message_box);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_thread_toolbar, menu);
        return true;
    }

    //Send Button
    public void onSendClicked(View view) {
        String msg=messageEditText.getText().toString();
        messageEditText.getText().clear();

        fileMessage = fileMessage +"\n"+"Me: "+msg;

        //sendReceive.write(msg.getBytes());
        messageList.add(new com.example.p2pchat.Message(msg,"myMessage"));
        mMessageRecycler.setAdapter(mMessageAdapter); //must do this for instant message showing

        msg = "normal###"+msg;
        sendReceive.write(msg.getBytes());
    }

    public void onFileAttachClicked(View view) {
        Intent intent = new Intent().setType("text/plain").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a TXT file"), 123);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode==123) {
            if (resultCode==RESULT_OK) {
                Uri uri = intent.getData();
                String textInsideTheSelectedFile = uriToString(uri);
                textInsideTheSelectedFile="file###"+textInsideTheSelectedFile;
                //Now do your stuff. Must attach a code at the beginning of the string before sending this string.
                sendReceive.write(textInsideTheSelectedFile.getBytes());
            }
        }
    }


    private String uriToString(Uri uri){
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));
            String line = "";

            while ((line = reader.readLine()) != null) {
                builder.append("\n"+line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }





    //Writing to File
    private void writeToFile(String fileName, String data) {
        Long time= System.currentTimeMillis();
        String timeMill = " "+time.toString();
        File defaultDir = Environment.getExternalStorageDirectory();
        File file = new File(defaultDir, fileName+timeMill+".txt");
        FileOutputStream stream;
        try {
            stream = new FileOutputStream(file, false);
            stream.write(data.getBytes());
            stream.close();
            Toast.makeText(ChatThread.this,"file saved in: "+file.getPath(),Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Log.d("TAG", e.toString());
        } catch (IOException e) {
            Log.d("TAG", e.toString());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.changeBackground:
                openDialogue();
                return true;
            case R.id.saveConversation  :
                saveConversation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openDialogue() {
        MyDialogue dialogue = new MyDialogue(sendReceive);
        dialogue.show(getSupportFragmentManager(),"Background Selection");
    }

    private void saveConversation() {
        writeToFile("Conversation",fileMessage);
    }

    public static void changeBackground(String backgroundName) {
        if (backgroundName.equals("Background 1")){
            constraintLayout.setBackgroundResource(R.mipmap.background_1);
        }else if (backgroundName.equals("Background 2")){
            constraintLayout.setBackgroundResource(R.mipmap.background_2);
        }else if (backgroundName.equals("Background 3")){
            constraintLayout.setBackgroundResource(R.mipmap.background_3);
        }
    }












    //Main functionality of Sending and Receiving
    public class SendReceive extends Thread{
        public Socket socket;
        public InputStream inputStream;
        public OutputStream outputStream;

        public SendReceive(Socket skt){
            socket=skt;
            try {
                inputStream=socket.getInputStream();
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            byte[] buffer=new byte[1024];
            int bytes;

            while (socket!=null){
                try {
                    bytes=inputStream.read(buffer);
                    if(bytes>0){
                        handler.obtainMessage(MESSAGE_READ,bytes,-1,buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(final byte[] bytes){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        outputStream.write(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }



    //Server Async

    public class ServerClassAsync extends AsyncTask<Void,Void,Void> {

        Socket soc;
        ServerSocket ss;
        int port;

        public ServerClassAsync(int port){
            this.port = port;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                ss = new ServerSocket(port);
                Log.d("TAG", "Waiting for client...");
                soc = ss.accept();
                Log.d("TAG", "Connection established from server");
                sendReceive=new SendReceive(soc);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    //Connect Async

    public class ConnectAsync extends AsyncTask<Void,Void,Void>{

        Socket soc;
        String hostAddress;
        int port;

        public ConnectAsync (String hostAddress, int port){
            this.hostAddress = hostAddress;
            this.port = port;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                soc = new Socket(hostAddress,port);
                sendReceive=new SendReceive(soc);
                sendReceive.start();
                //soc = SocketSingleton.getInstance(hostAddress,port).getSocket();
                Log.d("TAG", "Client is connected to server");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
