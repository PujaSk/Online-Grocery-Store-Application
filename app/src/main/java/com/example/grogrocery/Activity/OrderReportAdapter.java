package com.example.grogrocery.Activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.grogrocery.R;

import java.util.List;

public class OrderReportAdapter extends BaseAdapter {
    private Context context;
    private List<OrderReport> orderReports;
    private static final int NUM_FIELDS = 6; // order_id, user_name, item_name, quantity, total_amount, order_date

    public OrderReportAdapter(Context context, List<OrderReport> orderReports) {
        this.context = context;
        this.orderReports = orderReports;
    }

    @Override
    public int getCount() {
        return orderReports.size() * NUM_FIELDS; // Total cells
    }

    @Override
    public Object getItem(int position) {
        return orderReports.get(position / NUM_FIELDS); // Return the corresponding OrderReport
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Create a simple TextView for each cell
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView cell;

        if (convertView == null) {
            cell = new TextView(context);
            cell.setLayoutParams(new GridView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            cell.setPadding(8, 8, 8, 8);
            cell.setTextSize(14);
            cell.setBackgroundResource(R.drawable.grid_item_background); // Define a drawable for cell background
            cell.setGravity(android.view.Gravity.CENTER);
        } else {
            cell = (TextView) convertView;
        }

        // Determine which OrderReport and which field to display
        int rowIndex = position / NUM_FIELDS;
        int columnIndex = position % NUM_FIELDS;

        OrderReport report = orderReports.get(rowIndex);

        switch (columnIndex) {
            case 0: // Order ID
                cell.setText(String.valueOf(report.getOrderId()));
                break;
            case 1: // User Name
                cell.setText(report.getUserName());
                break;
            case 2: // Item Name
                cell.setText(report.getItemName());
                break;
            case 3: // Quantity
                cell.setText(String.valueOf(report.getQuantity()));
                break;
            case 4: // Total Amount
                cell.setText(String.format("Rs. %.2f", report.getTotalAmount()));
                break;
            case 5: // Order Date
                cell.setText(report.getOrderDate());
                break;
            default:
                cell.setText("");
        }

        return cell;
    }
}
