package com.example.grogrocery.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // Import Button
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.grogrocery.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, Object>> cartList;
    private OnCartItemInteractionListener listener;

    // Constructor
    public CartRecyclerAdapter(Context context, ArrayList<HashMap<String, Object>> cartList, OnCartItemInteractionListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerAdapter.ViewHolder holder, int position) {
        HashMap<String, Object> cartItem = cartList.get(position);
        int itemId = (int) cartItem.get("iid");
        String itemName = (String) cartItem.get("name");
        int price = (int) cartItem.get("price");
        int stockQty = (int) cartItem.get("stockQty"); // Available stock
        byte[] imageBytes = (byte[]) cartItem.get("avatar");
        int cartQty = (int) cartItem.get("quantity");

        holder.titleTxt.setText(itemName);
        holder.feeEachItem.setText("Rs. " + price);
        holder.totalEachItem.setText("Rs. " + (price * cartQty));

        // Set the item image
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.pic.setImageBitmap(bitmap);
        } else {
            holder.pic.setImageResource(R.drawable.ic_person); // Default image
        }

        // Check if the item is out of stock
        if (stockQty == 0) {
            holder.numberItemTxt.setText("OUTOFSTOCK");
            holder.numberItemTxt.setTextColor(Color.RED);
            holder.plusCartBtn.setEnabled(false);
            holder.plusCartBtn.setVisibility(View.INVISIBLE);
        } else {
            // If stock is available, show current cart quantity
            holder.numberItemTxt.setText(String.valueOf(cartQty));
            holder.numberItemTxt.setTextColor(Color.BLACK);

            // Update the visibility and enabled state of the '+' button
            if (cartQty < stockQty) {
                holder.plusCartBtn.setEnabled(true);
                holder.plusCartBtn.setVisibility(View.VISIBLE);
            } else {
                holder.plusCartBtn.setEnabled(false);
                holder.plusCartBtn.setVisibility(View.INVISIBLE);
            }

            // Handle "+" button click
            holder.plusCartBtn.setOnClickListener(v -> {
                if (cartQty < stockQty) {
                    listener.onIncreaseQuantity(itemId, cartQty + 1);
                }
            });

            // Handle "-" button click
            holder.minusCartBtn.setOnClickListener(v -> {
                if (cartQty > 1) {
                    listener.onDecreaseQuantity(itemId, cartQty - 1);
                } else if (cartQty == 1) {
                    // When quantity reaches 0, remove the item from the cart
                    listener.onRemoveItem(itemId, position);
                }

                // After decreasing quantity, check if the product is back in stock
                if (stockQty > 0) {
                    holder.plusCartBtn.setEnabled(true);
                    holder.plusCartBtn.setVisibility(View.VISIBLE);
                    holder.numberItemTxt.setTextColor(Color.BLACK);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, feeEachItem, totalEachItem, numberItemTxt;
        ImageView pic;
        Button plusCartBtn, minusCartBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            pic = itemView.findViewById(R.id.pic);
            plusCartBtn = itemView.findViewById(R.id.plusCartBtn);
            minusCartBtn = itemView.findViewById(R.id.minusCartBtn);
            numberItemTxt = itemView.findViewById(R.id.numberItemTxt);
        }
    }

    // Interface for cart item interactions
    public interface OnCartItemInteractionListener {
        void onIncreaseQuantity(int itemId, int newQuantity);
        void onDecreaseQuantity(int itemId, int newQuantity);
        void onRemoveItem(int itemId, int position); // New method for removing an item
        void onOutOfStock(int itemId);
    }

    // Method to update the cart list
    public void updateCartList(ArrayList<HashMap<String, Object>> newCartList) {
        this.cartList = newCartList;
        notifyDataSetChanged();
    }
}
