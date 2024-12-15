package com.example.grogrocery.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.grogrocery.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, Object>> categoryList;
    private OnItemClickListener listener;

    // Define the interface for click events
    public interface OnItemClickListener {
        void onItemClick(String categoryId, String categoryName);
    }

    // Corrected Constructor
    public CategoryRecyclerAdapter(Context context, ArrayList<HashMap<String, Object>> categoryList, OnItemClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerAdapter.ViewHolder holder, int position) {
        HashMap<String, Object> category = categoryList.get(position);
        String categoryName = (String) category.get("name");
        String categoryId = (String) category.get("id");

        holder.categoryName.setText(categoryName);

        // Set the category image
        byte[] imageBytes = (byte[]) category.get("avatar");
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.categoryImage.setImageBitmap(bitmap);
        } else {
            holder.categoryImage.setImageResource(R.drawable.ic_person); // Replace with your default image
        }

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(categoryId, categoryName);
                Log.d("CategoryRecyclerAdapter", "Clicked on: " + categoryName + " (ID: " + categoryId + ")");
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryNameTextView);
            categoryImage = itemView.findViewById(R.id.categoryImageView);
        }
    }
}
