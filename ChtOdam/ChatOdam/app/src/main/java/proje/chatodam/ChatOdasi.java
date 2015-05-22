package proje.chatodam;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by MYPC on 6.5.2015.
 */
public class ChatOdasi extends ActionBarActivity {

    private Handler handler = new Handler();
    public ListView msgView;
    public ArrayAdapter<String> msgList;
    public String host;
    Button btnSend;
    public String host2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_odasi);
        Bundle extras = getIntent().getExtras();
        host = extras.getString("ip");

        msgView = (ListView) findViewById(R.id.listView);

        msgList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        msgView.setAdapter(msgList);



        btnSend = (Button) findViewById(R.id.btn_Send);

        mesajGetir();
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final EditText txtEdit = (EditText) findViewById(R.id.txt_inputText);
                serverMesajGonder(txtEdit.getText().toString());
                msgView.smoothScrollToPosition(msgList.getCount() - 1);
                txtEdit.setText("");

            }
        });

//



        //End Receive msg from server//
    }
    public void serverMesajGonder(String str) {

        final String str1=str;
        new Thread(new Runnable() {

            @Override
            public void run() {

                final String host2=host;
               // host="10.0.2.2";

                PrintWriter out;
                try {
                    Socket socket = new Socket(host2, 8008);
                    out = new PrintWriter(socket.getOutputStream());

                    out.println(str1);

                    out.flush();
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }

            }
        }).start();
    }



    public void mesajGetir()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                // TODO Auto-generated method stub


               final String host2=host;
                //final String host="localhost";
                Socket socket = null ;
                BufferedReader in = null;
                try {
                    socket = new Socket(host2,8008);
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                while(true)
                {
                    String msg = null;
                    try {
                        msg = in.readLine();



                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if(msg == null)
                    {
                        break;
                    }
                    else
                    {
                        mesajGoster(msg);
                    }
                }

            }
        }).start();


    }

    public void mesajGoster(String msg)
    {
        final String mssg=msg;
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                msgList.add(mssg);
                msgView.setAdapter(msgList);
                msgView.smoothScrollToPosition(msgList.getCount() - 1);

            }
        });

    }

}