package com.example.assistance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class relogin extends AppCompatActivity {
    private EditText t1,t2;
    TextView passwordreset;
    Button b1;
    public final static String MESSAGE_KEY ="ganeshannt.senddata.message_key";
    FirebaseAuth fauth;
    ProgressBar p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relogin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            t1=findViewById(R.id.edit1);
            t2=findViewById(R.id.edit2);
            fauth= FirebaseAuth.getInstance();
            b1=findViewById(R.id.button1);
            p=findViewById(R.id.progress);
            t1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    t1.setTranslationY(-200f);
                    return false;
                }

            });
            t1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if(keyEvent!=null&&(keyEvent.getKeyCode()==keyEvent.KEYCODE_ENTER)||keyEvent!=null&&(keyEvent.getKeyCode()==keyEvent.KEYCODE_UNKNOWN)){
                       InputMethodManager in=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                         in.hideSoftInputFromWindow(t1.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    t1.setTranslationY(0f);
                    return false;
                }
            });
            t2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    t2.setTranslationY(-300f);
                    t1.setVisibility(View.INVISIBLE);
                    t2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                            if(keyEvent!=null&&(keyEvent.getKeyCode()==keyEvent.KEYCODE_ENTER)||keyEvent!=null&&(keyEvent.getKeyCode()==keyEvent.KEYCODE_UNKNOWN)){
                                InputMethodManager in=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                in.hideSoftInputFromWindow(t2.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                            t2.setTranslationY(0f);
                            t1.setVisibility(View.VISIBLE);
                            return false;
                        }
                    });
                    return false;
                }

            });



            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String em,pass1;
                    em=t1.getText().toString().trim();
                    pass1=t2.getText().toString().trim();
                    if(TextUtils.isEmpty(em)){
                        t1.setError("Enter email");
                    }
                    if(TextUtils.isEmpty(pass1)){
                        t2.setError("Enter password");
                    }
                    if(pass1.length()<=6){
                        t2.setError("Should have greater the 6 words");
                    }
                    else {
                       // p.setVisibility(view.VISIBLE);
                        FirebaseUser user=fauth.getCurrentUser();
                        try {
                                fauth.signInWithEmailAndPassword(em, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            if(true){
                                                Toast.makeText(relogin.this, "Welcome", Toast.LENGTH_SHORT).show();
                                            opentranslator();}
                                            else {
                                                Toast.makeText(relogin.this,"Please verify your mail address",Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(relogin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            //p.setVisibility(view.INVISIBLE);
                                        }

                                    }
                                });

                        }
                        catch(NullPointerException e){
                            Toast.makeText(relogin.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }

                }
            });
        }
        passwordreset=findViewById(R.id.passwordreset);
        passwordreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openforget();
            }
        });
    }
    public void opentranslator(){
        Intent i=new Intent(this,translator.class);
        startActivity(i);
    }
    public void openforget(){
        Intent i=new Intent(this,forgetpassword.class);
        startActivity(i);
    }
}