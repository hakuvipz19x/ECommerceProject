package com.example.ecommerceproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.ecommerceproject.fragment.manager.HomeManagerFragment;
import com.example.ecommerceproject.fragment.manager.ProductManageFragment;

public class BottomNavigationManagerAdapter extends FragmentStatePagerAdapter {
    int numPage = 2;
    public BottomNavigationManagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeManagerFragment();
            case 1:
                return new ProductManageFragment();
            default:
                return new HomeManagerFragment();

        }
    }

    @Override
    public int getCount() {
        return numPage;
    }
}
