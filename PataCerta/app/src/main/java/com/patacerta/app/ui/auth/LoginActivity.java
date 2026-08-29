package com.patacerta.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.patacerta.app.data.repository.AuthRepository;
import com.patacerta.app.databinding.ActivityLoginBinding;
import com.patacerta.app.ui.home.HomeActivity;

/**
 * Tela de login (RF01). Autentica contra a API pública reqres.in via
 * AuthRepository — ver relatório técnico, seção "Integração com APIs".
 */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = new AuthRepository(this);

        // Pré-preenche com a credencial de teste pública do reqres.in para
        // facilitar a avaliação/demonstração do app.
        binding.inputEmail.setText("eve.holt@reqres.in");
        binding.inputPassword.setText("cityslicka");

        binding.btnLogin.setOnClickListener(v -> attemptLogin());
        binding.btnGoToRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void attemptLogin() {
        String email = binding.inputEmail.getText() != null ? binding.inputEmail.getText().toString().trim() : "";
        String password = binding.inputPassword.getText() != null ? binding.inputPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError(getString(com.patacerta.app.R.string.error_invalid_email));
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            binding.inputPassword.setError(getString(com.patacerta.app.R.string.error_invalid_password));
            return;
        }

        setLoading(true);
        String displayName = email.contains("@") ? capitalize(email.substring(0, email.indexOf('@'))) : "Tutor(a)";

        authRepository.login(email, password, displayName, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(String token) {
                runOnUiThread(() -> {
                    setLoading(false);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    Toast.makeText(LoginActivity.this,
                            getString(com.patacerta.app.R.string.error_login_failed) + " (" + message + ")",
                            Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void setLoading(boolean loading) {
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!loading);
    }

    private static String capitalize(String s) {
        if (s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
