package com.example.ecommerceproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceproject.model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddCartActivity extends AppCompatActivity {

    ImageView imgProduct;
    TextView txtName, txtQuantity, txtPrice;
    Button btnCancel, btnAdd;
    EditText edtQuantity;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);

        initView();
        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("product_addCart");
        imgProduct.setImageResource(product.getImage());
        txtName.setText("Name: " + product.getName());
        txtQuantity.setText("Quantity: " + product.getQuantity());
        txtPrice.setText("Price: " + product.getPrice() + "");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Carts");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtQuantity.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Quantity is not blank!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int quantity = Integer.parseInt(edtQuantity.getText().toString());

                if (quantity == 0) {
                    Toast.makeText(getApplicationContext(), "input quantity", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (quantity > product.getQuantity()) {
                    Toast.makeText(getApplicationContext(), "no more than product quantity", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> data = new HashMap<>();
                data.put("key", product.getKey());
                data.put("quantity", quantity);

                reference.child("Cart - " + product.getKey()).removeValue();
                reference.child("Cart - " + product.getKey()).setValue(data);
                finish();
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
        btnAdd = findViewById(R.id.btn_add_addCart);
        btnCancel = findViewById(R.id.btn_cancel_addCart);
        imgProduct = findViewById(R.id.img_addCart);
        txtName = findViewById(R.id.txt_nameItem_addCart);
        txtQuantity = findViewById(R.id.txt_quantityItem_addCart);
        txtPrice = findViewById(R.id.txt_priceItem_addCart);
        edtQuantity = findViewById(R.id.edt_quantityItem_addCart);
    }
}