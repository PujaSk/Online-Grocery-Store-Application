package com.example.grogrocery.Activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

public class ItemReportAdapter extends BaseAdapter {
    private Context context;
    private List<ItemReport> itemReports;

    public ItemReportAdapter(Context context, List<ItemReport> itemReports) {
        this.context = context;
        this.itemReports = itemReports;
    }

    @Override
    public int getCount() {
        return itemReports.size() * 4; // 4 fields per item (iid, name, price, qty)
    }

    @Override
    public Object getItem(int position) {
        return itemReports.get(position / 4); // Return the appropriate ItemReport
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

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
        } else {
            cell = (TextView) convertView;
        }

        // Get the appropriate item from the itemReports list
        int rowIndex = position / 4; // Determine which row (item) this position is part of
        int columnIndex = position % 4; // Determine the column (field) in that row

        ItemReport report = itemReports.get(rowIndex);

        // Display corresponding field based on the column
        switch (columnIndex) {
            case 0: // Item ID
                cell.setText(String.valueOf(report.getIid()));
                break;
            case 1: // Name
                cell.setText(report.getName());
                break;
            case 2: // Price
                cell.setText(String.format("Rs. %.2f", report.getPrice()));
                break;
            case 3: // Quantity
                cell.setText(String.valueOf(report.getQty()));
                break;
        }

        return cell;
    }
}
