package com.example.ecommerceproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceproject.fragment.payment.PaymentAdapter;
import com.example.ecommerceproject.model.CartPayment;
import com.example.ecommerceproject.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;

public class PaymentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PaymentAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    Button btnCancel, btnGet;
    EditText edtMM, edtYYYY;
    ImageView imgSearch;
    ArrayList<String> listPaymentKeys = new ArrayList<>();
    ArrayList<String> listUserPaymentKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initView();

        Intent intent = getIntent();
        User u = (User) intent.getSerializableExtra("customer_payment");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {

                    String payment = item.getKey();
                    String[] getPaymentKeys = payment.split("- ");
                    String paymentKey = getPaymentKeys[1];
//                    System.out.println(paymentKey);
                    listPaymentKeys.add(paymentKey);
                    reference.child("Payment - " + paymentKey)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotPayment) {
                                    for (DataSnapshot itemPayment : snapshotPayment.getChildren()) {
                                        if (snapshotPayment.exists()) {
                                            CartPayment cartPayment = itemPayment.getValue(CartPayment.class);
                                            if (cartPayment.getUserKey().equals(u.getKey())) {
                                                listUserPaymentKeys.add(paymentKey);
                                                break;
                                            }

//                                            System.out.println(cartPayment);
//                                            System.out.println(cartPayment.getPaymentKey());
                                        }
                                    }

//                                    System.out.println(listCartPayments);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

//                    System.out.println(listPaymentKeys);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtMM.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Month is not blank!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtYYYY.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Year is not blank!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String mm = edtMM.getText().toString();
                String yyyy = edtYYYY.getText().toString();
                int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
//                System.out.println(currentMonth + " - " + currentYear);
                if (Integer.parseInt(mm) > 12 || Integer.parseInt(mm) == 0) {
                    Toast.makeText(getApplicationContext(), "Month is wrong format!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Integer.parseInt(yyyy) > currentYear) {
                    Toast.makeText(getApplicationContext(), "Date is not in future!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (Integer.parseInt(mm) > currentMonth) {
                        Toast.makeText(getApplicationContext(), "Date is not in future!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (mm.length() == 1) {
                    mm = "0" + mm;
                }
//                System.out.println("Month: " + mm);
                ArrayList<String> listDateUserPaymentKeys = new ArrayList<>();
                for (String payment : listUserPaymentKeys) {
                    String finalMm = mm;
                    reference.child("Payment - " + payment)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot item : snapshot.getChildren()) {
                                        CartPayment cartPayment = item.getValue(CartPayment.class);
//                                        System.out.println(cartPayment.getDate());
//                                        System.out.println(finalMm + "-" + yyyy);
                                        if (cartPayment.getDate().contains(finalMm + "-" + yyyy)) {
                                            listDateUserPaymentKeys.add(payment);
//                                            System.out.println(listDateUserPaymentKeys);
                                            break;
                                        }

                                    }
//                                    System.out.println("Payment hien tai: " + payment);
//                                    System.out.println("Paymentcuoi: " + listUserPaymentKeys.get(listUserPaymentKeys.size() - 1));
                                    if (payment.equals(listUserPaymentKeys.get(listUserPaymentKeys.size() - 1))) {
//                                        adapter.setListUserPaymentKeys(listDateUserPaymentKeys);

//                                        System.out.println(listDateUserPaymentKeys);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }


                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        System.out.println(listDateUserPaymentKeys);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println(u.getKey());
////                System.out.println(listPaymentKeys);
////                System.out.println(listCartPayments);
//                System.out.println(listPaymentKeys);
//                System.out.println(listUserPaymentKeys);
                adapter.setUserKey(u.getKey());
                adapter.setListUserPaymentKeys(listUserPaymentKeys);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
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
        edtMM = findViewById(R.id.edt_dateMM_payment);
        edtYYYY = findViewById(R.id.edt_dateYYYY_payment);
        imgSearch = findViewById(R.id.img_date_payment);
        recyclerView = findViewById(R.id.rcv_payment);
        btnCancel = findViewById(R.id.btn_cancel_payment);
        btnGet = findViewById(R.id.btn_getPayment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PaymentAdapter(this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Payments");


    }
}