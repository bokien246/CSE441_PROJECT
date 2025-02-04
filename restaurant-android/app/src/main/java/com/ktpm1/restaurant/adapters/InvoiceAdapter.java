package com.ktpm1.restaurant.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.activities.ReviewOrderActivity;
import com.ktpm1.restaurant.dtos.responses.InvoiceResponse;
import com.ktpm1.restaurant.models.Order;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {

    private List<Order> invoiceList;
    private Context context;

    public InvoiceAdapter(Context context, List<Order> invoiceList) {
        this.context = context;
        this.invoiceList = invoiceList;
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invoice, parent, false);
        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        Order invoice = invoiceList.get(position);

        holder.tvInvoiceCode.setText("Mã hóa đơn: " + invoice.getId());
        holder.tvInvoiceDate.setText("Ngày: " + invoice.getCreatedAt());
        holder.tvTotalAmount.setText("Tổng tiền: " + invoice.getTotalPrice() + " VNĐ");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReviewOrderActivity.class);
            intent.putExtra("invoiceId", invoice.getId());  // Truyền mã hóa đơn
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    public static class InvoiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvInvoiceCode, tvInvoiceDate, tvTotalAmount;

        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInvoiceCode = itemView.findViewById(R.id.tvInvoiceCode);
            tvInvoiceDate = itemView.findViewById(R.id.tvInvoiceDate);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
        }
    }
}
