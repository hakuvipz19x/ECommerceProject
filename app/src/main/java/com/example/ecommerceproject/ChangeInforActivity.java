package com.example.ecommerceproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerceproject.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangeInforActivity extends AppCompatActivity {

    private EditText edtName, edtPassword, edtPhone;
    private Button btnUpdate, btnCancel;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_infor);

        initView();
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("update_user");

        //Neu account dang nhap tu ben thu 3 thi disable edtPassword
        if (user.getPassword().equals("null")) {
            edtPassword.setEnabled(false);
        }

        edtName.setText(user.getName());
        edtPhone.setText(user.getPhone());
        edtPassword.setText(user.getPassword());

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users");


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();
                String p = edtPassword.getText().toString();
                if (name.isEmpty()) {
                    edtName.setError("Name is not blank!");
                    return;
                }

                if (phone.isEmpty() && phone.equals("null")) {
                    edtPhone.setError("Phone is not blank!");
                    return;
                }

                if (p.isEmpty()) {
                    edtPassword.setError("Password is not blank!");
                    return;
                }

                if (p.length() < 6 && !(p.equals("null"))) {
                    edtPassword.setError("No less than 6");
                    return;
                }

                Map<String, Object> data = new HashMap<>();
                data.put("email", user.getEmail());
                data.put("name", edtName.getText().toString());
                data.put("password", edtPassword.getText().toString());
                data.put("phone", edtPhone.getText().toString());
                data.put("role", "Customer");
                data.put("key", user.getKey());
                System.out.println(user.getKey());
                reference.child("User - " + user.getKey())
                        .updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Update success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        edtName = findViewById(R.id.edt_name_update);
        edtPassword = findViewById(R.id.edt_password_update);
        edtPhone = findViewById(R.id.edt_phone_update);
        btnUpdate = findViewById(R.id.btn_update);
        btnCancel = findViewById(R.id.btn_cancel_change);
    }
}