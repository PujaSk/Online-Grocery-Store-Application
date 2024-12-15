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
import androidx.core.util.Pair;
import android.widget.BaseAdapter;

import com.example.grogrocery.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, Object>> categoryList;
    LayoutInflater inflater;


    public CategoryAdapter(Context context, ArrayList<HashMap<String, Object>> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_category, parent, false);
        }

        TextView categoryID = convertView.findViewById(R.id.categoryID);
        TextView categoryName = convertView.findViewById(R.id.categoryName);
        ImageView categoryImage = convertView.findViewById(R.id.categoryImage);

        // Get the current category
        HashMap<String, Object> category = categoryList.get(position);

        // Debugging Logs
        Log.d("CategoryAdapter", "ID: " + category.get("id"));
        Log.d("CategoryAdapter", "Name: " + category.get("name"));

        // Set the Category ID and Name
        categoryID.setText((String) category.get("id"));
        categoryName.setText((String) category.get("name"));

        // Set the image
        byte[] imageBytes = (byte[]) category.get("avatar");
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        categoryImage.setImageBitmap(bitmap);

        return convertView;
    }
}
