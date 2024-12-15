package com.example.grogrocery.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.grogrocery.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    private ArrayList<HashMap<String, Object>> orderItems;

    public BillAdapter(ArrayList<HashMap<String, Object>> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public BillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdapter.ViewHolder holder, int position) {
        HashMap<String, Object> order = orderItems.get(position);
        String itemName = (String) order.get("item_name");
        int quantity = (int) order.get("quantity");
        double price = (double) order.get("price");
        double totalPrice = price * quantity ;

        holder.itemNameTextView.setText(itemName);
        holder.orderQuantityTextView.setText("Quantity: " + quantity);
        holder.orderPriceTextView.setText("Price: Rs." + String.format("%.2f", price));
        holder.orderTotalPriceTextView.setText("Total: Rs." + String.format("%.2f", totalPrice));
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView, orderQuantityTextView, orderPriceTextView, orderTotalPriceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            orderQuantityTextView = itemView.findViewById(R.id.orderQuantityTextView);
            orderPriceTextView = itemView.findViewById(R.id.orderPriceTextView);
            orderTotalPriceTextView = itemView.findViewById(R.id.orderTotalPriceTextView);
        }
    }
}
