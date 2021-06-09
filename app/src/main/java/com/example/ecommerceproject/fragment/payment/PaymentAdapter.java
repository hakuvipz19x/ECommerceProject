package com.example.ecommerceproject.fragment.payment;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.DataShareRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceproject.R;
import com.example.ecommerceproject.fragment.cart.CartAdapter;
import com.example.ecommerceproject.model.Cart;
import com.example.ecommerceproject.model.CartPayment;
import com.example.ecommerceproject.model.Product;
import com.example.ecommerceproject.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.CartPaymentHolder> {
    Context context;
    String userKey = "";
    ArrayList<String> listUserPaymentKeys = new ArrayList<>();
//    ArrayList<String> listCartPaymentKeys;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference referencePayment = database.getReference().child("Payments");
    DatabaseReference referenceUser = database.getReference().child("Users");
    DatabaseReference referenceProduct = database.getReference().child("Products");


    public PaymentAdapter(Context context) {
        this.context = context;
    }

    public void setListUserPaymentKeys(ArrayList<String> listUserPaymentKeys) {
        this.listUserPaymentKeys = listUserPaymentKeys;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    @NonNull
    @Override
    public CartPaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_payment, null);
        return new CartPaymentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartPaymentHolder holder, int position) {
        //Hien thi name customer, location, status, list product, total price
        //Hien thi name customer;
        referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    User user = item.getValue(User.class);
                    if (user.getKey().equals(userKey)) {
                        holder.txtNameCustomer.setText("Customer: " + user.getName());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Hien thi cac loai hang da mua
//        for (int i = 0; i < listUserPaymentKeys.size(); i++) {
//            System.out.println(listCartPaymentKeys.get(i));
//        }
        String pm = "null";
        for (String paymentKey : listUserPaymentKeys) {
//            System.out.println("Payment key: " + paymentKey);
//            System.out.println("Index: " + listUserPaymentKeys.indexOf(paymentKey));
//            System.out.println("Size cua list: " + listUserPaymentKeys.size());
            if (listUserPaymentKeys.indexOf(paymentKey) == position) {
                pm = paymentKey;
//                break;
            }
        }
//        System.out.println(pm);
//        System.out.println(listUserPaymentKeys);
//        System.out.println("Payment - " + pm);
        String finalPm = pm;
        referencePayment.child("Payment - " + pm)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        listCartPaymentKeys = new ArrayList<>();
                        holder.txtProduct.setText("");
                        holder.txtProduct.setText("Bought:\n");
                        for (DataSnapshot item : snapshot.getChildren()) {
                            String cartPayment = item.getKey();
//                            System.out.println("Cart payment: " + cartPayment);
                            String[] getCartPaymentKey = cartPayment.split("- ");
                            String cartPaymentKey = getCartPaymentKey[1];
//                            listCartPaymentKeys.add(cartPaymentKey);
//                            System.out.println("List cart payment key: ");
//                            System.out.println(listCartPaymentKeys);

                            String paymentKey = ("Payment - " + finalPm);
                            referencePayment.child(paymentKey)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshotCart) {
                                            holder.txtTotal.setText("0");
                                            for (DataSnapshot itemCart : snapshotCart.getChildren()) {
                                                CartPayment cartPayment1 = itemCart.getValue(CartPayment.class);
//                                                System.out.println(cartPayment1);
                                                int quantity = cartPayment1.getQuantity();
                                                holder.txtStatus.setText("Status: " + cartPayment1.getStatus());
                                                holder.txtLocation.setText("Location: " + cartPayment1.getLocation());
                                                holder.txtDate.setText("Date: " + cartPayment1.getDate());

                                                String productKey = cartPayment1.getProductKey();
                                                referenceProduct.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshotProduct) {
                                                        for (DataSnapshot itemProduct : snapshotProduct.getChildren()) {
                                                            Product product = itemProduct.getValue(Product.class);
                                                            if (product.getKey().equals(productKey)) {
                                                                String buy = ("Name: " + product.getName()
                                                                        + "; Price: " + product.getPrice()
                                                                        + "; Quantity: " + quantity + "\n");

                                                                //Khong chua String buy thi se add (Tao ra dieu kien nay vi bi lap vong lap
                                                                if (!holder.txtProduct.getText().toString().contains(buy)) {
                                                                    holder.txtTotal.setText(
                                                                            Double.valueOf(holder.txtTotal.getText().toString()) + (product.getPrice() * quantity) + ""
                                                                    );
//                                                                        System.out.println(buy[0]);
                                                                    holder.txtProduct.setText(
                                                                            holder.txtProduct.getText()
                                                                                    + buy);
                                                                }

                                                                break;
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//            System.out.println(listCartPayment);

    }


    @Override
    public int getItemCount() {
        if (listUserPaymentKeys != null) {
            return listUserPaymentKeys.size();
        }
        return 0;
    }

    public class CartPaymentHolder extends RecyclerView.ViewHolder {

        TextView txtNameCustomer, txtProduct, txtLocation, txtTotal, txtStatus, txtDate;

        public CartPaymentHolder(@NonNull View itemView) {
            super(itemView);
            txtNameCustomer = itemView.findViewById(R.id.txt_nameCustomer_payment);
            txtProduct = itemView.findViewById(R.id.txt_product_payment);
            txtLocation = itemView.findViewById(R.id.txt_location_payment);
            txtTotal = itemView.findViewById(R.id.txt_totalPrice_payment);
            txtStatus = itemView.findViewById(R.id.txt_status_payment);
            txtDate = itemView.findViewById(R.id.txt_date_payment);
        }
    }
}
