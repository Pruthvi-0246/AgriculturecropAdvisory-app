package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.databinding.ActivityReportsBinding;
import java.util.ArrayList;
import java.util.List;

public class ReportsActivity extends AppCompatActivity {

    private ActivityReportsBinding binding;
    private List<CropHistory> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        historyList = new ArrayList<>();
        historyList.add(new CropHistory("Wheat", "2023", "₹ 50,000"));
        historyList.add(new CropHistory("Rice", "2022", "₹ 42,000"));

        binding.rvCropHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCropHistory.setAdapter(new HistoryAdapter(historyList));
    }

    static class CropHistory {
        String name, year, profit;
        CropHistory(String name, String year, String profit) {
            this.name = name; this.year = year; this.profit = profit;
        }
    }

    private static class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
        private final List<CropHistory> list;
        HistoryAdapter(List<CropHistory> list) { this.list = list; }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CropHistory item = list.get(position);
            holder.text1.setText(item.name + " (" + item.year + ")");
            holder.text2.setText("Profit: " + item.profit);
        }

        @Override
        public int getItemCount() { return list.size(); }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView text1, text2;
            ViewHolder(View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
                text2 = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}