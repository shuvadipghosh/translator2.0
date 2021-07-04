package com.example.assistance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyCtorMarker;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class antonym extends AppCompatActivity {
    AutoCompleteTextView at1;
    private static final String TAG = translator.class.getSimpleName();
    ImageView i1;
    EditText txt;
    String s;
    TextView txt1,r;
    Button b,myke;
    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_antonym);
            at1 = findViewById(R.id.auto);
            i1 = findViewById(R.id.drop);
            String[] ar = new String[]{"Synonyms", "Antonyms"};
            ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ar);
            at1.setAdapter(ad);
            i1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    at1.showDropDown();
                }
            });
            txt=findViewById(R.id.type);
            myke=findViewById(R.id.myke);
            r=findViewById(R.id.res);

            myke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Toast.makeText(antonym.this, "Listening...", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                        if (i.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(i, 100);
                        } else {
                            Toast.makeText(antonym.this, "This feature is not supported in your device", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(antonym.this,"Could not listen....",Toast.LENGTH_SHORT).show();

                    }

                }
            });
            bar=findViewById(R.id.progress1);
            b=findViewById(R.id.antonym);
            txt1=findViewById(R.id.result);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bar.setVisibility(View.VISIBLE);
                    Toast.makeText(antonym.this,"Seaching our database.....",Toast.LENGTH_SHORT).show();

                    if(!Python.isStarted()){

                        Python.start(new AndroidPlatform(antonym.this));

                    }


                    Python py=Python.getInstance();
                    PyObject object=py.getModule("antonym-synonym");
                    s=at1.getText().toString();
                    r.setText(s+" of "+txt.getText().toString()+" ...");
                    Log.d(TAG,"s value "+s);
                    PyObject ob=object.callAttr("an_sy",txt.getText().toString(),s);

                    txt1.setVisibility(View.VISIBLE);
                    txt1.setText(ob.toString());
                    bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(antonym.this,"Swipe down to see the answer",Toast.LENGTH_SHORT).show();
                }
            });
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 100:
                    try {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        txt.setText(result.get(0));
                        Log.d(TAG, "speak " + result.get(0));


                        break;
                    }
                    catch(Exception e){
                        Toast.makeText(antonym.this,"Could not listen....",Toast.LENGTH_SHORT).show();
                    }
        }
    }
}