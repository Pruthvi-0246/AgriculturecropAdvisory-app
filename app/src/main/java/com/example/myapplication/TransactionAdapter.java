package com.example.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.models.Transaction;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactions;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.tvTitle.setText(transaction.getTitle());
        holder.tvDate.setText(transaction.getDate());
        
        if (transaction.isIncome()) {
            holder.tvAmount.setText("+ " + transaction.getAmount());
            holder.tvAmount.setTextColor(Color.parseColor("#2E7D32")); // Professional Green
            holder.ivIcon.setImageResource(android.R.drawable.ic_input_add);
            holder.ivIcon.setColorFilter(Color.parseColor("#2E7D32"));
        } else {
            holder.tvAmount.setText("- " + transaction.getAmount());
            holder.tvAmount.setTextColor(Color.parseColor("#E53935")); // Professional Red
            holder.ivIcon.setImageResource(android.R.drawable.ic_delete);
            holder.ivIcon.setColorFilter(Color.parseColor("#E53935"));
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvAmount;
        ImageView ivIcon;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTransactionTitle);
            tvDate = itemView.findViewById(R.id.tvTransactionDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            ivIcon = itemView.findViewById(R.id.ivTransactionIcon);
        }
    }
}
