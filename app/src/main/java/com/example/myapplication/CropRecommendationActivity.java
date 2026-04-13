package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.databinding.ActivityCropRecommendationBinding;

public class CropRecommendationActivity extends AppCompatActivity {

    private ActivityCropRecommendationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCropRecommendationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        setupDropdowns();

        binding.btnRecommend.setOnClickListener(v -> recommendCrop());
    }

    private void setupDropdowns() {
        String[] soils = {"Alluvial Soil", "Black Soil", "Red Soil", "Laterite Soil", "Desert Soil"};
        String[] seasons = {"Rabi (Winter)", "Kharif (Monsoon)", "Zaid (Summer)"};
        String[] water = {"High (Irrigation)", "Medium", "Low (Rainfed)"};

        binding.autoCompleteSoil.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, soils));
        binding.autoCompleteSeason.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, seasons));
        binding.autoCompleteWater.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, water));
    }

    private void recommendCrop() {
        String soil = binding.autoCompleteSoil.getText().toString();
        String season = binding.autoCompleteSeason.getText().toString();
        String water = binding.autoCompleteWater.getText().toString();

        if (soil.isEmpty() || season.isEmpty() || water.isEmpty()) {
            return;
        }

        binding.cvResult.setVisibility(View.VISIBLE);
        
        // Simple Logic for demonstration
        if (season.contains("Rabi")) {
            binding.tvRecommendedCrop.setText("Wheat (गेहूँ)");
            binding.tvReason.setText("Rabi season with " + soil + " is perfect for Wheat. It requires moderate water during the growth phase.");
        } else if (season.contains("Kharif")) {
            if (water.contains("High")) {
                binding.tvRecommendedCrop.setText("Rice (चावल)");
                binding.tvReason.setText("Kharif season with high water availability is ideal for Rice production in " + soil + ".");
            } else {
                binding.tvRecommendedCrop.setText("Maize (मक्का)");
                binding.tvReason.setText("Maize is a hardy crop that grows well in " + season + " with " + water + " water.");
            }
        } else {
            binding.tvRecommendedCrop.setText("Moong Dal (मूंग)");
            binding.tvReason.setText("Short-duration pulses like Moong are best for the Summer (Zaid) season.");
        }
    }
}
