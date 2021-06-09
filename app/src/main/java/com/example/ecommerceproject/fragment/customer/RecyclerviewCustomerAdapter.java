package com.example.ecommerceproject.fragment.customer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceproject.AddCartActivity;
import com.example.ecommerceproject.R;
import com.example.ecommerceproject.ShopActivity;
import com.example.ecommerceproject.UpdateProductActivity;
import com.example.ecommerceproject.fragment.manager.RecyclerviewManagerAdapter;
import com.example.ecommerceproject.model.Cart;
import com.example.ecommerceproject.model.Product;
import com.example.ecommerceproject.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerviewCustomerAdapter extends RecyclerView.Adapter<RecyclerviewCustomerAdapter.ProductHolder> {
    Context context;
    ArrayList<Product> list = new ArrayList<>();
    ArrayList<String> listKeys = new ArrayList<>();
//    FirebaseDatabase database;
//    DatabaseReference reference;
//    ArrayList<Cart> listCart = new ArrayList<>();
    public RecyclerviewCustomerAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<Product> list) {
        this.list = list;
    }
    public void getKeys(ArrayList<String> listKeys) {
        this.listKeys = listKeys;
    }
    @NonNull
    @Override
    public RecyclerviewCustomerAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_customer, null);
        return new RecyclerviewCustomerAdapter.ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewCustomerAdapter.ProductHolder holder, int position) {
        String key = listKeys.get(position);
        Product p = list.get(position);
        holder.txtID.setText("ID: "+ p.getId());
        holder.txtName.setText("Name: " + p.getName());
        holder.txtQuantity.setText("Quantity: " + p.getQuantity());
        holder.txtPrice.setText("Price: " + p.getPrice());
        holder.imageItem.setImageResource(p.getImage());
        p.setKey(key);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddCartActivity.class);
                intent.putExtra("product_addCart", p);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        };
        return 0;
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        TextView txtID, txtName, txtQuantity, txtPrice;
        ImageView imageItem;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.txt_idItem_customer);
            txtName = itemView.findViewById(R.id.txt_nameItem_customer);
            txtQuantity = itemView.findViewById(R.id.txt_quantityItem_customer);
            txtPrice = itemView.findViewById(R.id.txt_priceItem_customer);
            imageItem = itemView.findViewById(R.id.imgItem_customer);
        }
    }
}
