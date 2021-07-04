package com.example.assistance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import java.util.Locale;

public class dictionary extends AppCompatActivity {
    Button b, b1, b2;
    EditText txt;
    TextView txt1;
    ImageView i;
    TextToSpeech ts;
    String s = "";
    AutoCompleteTextView auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
            i = findViewById(R.id.myke);
            b = findViewById(R.id.language);
            b1 = findViewById(R.id.ans);
            txt = findViewById(R.id.txt1);
            txt1 = findViewById(R.id.result);
            b2 = findViewById(R.id.speaker);
            auto = findViewById(R.id.auto1);
            String[] ar = new String[]{"en_US", "hi", "es", "fr", "ja",
                    "ru", "en_GB", "de", "it", "ko",
                    "pt - BR", "ar", "tr"};
            ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ar);
            auto.setAdapter(ad);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    auto.showDropDown();
                }
            });
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(dictionary.this, "Listening...", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    if (i.resolveActivity(getPackageManager()) != null) {
                        try{
                        startActivityForResult(i, 100);}
                        catch(Exception e){
                            Toast.makeText(dictionary.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(dictionary.this, "This feature is not supported in your device", Toast.LENGTH_SHORT).show();
                    }
                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!Python.isStarted()) {
                                Python.start(new AndroidPlatform(dictionary.this));
                            }
                            Toast.makeText(dictionary.this, "Searching our database.....", Toast.LENGTH_SHORT).show();
                            Python py = Python.getInstance();
                            PyObject object = py.getModule("dictionary");
                            s = auto.getText().toString();
                            PyObject ob=object.callAttr("dict",s,txt.getText().toString());
                            txt1.setVisibility(View.VISIBLE);
                            b2.setVisibility(View.VISIBLE);
                            txt1.setText(ob.toString());
                            if(ob.toString().equals("")){
                                txt1.setText("No data found");
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
                            ts.speak(txt1.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                        }
                    });
                }
            });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 100:

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txt.setText(result.get(0));
                    break;
            }
        }
        catch (Exception e){
            Toast.makeText(dictionary.this, "No data found", Toast.LENGTH_SHORT).show();
        }

    }
}