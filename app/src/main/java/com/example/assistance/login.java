package com.example.assistance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

import java.io.LineNumberReader;

public class login extends AppCompatActivity {
   Button b1,b2;
   private EditText t1,t2,t3,t4;
   FirebaseAuth fauth;
   ProgressBar p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            t1=findViewById(R.id.user);
            t2=findViewById(R.id.email);
            t3=findViewById(R.id.password1);
            t4=findViewById(R.id.password2) ;
            b1=findViewById(R.id.button1);
            b2=findViewById(R.id.button2);
            fauth=FirebaseAuth.getInstance();
            p=findViewById(R.id.progress);
           if(fauth.getCurrentUser()!=null&&fauth.getCurrentUser().isEmailVerified()){
               openmain();
           }

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String em = "", pass1 = "", pass2 = "", us = "";
                    us = t1.getText().toString().trim();
                    em = t2.getText().toString().trim();
                    pass1 = t3.getText().toString().trim();
                    pass2 = t4.getText().toString().trim();
                    boolean eq = pass1.equals(pass2);
                    if (TextUtils.isEmpty(em)) {
                        t2.setError("Enter email");
                    }
                    else if (us.equals("")) {
                        t1.setError("Enter username");
                    }

                    else if (TextUtils.isEmpty(pass1)) {
                        t3.setError("Enter password");
                    }
                    else if (pass1.length() <= 6) {
                        t3.setError("Should have greater the 6 words");
                    }
                    else if (TextUtils.isEmpty(pass2)) {
                        t4.setError("Enter password");
                    }
                    else if (eq == false) {
                        t4.setError("Renter correct password");

                    } else{
                        p.setVisibility(view.VISIBLE);
                        fauth.createUserWithEmailAndPassword(em, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(login.this, "Welcome", Toast.LENGTH_SHORT).show();
                                FirebaseUser user=fauth.getCurrentUser();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                      Toast.makeText(login.this,"Verification mail has been sent...",Toast.LENGTH_SHORT).show();

                                      openrelogin();
                                    }
                                });
                                //openmain();
                            } else {
                                Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                p.setVisibility(view.INVISIBLE);
                            }
                        }
                    });


                }
                }
            });

            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openrelogin();
                }
            });

    }


        }
    public void openmain(){
        Intent intent = new Intent(this,translator.class);
        startActivity(intent);
    }
    public  void openrelogin(){
        Intent intent=new Intent(this,relogin.class);
        startActivity(intent);
    }
}