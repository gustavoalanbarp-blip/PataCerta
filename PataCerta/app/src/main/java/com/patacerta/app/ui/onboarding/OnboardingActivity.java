package com.patacerta.app.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayoutMediator;
import com.patacerta.app.data.repository.AuthRepository;
import com.patacerta.app.databinding.ActivityOnboardingBinding;
import com.patacerta.app.ui.auth.LoginActivity;
import com.patacerta.app.ui.home.HomeActivity;

import java.util.Arrays;
import java.util.List;

/**
 * Primeira tela do app (RF: onboarding). Se já existir uma sessão salva
 * (AuthRepository), pula direto para a Home — evita fricção de UX para
 * quem já é usuário do PataCerta.
 */
public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthRepository authRepository = new AuthRepository(this);
        if (authRepository.isLoggedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<OnboardingPagerAdapter.Page> pages = Arrays.asList(
                new OnboardingPagerAdapter.Page(
                        getString(com.patacerta.app.R.string.onboarding_title_1),
                        getString(com.patacerta.app.R.string.onboarding_desc_1)),
                new OnboardingPagerAdapter.Page(
                        getString(com.patacerta.app.R.string.onboarding_title_2),
                        getString(com.patacerta.app.R.string.onboarding_desc_2)),
                new OnboardingPagerAdapter.Page(
                        getString(com.patacerta.app.R.string.onboarding_title_3),
                        getString(com.patacerta.app.R.string.onboarding_desc_3))
        );

        binding.viewPager.setAdapter(new OnboardingPagerAdapter(pages));
        new TabLayoutMediator(binding.tabDots, binding.viewPager, (tab, position) -> {}).attach();

        binding.btnStart.setOnClickListener(v -> goToLogin());
        binding.btnAlreadyHaveAccount.setOnClickListener(v -> goToLogin());
    }

    private void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
