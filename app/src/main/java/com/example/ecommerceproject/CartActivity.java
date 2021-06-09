package com.example.ecommerceproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerceproject.fragment.cart.CartAdapter;
import com.example.ecommerceproject.model.Cart;
import com.example.ecommerceproject.model.Product;
import com.example.ecommerceproject.model.User;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
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

public class CartActivity extends AppCompatActivity {

    Button btnCancel, btnApply;
    RecyclerView recyclerView;
    CartAdapter adapter;
    FirebaseDatabase database;
    ImageView imgLocation;
    EditText edtYourLocation;
    DatabaseReference referenceProduct, referenceCart, referencePayment;
    ArrayList<Product> listProduct;
    ArrayList<Integer> listQuantity;
    ArrayList<Cart> listCarts = new ArrayList<>();
    //    GoogleMap mMap;
//    int PLACE_PICKER_REQUEST = 1;
//    ArrayList<Place.Field> fields;
    String paymentKey = String.valueOf(new Random().nextInt());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initView();
        Intent getIntent = getIntent();

        User user = (User) getIntent.getSerializableExtra("customer_cart");
        database = FirebaseDatabase.getInstance();
        referenceCart = database.getReference().child("Carts");
        //Hien thi product len cart
        referenceCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listQuantity = new ArrayList<>();
//                if (!snapshot.exists()) {
//
//                    Toast.makeText(getApplicationContext(), "There is no product", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                for (DataSnapshot item : snapshot.getChildren()) {
                    listProduct = new ArrayList<>();
                    Cart cart = item.getValue(Cart.class);
                    listCarts.add(cart);
                    listQuantity.add(cart.getQuantity());
                    referenceProduct = database.getReference().child("Products");

                    referenceProduct.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot itemProduct : snapshot.getChildren()) {
                                Product product = itemProduct.getValue(Product.class);
                                if (product.getKey().equals(cart.getKey())) {
                                    listProduct.add(product);
                                    break;
                                }

                            }
//                            System.out.println(listProduct);
                            adapter.setList(listProduct);
                            adapter.setListQuantity(listQuantity);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
//                System.out.println(listProduct);
//                adapter.setList(listProduct);
//                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        Places.initialize(getApplicationContext(), "AIzaSyBNdchUwfvupNq6rFezN8kdFwCOOvQDroU");
//        PlacesClient placesClient = Places.createClient(this);
//        imgLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mapIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(CartActivity.this);
//                startActivityForResult(mapIntent, PLACE_PICKER_REQUEST);
//            }
//        });

//        if (listCarts.size() == 0) {
//            System.out.println(listCarts);
//            System.out.println(listProduct);
//            System.out.println(listQuantity);
//            btnApply.setEnabled(false);
//            btnApply.setActivated(false);
//        }
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtYourLocation.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Your location is not blank!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (listCarts.size() == 0) {
                    Toast.makeText(getApplicationContext(), "There is no product", Toast.LENGTH_SHORT).show();
                    return;
                }
                String paymentKey = String.valueOf(new Random().nextInt());
                String yourLocation = edtYourLocation.getText().toString();
                //send data to firebase
                referencePayment = database.getReference().child("Payments");
                referenceProduct = database.getReference().child("Products");

//                System.out.println(listCarts);
//                System.out.println(listProduct);
//                System.out.println(listQuantity);

                //Update quantity product
                for (int i = 0; i < listProduct.size(); i++) {
                    Map<String, Object> dataProduct = new HashMap<>();
                    Product p = listProduct.get(i);
                    int newQuantity = p.getQuantity() - listQuantity.get(i);
                    dataProduct.put("quantity", newQuantity);
                    referenceProduct.child("Product - " + p.getKey())
                            .updateChildren(dataProduct);
                }

                //Add cart to payment
                for (int i = 0; i < listCarts.size(); i++) {
                    Map<String, Object> dataPayment = new HashMap<>();
                    Cart cart = listCarts.get(i);
                    dataPayment.put("quantity", cart.getQuantity());
                    dataPayment.put("productKey", cart.getKey());
                    dataPayment.put("paymentKey", paymentKey);
                    dataPayment.put("status", "waiting");
                    dataPayment.put("location", yourLocation);
                    dataPayment.put("userKey", user.getKey());
                    Date todayDate = Calendar.getInstance().getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String todayString = formatter.format(todayDate);
                    dataPayment.put("date", todayString);
                    referencePayment.child("Payment - " + paymentKey)
                            .child("Cart - " + cart.getKey()).setValue(dataPayment);


                }

                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                finish();
                referenceCart.removeValue();
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
        btnCancel = findViewById(R.id.btn_cancel_cart);
        btnApply = findViewById(R.id.btn_apply_cart);
        imgLocation = findViewById(R.id.img_yourLocation);
        edtYourLocation = findViewById(R.id.edt_yourLocation);
        recyclerView = findViewById(R.id.rcv_listItem_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(this);
        recyclerView.setAdapter(adapter);


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            Place place = Autocomplete.getPlaceFromIntent(data);
//            String name = place.getName();
//            LatLng latLng = place.getLatLng();
//            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
//            mMap.animateCamera(update);
//        }
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        LatLng sydney = new LatLng(34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker is Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }

}