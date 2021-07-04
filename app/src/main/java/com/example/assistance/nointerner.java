package com.example.assistance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.strictmode.IntentReceiverLeakedViolation;
import android.view.View;
import android.widget.Button;

public class nointerner extends AppCompatActivity {
   boolean internet;
   Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nointerner);
        b=findViewById(R.id.internet_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internet=checkInternetConnection();
                if(internet==true){
                    opentranslator();
                }
                else{
                    opennointernet();
                }
            }
        });

    }
    public boolean checkInternetConnection(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return  connected;
    }
    public void opentranslator(){
        Intent i=new Intent(this,translator.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }
    public void opennointernet(){
        Intent i=new Intent(this,nointerner.class);
        startActivity(i);
    }
}