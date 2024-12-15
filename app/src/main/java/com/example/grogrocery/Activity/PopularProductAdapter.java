package com.example.grogrocery.Activity;// PopularProductAdapter.java

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

import com.example.grogrocery.Activity.Item;
import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

import java.util.ArrayList;

public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Item> itemList;
    private DBHelper dbHelper;
    private int userId; // User ID

    // Constructor
    public PopularProductAdapter(Context context, ArrayList<Item> itemList, int userId) {
        this.context = context;
        this.itemList = itemList;
        this.dbHelper = new DBHelper(context);
        this.userId = userId;
    }

    @NonNull
    @Override
    public PopularProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularProductAdapter.ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.productName.setText(item.getName());
        holder.productPrice.setText("₹" + item.getPrice());
        holder.productQty.setText("Stock: " + item.getQty());

        // Set image (if available)
        if (item.getAvatar() != null && item.getAvatar().length > 0) {
            Bitmap bmp = BitmapFactory.decodeByteArray(item.getAvatar(), 0, item.getAvatar().length);
            holder.productImage.setImageBitmap(bmp);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_person); // Default image
        }

        // Handle "Add to Cart" button state
        if (item.getQty() <= 0) {
            holder.addToCartBtn.setEnabled(false);
            holder.addToCartBtn.setText("OUTOFSTOCK");
            holder.addToCartBtn.setBackgroundColor(Color.RED); // Optional: Change button color
        } else {
            holder.addToCartBtn.setEnabled(true);
            holder.addToCartBtn.setText("Add to Cart");
            holder.addToCartBtn.setBackgroundResource(R.drawable.button_background_roundcorner); // Restore original background
        }

        // Handle "Add to Cart" button click
        holder.addToCartBtn.setOnClickListener(v -> {
            if (item.getQty() <= 0) {
                Toast.makeText(context, "Item is out of stock", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.addToCart(userId, item.getId(), 1); // Adding 1 quantity
            if (success) {
                Toast.makeText(context, item.getName() + " Added to Cart", Toast.LENGTH_SHORT).show();
                // Update the quantity locally
                item.setQty(item.getQty() - 1);
                notifyItemChanged(position);
            } else {
                Toast.makeText(context, "Failed to add " + item.getName() + " to Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productQty;
        Button addToCartBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQty = itemView.findViewById(R.id.productQty);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }

    // Method to update the item list
    public void updateItemList(ArrayList<Item> newItemList) {
        this.itemList = newItemList;
        notifyDataSetChanged();
    }
}
