package com.example.grogrocery.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.grogrocery.R;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, Object>> orderList;

    // Constructor
    public OrderRecyclerAdapter(Context context, ArrayList<HashMap<String, Object>> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_order, parent, false);
        return new ViewHolder(view);
    }

    // OrderRecyclerAdapter.java
    @Override
    public void onBindViewHolder(@NonNull OrderRecyclerAdapter.ViewHolder holder, int position) {
        HashMap<String, Object> order = orderList.get(position);

        // Safely handle 'order_id'
        Object orderIdObj = order.get("order_id");
        String orderId = (orderIdObj != null) ? orderIdObj.toString() : "N/A";
        holder.orderIdTextView.setText("Order ID: #" + orderId);

        // Safely handle 'bill_id'
        Object billIdObj = order.get("bill_id");
        String billId = (billIdObj != null) ? billIdObj.toString() : "N/A";
        holder.billIdTextView.setText("Bill ID: #" + billId);

        // Safely handle 'order_date'
        Object orderDateObj = order.get("order_date");
        String orderDate = (orderDateObj != null) ? orderDateObj.toString() : "N/A";
        holder.orderDateTextView.setText("Date: " + orderDate);

        // Safely handle 'item_name'
        Object itemNameObj = order.get("item_name");
        String itemName = (itemNameObj != null) ? itemNameObj.toString() : "N/A";
        holder.itemNameTextView.setText("Item Name: " + itemName);

        // Safely handle 'quantity'
        Object quantityObj = order.get("quantity");
        String quantity = (quantityObj != null) ? quantityObj.toString() : "0";
        holder.orderQuantityTextView.setText("Quantity: " + quantity);

        // Safely handle 'price'
        Object priceObj = order.get("price");
        double price = 0.0;
        if (priceObj instanceof Double) {
            price = (Double) priceObj;
        } else if (priceObj instanceof Integer) {
            price = ((Integer) priceObj).doubleValue();
        }
        holder.orderPriceTextView.setText("Price: Rs." + String.format("%.2f", price));

        // Safely handle 'total_price'
        Object totalPriceObj = order.get("total_price");
        double totalPrice = 0.0;
        if (totalPriceObj instanceof Double) {
            totalPrice = (Double) totalPriceObj;
        } else if (totalPriceObj instanceof Integer) {
            totalPrice = ((Integer) totalPriceObj).doubleValue();
        }
        holder.orderTotalPriceTextView.setText("Total: Rs." + String.format("%.2f", totalPrice));
    }




    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView, orderDateTextView, itemNameTextView, orderQuantityTextView, orderPriceTextView, orderTotalPriceTextView, billIdTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            orderQuantityTextView = itemView.findViewById(R.id.orderQuantityTextView);
            orderPriceTextView = itemView.findViewById(R.id.orderPriceTextView);
            orderTotalPriceTextView = itemView.findViewById(R.id.orderTotalPriceTextView);
            billIdTextView = itemView.findViewById(R.id.billIdTextView);

        }
    }

    // Method to update the order list
    public void updateOrderList(ArrayList<HashMap<String, Object>> newOrderList) {
        this.orderList = newOrderList;
        notifyDataSetChanged();
    }
}
