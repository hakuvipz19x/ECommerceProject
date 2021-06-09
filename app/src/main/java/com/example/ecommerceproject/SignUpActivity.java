package com.example.ecommerceproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPassword, edtPhone;
    private Button btnRegister, btnCancel;
    private Integer userNum = new Random().nextInt();
    private String keyUser = Integer.toString(userNum);
    protected FirebaseAuth auth;
    protected DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        initView();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = edtName.getText().toString();
                String e = edtEmail.getText().toString();
                String p = edtPassword.getText().toString();
                String phone = edtPhone.getText().toString();

                if (n.isEmpty()) {
                    edtName.setError("Name is not blank!");
                    return;
                }

                if (e.isEmpty()) {
                    edtEmail.setError("Email is not blank!");
                    return;
                }

                if (!e.contains("@") || !e.contains(".com")) {
                    edtEmail.setError("Email is wrong format");
                    return;
                }

                if (p.isEmpty()) {
                    edtPassword.setError("Password is not blank!");
                    return;
                }

                if (p.length() < 6) {
                    edtPassword.setError("No less than 6");
                    return;
                }

                if (phone.isEmpty()) {
                    edtPhone.setError("Phone is not blank!");
                    return;
                }

                reference = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("Users")
                        .child("user-" + keyUser);

                Map<String, Object> data = new HashMap<>();
                data.put("name", n);
                data.put("email", e);
                data.put("password", p);
                data.put("phone", phone);
                data.put("role", "Customer");
                data.put("key", keyUser);
                auth.createUserWithEmailAndPassword(e, p)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                reference.setValue(data);
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });
    }

    private void initView() {
        edtName = findViewById(R.id.edt_name_register);
        edtEmail = findViewById(R.id.edt_email_register);
        edtPassword = findViewById(R.id.edt_password_register);
        edtPhone = findViewById(R.id.edt_phone_register);
        btnRegister = findViewById(R.id.btn_register);
        btnCancel = findViewById(R.id.btn_cancel);
    }
}