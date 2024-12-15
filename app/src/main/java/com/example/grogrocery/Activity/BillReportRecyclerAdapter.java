package com.example.grogrocery.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grogrocery.R;

import java.util.List;

public class BillReportRecyclerAdapter extends RecyclerView.Adapter<BillReportRecyclerAdapter.BillViewHolder> {
    private Context context;
    private List<Bill> billList;

    public BillReportRecyclerAdapter(Context context, List<Bill> billList) {
        this.context = context;
        this.billList = billList;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = billList.get(position);
        holder.textBillId.setText(String.valueOf(bill.getBillId()));
        holder.textUserId.setText(String.valueOf(bill.getUserId()));
        holder.textTotalAmount.setText(String.format("Rs. %.2f", bill.getTotalAmount()));
        holder.textBillDate.setText(bill.getBillDate());
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView textBillId, textUserId, textTotalAmount, textBillDate;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            textBillId = itemView.findViewById(R.id.textBillId);
            textUserId = itemView.findViewById(R.id.textUserId);
            textTotalAmount = itemView.findViewById(R.id.textTotalAmount);
            textBillDate = itemView.findViewById(R.id.textBillDate);
        }
    }
}
