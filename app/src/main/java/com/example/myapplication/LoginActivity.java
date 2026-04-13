package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.authState.observe(this, state -> {
            if (state instanceof AuthViewModel.AuthState.Loading) {
                showLoading(true);
            } else if (state instanceof AuthViewModel.AuthState.Success) {
                showLoading(false);
                Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else if (state instanceof AuthViewModel.AuthState.Message) {
                showLoading(false);
                Toast.makeText(this, ((AuthViewModel.AuthState.Message) state).msg, Toast.LENGTH_LONG).show();
            } else if (state instanceof AuthViewModel.AuthState.Error) {
                showLoading(false);
                Toast.makeText(this, ((AuthViewModel.AuthState.Error) state).message, Toast.LENGTH_LONG).show();
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                binding.etPassword.setError("Password is required");
                return;
            }

            authViewModel.login(email, password);
        });

        binding.tvForgotPassword.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            authViewModel.resetPassword(email);
        });

        binding.tvSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }

    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.loginForm.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
}
