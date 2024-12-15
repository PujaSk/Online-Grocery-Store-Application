package com.example.grogrocery.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, Object>> itemList;
    private OnAddToCartClickListener listener;
    private DBHelper dbHelper;
    private int userId; // Assume userId is obtained via some method

    // Constructor
    public ItemsRecyclerAdapter(Context context, ArrayList<HashMap<String, Object>> itemList, OnAddToCartClickListener listener, int userId) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
        this.dbHelper = new DBHelper(context);
        this.userId = userId;
    }

    @NonNull
    @Override
    public ItemsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsRecyclerAdapter.ViewHolder holder, int position) {
        HashMap<String, Object> item = itemList.get(position);
        int itemId = (int) item.get("iid");
        String itemName = (String) item.get("name");
        int price = (int) item.get("price");
        int qty = (int) item.get("qty");
        String description = (String) item.get("description").toString(); // Removed

        holder.itemName.setText(itemName);
        holder.itemPrice.setText("Price: ₹" + price);
        holder.itemQty.setText("Quantity: " + qty);
        holder.itemDescription.setText("Description: " + description); // Removed

        // Handle "Add to Cart" button state
        if (qty <= 0) {
            holder.addToCartButton.setEnabled(false);
            holder.addToCartButton.setText("OUTOFSTOCK");
            holder.addToCartButton.setBackgroundColor(Color.RED); // Optional: Change button color to indicate out of stock
        } else {
            holder.addToCartButton.setEnabled(true);
            holder.addToCartButton.setText("Add to Cart");
            holder.addToCartButton.setBackgroundResource(R.drawable.button_background_roundcorner); // Restore original background
        }

        // Handle "Add to Cart" button click
        holder.addToCartButton.setOnClickListener(v -> {
            if (qty <= 0) {
                Toast.makeText(context, "Item is out of stock", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.addToCart(userId, itemId, 1); // Adding 1 quantity
            if (success) {
                Toast.makeText(context, itemName + " Added to Cart", Toast.LENGTH_SHORT).show();
                // Optionally, notify listener to refresh cart
                if (listener != null) {
                    listener.onAddToCart(itemId, itemName, qty);
                }
                // Optionally, update the quantity locally
                item.put("qty", qty - 1);
                notifyItemChanged(position);
            } else {
                Toast.makeText(context, "Failed to add " + itemName + " to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        // Set item image
        byte[] imageBytes = (byte[]) item.get("avatar");
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.itemImage.setImageBitmap(bitmap);
        } else {
            holder.itemImage.setImageResource(R.drawable.ic_person); // Default image
        }

        // Optionally, set click listeners for items
        holder.itemView.setOnClickListener(v -> {
            // Handle item click if needed
            // For example, navigate to item details
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button addToCartButton;
        TextView itemName, itemPrice, itemQty,itemDescription;
        ImageView itemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemNameTextView);
            itemPrice = itemView.findViewById(R.id.itemPriceTextView);
            itemQty = itemView.findViewById(R.id.itemQtyTextView);
            itemImage = itemView.findViewById(R.id.itemImageView);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }

    // Interface for "Add to Cart" button clicks
    public interface OnAddToCartClickListener {
        void onAddToCart(int itemId, String itemName, int qty);
    }

    // Method to update the item list
    public void updateItemList(ArrayList<HashMap<String, Object>> newItemList) {
        this.itemList = newItemList;
        notifyDataSetChanged();
    }
}
