package com.example.ecommerceproject.fragment.manager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceproject.R;
import com.example.ecommerceproject.UpdateProductActivity;
import com.example.ecommerceproject.model.Product;

import java.util.ArrayList;

public class RecyclerviewManagerAdapter extends RecyclerView.Adapter<RecyclerviewManagerAdapter.ProductHolder> {

    Context context;
    ArrayList<Product> list = new ArrayList<>();
    ArrayList<String> listKeys = new ArrayList<>();
    public RecyclerviewManagerAdapter(Context context) {
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
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item, null);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
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
                Intent intent = new Intent(context, UpdateProductActivity.class);
                intent.putExtra("update_product", p);
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
            txtID = itemView.findViewById(R.id.txt_idItem);
            txtName = itemView.findViewById(R.id.txt_nameItem);
            txtQuantity = itemView.findViewById(R.id.txt_quantityItem);
            txtPrice = itemView.findViewById(R.id.txt_priceItem);
            imageItem = itemView.findViewById(R.id.imgItem);
        }
    }
}
