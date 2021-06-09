package com.example.ecommerceproject.fragment.cart;

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
import com.example.ecommerceproject.UpdateProductActivity;
import com.example.ecommerceproject.fragment.customer.RecyclerviewCustomerAdapter;
import com.example.ecommerceproject.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ProductHolder> {

    Context context;
    ArrayList<Product> list = new ArrayList<>();
    ArrayList<Integer> listQuantity = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference reference;

    public void setListQuantity(ArrayList<Integer> listQuantity) {
        this.listQuantity = listQuantity;
    }

    public CartAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CartAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_cart, null);
        return new CartAdapter.ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ProductHolder holder, int position) {
        int quantity = 0;
        if (!listQuantity.isEmpty() && position < listQuantity.size()) {
            quantity = listQuantity.get(position);
        }
        Product p = list.get(position);
        holder.txtName.setText("Name: " + p.getName());
        holder.txtQuantity.setText("Quantity: " + p.getQuantity());
        holder.txtPrice.setText("Price: " + p.getPrice());
        holder.imageItem.setImageResource(p.getImage());
        holder.edtQuantity.setText(quantity + "");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Carts");

        holder.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.edtQuantity.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Quantity is not blank!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int quantity = Integer.parseInt(holder.edtQuantity.getText().toString());

                if (quantity == 0) {
                    Toast.makeText(context, "input quantity", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (quantity > p.getQuantity()) {
                    Toast.makeText(context, "no more than product quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> data = new HashMap<>();
                data.put("quantity", quantity);
                reference.child("Cart - " + p.getKey()).updateChildren(data);

                Toast.makeText(context, "update success", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("Cart - " + p.getKey()).removeValue();
                Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();
                list.remove(p);
                notifyDataSetChanged();
            }
        });

    }

    public ArrayList<Integer> getListQuantity() {
        return this.listQuantity;
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        ;
        return 0;
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtQuantity, txtPrice;
        ImageView imageItem;
        EditText edtQuantity;
        Button btnDelete, btnChange;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_nameItem_cart);
            txtQuantity = itemView.findViewById(R.id.txt_quantityItem_cart);
            txtPrice = itemView.findViewById(R.id.txt_priceItem_cart);
            imageItem = itemView.findViewById(R.id.imgItem_cart);
            edtQuantity = itemView.findViewById(R.id.edt_quantityItem_cart);
            btnDelete = itemView.findViewById(R.id.btn_deleteItem_cart);
            btnChange = itemView.findViewById(R.id.btn_changeItem_cart);
        }
    }
}
