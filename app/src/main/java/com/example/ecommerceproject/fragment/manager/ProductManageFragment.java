package com.example.ecommerceproject.fragment.manager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ecommerceproject.R;
import com.example.ecommerceproject.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ProductManageFragment extends Fragment {

    private EditText edtID, edtName, edtQuantity, edtPrice;
    private Spinner spinnerImgItem;
    private Button btnAdd;
    private int imageItem;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_manage, container, false);

        initView(view);
        String[] items = {"iphone12", "ssg_note_10", "xiaomi_redmi_note_10", "op1", "op2", "op3"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, items);
        spinnerImgItem.setAdapter(arrayAdapter);
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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtID.getText().toString();
                String name = edtName.getText().toString();
                int quantity = Integer.parseInt(edtQuantity.getText().toString());
                Double price = Double.valueOf(edtPrice.getText().toString());
                int randomNum = new Random().nextInt();
                String productKey = String.valueOf(randomNum);
                if(id.isEmpty()) {
                    edtID.setError("ID is not blank!");
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name.isEmpty()) {
                    edtID.setError("ID is not blank!");
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edtQuantity.toString().isEmpty()) {
                    edtID.setError("ID is not blank!");
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edtPrice.toString().isEmpty()) {
                    edtID.setError("ID is not blank!");
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                reference = database.getReference()
                        .child("Products")
                        .child("Product - " + productKey);

                Map<String, Object> data = new HashMap<>();
                data.put("id", id);
                data.put("name", name);
                data.put("quantity", quantity);
                data.put("price", price);
                data.put("image", imageItem);
                data.put("key", productKey);

                reference.setValue(data);
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    private void initView(View view) {
        edtID = view.findViewById(R.id.edt_addProduct_id);
        edtName = view.findViewById(R.id.edt_addProduct_name);
        edtQuantity = view.findViewById(R.id.edt_addProduct_quantity);
        edtPrice = view.findViewById(R.id.edt_addProduct_Price);
        spinnerImgItem = view.findViewById(R.id.spinner_imageItem);
        btnAdd = view.findViewById(R.id.btn_addProduct);
    }


}