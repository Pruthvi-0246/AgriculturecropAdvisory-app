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
import com.example.myapplication.databinding.ActivityMarketPricesBinding;
import com.example.myapplication.models.CropPrice;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MarketPricesActivity extends AppCompatActivity {

    private ActivityMarketPricesBinding binding;
    private FirebaseFirestore db;
    private MarketPriceAdapter adapter;
    private List<CropPrice> priceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMarketPricesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        priceList = new ArrayList<>();
        adapter = new MarketPriceAdapter(priceList);

        binding.rvMarketPrices.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMarketPrices.setAdapter(adapter);

        loadMarketPrices();
    }

    private void loadMarketPrices() {
        db.collection("market_prices").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    priceList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        CropPrice price = doc.toObject(CropPrice.class);
                        priceList.add(price);
                    }
                    if (priceList.isEmpty()) {
                        // Add dummy data if firestore is empty for demo
                        priceList.add(new CropPrice("Wheat", "₹ 2125/quintal"));
                        priceList.add(new CropPrice("Rice (Paddy)", "₹ 2040/quintal"));
                        priceList.add(new CropPrice("Cotton", "₹ 6080/quintal"));
                        priceList.add(new CropPrice("Mustard", "₹ 5450/quintal"));
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private static class MarketPriceAdapter extends RecyclerView.Adapter<MarketPriceAdapter.ViewHolder> {
        private final List<CropPrice> prices;

        public MarketPriceAdapter(List<CropPrice> prices) {
            this.prices = prices;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market_price, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CropPrice price = prices.get(position);
            holder.tvName.setText(price.name);
            holder.tvPrice.setText(price.price);
        }

        @Override
        public int getItemCount() {
            return prices.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvPrice;
            ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvCropName);
                tvPrice = itemView.findViewById(R.id.tvCropPrice);
            }
        }
    }
}