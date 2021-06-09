package com.example.ecommerceproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ecommerceproject.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UpdateProductActivity extends AppCompatActivity {

    private EditText edtID, edtName, edtQuantity, edtPrice;
    private Spinner spinnerImgItem;
    private Button btnUpdate, btnDelete, btnCancel;
    private int imageItem;
    private String productKey;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        initView();

        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("update_product");
        edtID.setText(product.getId());
        edtName.setText(product.getName());
        edtQuantity.setText(product.getQuantity() + "");
        edtPrice.setText(product.getPrice() + "");
        imageItem = product.getImage();
        productKey = product.getKey();
        String[] items = {"iphone12", "ssg_note_10", "xiaomi_redmi_note_10", "op1", "op2", "op3"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, items);
        spinnerImgItem.setAdapter(arrayAdapter);

        switch (imageItem) {
            case R.drawable.iphone12:
                spinnerImgItem.setSelection(0);
                break;
            case R.drawable.ssg_note_10:
                spinnerImgItem.setSelection(1);
                break;
            case R.drawable.xiaomi_redmi_note_10:
                spinnerImgItem.setSelection(2);
                break;
            case R.drawable.op1:
                spinnerImgItem.setSelection(3);
                break;
            case R.drawable.op2:
                spinnerImgItem.setSelection(4);
                break;
            case R.drawable.op3:
                spinnerImgItem.setSelection(5);
                break;
        }
        spinnerImgItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (items[0].equals(spinnerImgItem.getItemAtPosition(position).toString())) {
                    imageItem = R.drawable.iphone12;
                }
                if (items[1].equals(spinnerImgItem.getItemAtPosition(position).toString())) {
                    imageItem = R.drawable.ssg_note_10;
                }
                if (items[2].equals(spinnerImgItem.getItemAtPosition(position).toString())) {
                    imageItem = R.drawable.xiaomi_redmi_note_10;
                }
                if (items[3].equals(spinnerImgItem.getItemAtPosition(position).toString())) {
                    imageItem = R.drawable.op1;
                }
                if (items[4].equals(spinnerImgItem.getItemAtPosition(position).toString())) {
                    imageItem = R.drawable.op2;
                }
                if (items[5].equals(spinnerImgItem.getItemAtPosition(position).toString())) {
                    imageItem = R.drawable.op3;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Products");

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtID.getText().toString();
                String name = edtName.getText().toString();
                int quantity = Integer.parseInt(edtQuantity.getText().toString());
                Double price = Double.valueOf(edtPrice.getText().toString());

                if(id.isEmpty()) {
                    edtID.setError("ID is not blank!");
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name.isEmpty()) {
                    edtID.setError("ID is not blank!");
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edtQuantity.toString().isEmpty()) {
                    edtID.setError("ID is not blank!");
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edtPrice.toString().isEmpty()) {
                    edtID.setError("ID is not blank!");
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> data = new HashMap();
                data.put("id", id);
                data.put("name", name);
                data.put("quantity", quantity);
                data.put("price", price);
                data.put("image", imageItem);
                data.put("key", productKey);

                reference.child("Product - " + productKey).removeValue();
                reference.child("Product - " + productKey).setValue(data).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Update success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("Product - " + productKey).removeValue();
                Toast.makeText(getApplicationContext(), "Delete success", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Cancel update", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void initView() {
        edtID = findViewById(R.id.edt_updateProduct_id);
        edtName = findViewById(R.id.edt_updateProduct_name);
        edtQuantity = findViewById(R.id.edt_updateProduct_quantity);
        edtPrice = findViewById(R.id.edt_updateProduct_Price);
        spinnerImgItem = findViewById(R.id.spinner_update_imageItem);
        btnUpdate = findViewById(R.id.btn_updateProduct);
        btnDelete = findViewById(R.id.btn_deleteProduct);
        btnCancel = findViewById(R.id.btn_cancelUpdate);
    }
}