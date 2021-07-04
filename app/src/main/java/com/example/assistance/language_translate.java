package com.example.assistance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import java.util.Locale;

public class language_translate extends AppCompatActivity {
     EditText edit;
     TextView t1,t2;
    Spinner spinner;
    Button b1,b2;
    ImageView i;
    TextToSpeech ts;
    String s="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_translate);
        edit=findViewById(R.id.txt1);
        t1=findViewById(R.id.from);
        t2=findViewById(R.id.result);
        b1=findViewById(R.id.ans);
        i=findViewById(R.id.myke);
        b2=findViewById(R.id.speaker);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(language_translate.this, "Listening...", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                if (i.resolveActivity(getPackageManager()) != null) {
                    try{
                    startActivityForResult(i, 100);}
                    catch (Exception e){
                        Toast.makeText(language_translate.this,"could not listen",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(language_translate.this, "This feature is not supported in your device", Toast.LENGTH_SHORT).show();
                }
            }
        });

         spinner =findViewById(R.id.spinner);
        ArrayAdapter<String>adapter=new ArrayAdapter<>(this,R.layout.custom_spinner,getResources().getStringArray(R.array.list));
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                s=spinner.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(language_translate.this,"Detecting language..",Toast.LENGTH_SHORT).show();
                if (!Python.isStarted()) {
                    Python.start(new AndroidPlatform(language_translate.this));
                }
                try{
                Toast.makeText(language_translate.this, "Searching our database.....", Toast.LENGTH_SHORT).show();
                Python py = Python.getInstance();
                PyObject object = py.getModule("language");
                PyObject ob=object.callAttr("main_process",edit.getText().toString(),s);
                String s2=ob.toString();
                String arr[]=s2.split(",");
                t1.setText(arr[0]);
               t2.setText(arr[1]);
                t2.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);}
                catch (Exception e){
                    Toast.makeText(language_translate.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR) {
                    ts.setLanguage(Locale.UK);
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ts.speak(t2.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case 100:

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edit.setText(result.get(0));
                    break;
            }
        }
        catch (Exception e){
            Toast.makeText(language_translate.this,"could not listen",Toast.LENGTH_SHORT).show();
        }

    }
}