package com.example.bustrucker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginact extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button log, reg;
    EditText email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginact);

        mAuth = FirebaseAuth.getInstance();

        log = findViewById(R.id.login);
        reg = findViewById(R.id.regester);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {

                mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            if (email.getText().toString().contains(".dd")){

                                Intent intent = new Intent(loginact.this, Driver.class);
                                startActivity(intent);

                            }
                            else if (email.getText().toString().contains(".mm")){

                                Intent intent = new Intent(loginact.this, AdminActivity.class);
                                startActivity(intent);

                            }
                            else{
                                Toast.makeText(loginact.this, email.getText().toString()
                                        , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(loginact.this, passactivety.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                        else
                            Toast.makeText(loginact.this, "account not found", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(loginact.this, Register.class);
                startActivity(intent);

            }
        });

    }


}
