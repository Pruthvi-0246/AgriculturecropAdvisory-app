package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.models.Transaction;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private List<Transaction> transactionList;
    private TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        transactionList = new ArrayList<>();

        setupUI();
        setupChart();
        setupTransactions();
    }

    private void setupUI() {
        if (mAuth.getCurrentUser() != null) {
            String email = mAuth.getCurrentUser().getEmail();
            if (email != null && email.contains("@")) {
                String name = email.split("@")[0];
                // Capitalize first letter
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                binding.tvWelcome.setText(name + "!");
                binding.tvProfileInitial.setText(name.substring(0, 1));
            } else {
                binding.tvWelcome.setText("Farmer!");
                binding.tvProfileInitial.setText("F");
            }
        }

        binding.btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        binding.btnCropRecommendation.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CropRecommendationActivity.class));
        });

        binding.btnMarketPrices.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MarketPricesActivity.class));
        });

        binding.fabAdd.setOnClickListener(v -> {
            showAddRecordDialog();
        });
    }

    private void showAddRecordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Record");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_record, null);
        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etAmount = view.findViewById(R.id.etAmount);
        Spinner spinnerType = view.findViewById(R.id.spinnerType);

        String[] types = {"Income", "Expense"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        builder.setView(view);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = etTitle.getText().toString().trim();
            String amount = etAmount.getText().toString().trim();
            boolean isIncome = spinnerType.getSelectedItemPosition() == 0;

            if (!title.isEmpty() && !amount.isEmpty()) {
                String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
                Transaction newTransaction = new Transaction(title, date, "₹" + amount, isIncome);
                transactionList.add(0, newTransaction);
                adapter.notifyItemInserted(0);
                binding.rvTransactions.scrollToPosition(0);
                Toast.makeText(this, "Record added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void setupChart() {
        LineChart chart = binding.priceChart;
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 1800));
        entries.add(new Entry(1, 1950));
        entries.add(new Entry(2, 1900));
        entries.add(new Entry(3, 2100));
        entries.add(new Entry(4, 2050));
        entries.add(new Entry(5, 2300));

        LineDataSet dataSet = new LineDataSet(entries, "Wheat Price Trend (per quintal)");
        dataSet.setColor(Color.parseColor("#4CAF50"));
        dataSet.setCircleColor(Color.parseColor("#4CAF50"));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(true);
        chart.getXAxis().setEnabled(false);
        chart.invalidate();
    }

    private void setupTransactions() {
        transactionList.add(new Transaction("Sold Wheat", "24 May 2024", "₹45,000", true));
        transactionList.add(new Transaction("Purchased Seeds", "20 May 2024", "₹5,200", false));
        transactionList.add(new Transaction("Sold Corn", "15 May 2024", "₹12,800", true));
        transactionList.add(new Transaction("Fertilizer", "10 May 2024", "₹2,400", false));

        adapter = new TransactionAdapter(transactionList);
        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTransactions.setAdapter(adapter);
    }
}
