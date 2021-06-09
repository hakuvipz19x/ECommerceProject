package com.example.ecommerceproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ManagerActivity extends AppCompatActivity {

    private Button btnLogout;
    private TextView txtEmail;
    private FirebaseAuth auth;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        initView();
        Intent intent = getIntent();
        String e = intent.getStringExtra("email_manager");
        txtEmail.setText("Hello, " + e);
        auth = FirebaseAuth.getInstance();

        BottomNavigationManagerAdapter adapter = new BottomNavigationManagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(adapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mHomeManager:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.mProductManage:
                        viewPager.setCurrentItem(1);
                        break;
                }
                return true;
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
        btnLogout = findViewById(R.id.btn_logout_manager);
        txtEmail = findViewById(R.id.txt_email_manager);
        bottomNavigationView = findViewById(R.id.bottomNavigation_manager);
        viewPager = findViewById(R.id.viewpager_manager);
    }
}