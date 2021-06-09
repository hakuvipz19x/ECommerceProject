package com.example.ecommerceproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceproject.fragment.customer.RecyclerviewCustomerAdapter;
import com.example.ecommerceproject.fragment.manager.RecyclerviewManagerAdapter;
import com.example.ecommerceproject.model.Product;
import com.example.ecommerceproject.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.security.AccessController.getContext;

public class ShopActivity extends AppCompatActivity {

    private Button btnLogout, btnChange, btnCart, btnPayment;
    private FirebaseAuth auth;
    RecyclerView recyclerView;
    EditText edtSearch;
    ImageView imgSearch;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<Product> products;
    ArrayList<String> productKeys;
    RecyclerviewCustomerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        initView();

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("customer");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products = new ArrayList<>();
                productKeys = new ArrayList<>();
                for(DataSnapshot item : snapshot.getChildren()) {
                    Product product = item.getValue(Product.class);
                    products.add(product);
                    productKeys.add(product.getKey());
                }
                adapter = new RecyclerviewCustomerAdapter(getApplicationContext());
                adapter.setList(products);
                adapter.getKeys(productKeys);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtSearch.getText().toString();
                if(name.isEmpty()) {
                    adapter.setList(products);
                } else {
                    ArrayList<Product> listProducts = new ArrayList<>();
                    for(Product product : products) {
                        if(product.getName().contains(name.toLowerCase())) {
                            listProducts.add(product);
                        }
                    }
                    adapter.setList(listProducts);
                }
                Toast.makeText(getApplicationContext(), "Search success!", Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(adapter);
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentIntent = new Intent(ShopActivity.this, PaymentActivity.class);
                paymentIntent.putExtra("customer_payment", user);
                startActivity(paymentIntent);
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(ShopActivity.this, CartActivity.class);
                cartIntent.putExtra("customer_cart", user);
                startActivity(cartIntent);
//                Date todayDate = Calendar.getInstance().getTime();
//                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
//                String todayString = formatter.format(todayDate);
//                System.out.println(todayString);
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeIntent = new Intent(ShopActivity.this, ChangeInforActivity.class);
                changeIntent.putExtra("update_user", user);
                startActivity(changeIntent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                finish();
            }
        });
    }

    private void initView() {
        btnLogout = findViewById(R.id.btn_logout_customer);
        btnChange = findViewById(R.id.btn_change_customer);
        btnCart = findViewById(R.id.btn_cart_customer);
        btnPayment = findViewById(R.id.btn_payment_customer);
        imgSearch = findViewById(R.id.img_search);
        edtSearch = findViewById(R.id.edt_searchItem);
        recyclerView = findViewById(R.id.rcv_listItem_customer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

}