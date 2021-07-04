package com.example.assistance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class summery extends AppCompatActivity {
EditText editText;
ImageView i;
Button b1,b2;
private static final String TAG = translator.class.getSimpleName();
TextView t;
TextToSpeech ts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summery);
        editText=findViewById(R.id.txt1);
        i=findViewById(R.id.myke);
        b1=findViewById(R.id.ans);
        b2=findViewById(R.id.speaker);
        t=findViewById(R.id.result);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(summery.this, "Listening...", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                if (i.resolveActivity(getPackageManager()) != null) {
                    try{
                        startActivityForResult(i, 100);}
                    catch (Exception e){
                        Toast.makeText(summery.this,"could not listen",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(summery.this, "This feature is not supported in your device", Toast.LENGTH_SHORT).show();
                }
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!Python.isStarted()) {
                            Python.start(new AndroidPlatform(summery.this));
                        }
                        try{
                            Toast.makeText(summery.this, "Summerizing.....", Toast.LENGTH_SHORT).show();
                            Python py = Python.getInstance();
                            String s[]= new String[1];
                            s[0]=editText.getText().toString();
                            PyObject object = py.getModule("summarizer");
                            PyObject ob=object.callAttr("generate_summary",s,5);
                            t.setMovementMethod(new ScrollingMovementMethod());
                            t.setText(ob.toString());
                            t.setVisibility(View.VISIBLE);
                            b2.setVisibility(View.VISIBLE);
                            }
                        catch (Exception e){
                            Toast.makeText(summery.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG,e.getMessage());
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
                        ts.speak(t.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                    }
                });

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
                    editText.setText(result.get(0));
                    break;
            }
        }
        catch (Exception e){
            Toast.makeText(summery.this,"could not listen",Toast.LENGTH_SHORT).show();
        }

    }
    }
