package com.example.bustrucker;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText c_email, c_pass, mobile, fname, lname, conf_password;
    Button c_create;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        c_email = findViewById(R.id.email);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lName);
        conf_password = findViewById(R.id.confirm_password);
        mobile = findViewById(R.id.mobile);
        c_pass = findViewById(R.id.password);
        c_create = findViewById(R.id.reg);

        mRegProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        c_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!c_email.getText().toString().isEmpty() && !fname.getText().toString().isEmpty() &&
                        !lname.getText().toString().isEmpty() && !c_pass.getText().toString().isEmpty() &&
                        !conf_password.getText().toString().isEmpty()
                ) {
                    if ((conf_password.equals(c_pass))) {
                        mAuth.createUserWithEmailAndPassword(c_email.getText().toString().trim(), c_pass.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                            String uid = current_user.getUid();
                                            if (c_email.getText().toString().contains("dd"))
                                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Driver").child(uid);
                                            if (c_email.getText().toString().contains("mm"))
                                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Admin").child(uid);
                                            HashMap<String, String> userMap = new HashMap<>();

                                            userMap.put("Password", c_pass.getText().toString());
                                            userMap.put("firstName", fname.getText().toString());
                                            userMap.put("lastName", lname.getText().toString());
                                            userMap.put("E-mail", c_email.getText().toString());
                                            userMap.put("Seat", "21");
                                            userMap.put("Phone", mobile.getText().toString());
                                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        mRegProgress.show();
                                                        Toast.makeText(Register.this, "Done ! Account created", Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(Register.this, loginact.class);
                                                        startActivity(intent);
                                                        mRegProgress.dismiss();
                                                        finish();
                                                    }
                                                }
                                            });
                                        } else {

                                            Toast.makeText(Register.this, "Can't Create Account, Check Your Information", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else
                        Toast.makeText(Register.this, "Your password and confirm password are not the same", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(Register.this, "Please Fill All required information", Toast.LENGTH_LONG).show();
            }
        });

        }
    }
