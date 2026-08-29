package com.patacerta.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.patacerta.app.data.repository.AuthRepository;
import com.patacerta.app.databinding.ActivityRegisterBinding;
import com.patacerta.app.ui.home.HomeActivity;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = new AuthRepository(this);

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnRegister.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String name = binding.inputName.getText() != null ? binding.inputName.getText().toString().trim() : "";
        String email = binding.inputEmail.getText() != null ? binding.inputEmail.getText().toString().trim() : "";
        String password = binding.inputPassword.getText() != null ? binding.inputPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) {
            binding.inputName.setError("Informe seu nome");
            return;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError(getString(com.patacerta.app.R.string.error_invalid_email));
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            binding.inputPassword.setError(getString(com.patacerta.app.R.string.error_invalid_password));
            return;
        }

        setLoading(true);
        authRepository.register(email, password, name, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(String token) {
                runOnUiThread(() -> {
                    setLoading(false);
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    Toast.makeText(RegisterActivity.this,
                            "Não foi possível cadastrar: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void setLoading(boolean loading) {
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnRegister.setEnabled(!loading);
    }
}
