package com.example.grogrocery.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.grogrocery.R;
import java.util.List;

public class BillReportAdapter extends BaseAdapter {
    private Context context;
    private List<Bill> billList;

    public BillReportAdapter(Context context, List<Bill> billList) {
        this.context = context;
        this.billList = billList;
    }

    @Override
    public int getCount() {
        return billList.size();
    }

    @Override
    public Object getItem(int position) {
        return billList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return billList.get(position).getBillId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the grid_item_bill layout
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_bill, parent, false);
        }

        // Get current Bill object
        Bill bill = billList.get(position);

        // Bind data to TextViews
        TextView textBillId = convertView.findViewById(R.id.textBillId);
        TextView textUserId = convertView.findViewById(R.id.textUserId);
        TextView textTotalAmount = convertView.findViewById(R.id.textTotalAmount);
        TextView textBillDate = convertView.findViewById(R.id.textBillDate);

        textBillId.setText(String.valueOf(bill.getBillId()));
        textUserId.setText(String.valueOf(bill.getUserId()));
        textTotalAmount.setText(String.format("Rs. %.2f", bill.getTotalAmount()));
        textBillDate.setText(bill.getBillDate());

        return convertView;
    }
}
