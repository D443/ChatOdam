package proje.chatodam;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;



public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Button btn_getir;
    EditText txt_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_getir = (Button) findViewById(R.id.btn_gonder);
        btn_getir.setOnClickListener(this);
        txt_ip = (EditText) findViewById(R.id.txt_ip);


    }

    @Override
    public void onClick(View v) {
        String ip = txt_ip.getText().toString();
        Log.d("ip = " + ip , "");
        Intent i = new Intent(this,ChatOdasi.class);
        i.putExtra("ip",ip);
        startActivity(i);
    }
}