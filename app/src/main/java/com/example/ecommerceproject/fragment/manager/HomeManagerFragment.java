package com.example.ecommerceproject.fragment.manager;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.ecommerceproject.R;
import com.example.ecommerceproject.model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeManagerFragment extends Fragment {

    RecyclerView recyclerView;
    EditText edtSearch;
    ImageView imgSearch;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<Product> products;
    ArrayList<String> productKeys;
    RecyclerviewManagerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_manager, container, false);
        initView(view);


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
                adapter = new RecyclerviewManagerAdapter(getContext());
                adapter.setList(products);
                adapter.getKeys(productKeys);
//                System.out.println(productKeys);
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

                Toast.makeText(getContext(), "Search success!", Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(adapter);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView(View view) {

        recyclerView = view.findViewById(R.id.rcv_listItem_manager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        edtSearch = view.findViewById(R.id.edt_searchItem);
        imgSearch = view.findViewById(R.id.img_search);
    }


}