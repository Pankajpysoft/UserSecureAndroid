package com.usersecure.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.usersecure.app.R;
import com.usersecure.app.databinding.ActivitySplashBinding;

/**
 * SplashActivity — App entry point.
 * Displays the logo and app name with a fade-in animation,
 * then navigates to MainActivity after a 2.5 second delay.
 */
public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private static final long SPLASH_DELAY_MS = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Animate logo: fade in + scale
        binding.ivLogo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setStartDelay(200)
                .setDuration(600)
                .start();

        // Animate app name after logo
        binding.tvAppName.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(500)
                .setDuration(500)
                .start();

        // Animate tagline
        binding.tvTagline.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(700)
                .setDuration(500)
                .start();

        // Navigate to MainActivity after delay
        binding.getRoot().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, SPLASH_DELAY_MS);
    }
}
