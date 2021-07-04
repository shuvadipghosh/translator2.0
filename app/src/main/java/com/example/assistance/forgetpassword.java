
package com.example.assistance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetpassword extends AppCompatActivity {


    private EditText t;
    private FirebaseAuth auth;
    private String e;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        auth=FirebaseAuth.getInstance();
        t=findViewById(R.id.email);
        b=findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e=t.getText().toString().trim();
                if(TextUtils.isEmpty(e)){
                    t.setError("Enter email");
                }
                else{
                    auth.sendPasswordResetEmail(e).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(forgetpassword.this,"check your mail....",Toast.LENGTH_SHORT).show();
                           }
                           else{
                               Toast.makeText(forgetpassword.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                           }
                        }
                    });
                }
            }
        });
    }
    public void openrelogin(){
        Intent i=new Intent(this,relogin.class);
        startActivity(i);
    }
}