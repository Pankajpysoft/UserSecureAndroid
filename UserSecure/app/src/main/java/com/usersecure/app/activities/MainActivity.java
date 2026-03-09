package com.usersecure.app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.usersecure.app.R;
import com.usersecure.app.databinding.ActivityMainBinding;
import com.usersecure.app.fragments.AddItemFragment;
import com.usersecure.app.fragments.FavoritesFragment;
import com.usersecure.app.fragments.HomeFragment;
import com.usersecure.app.fragments.PasswordFragment;
import com.usersecure.app.fragments.UsernameFragment;
import com.usersecure.app.utils.AdMobHelper;

/**
 * MainActivity — Host activity for all fragments.
 * Contains: BottomNavigationView + AdMob banner ad at the bottom.
 * Manages fragment switching without recreating them (using add/hide/show pattern).
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    // Fragment instances (kept alive to avoid recreation)
    private HomeFragment homeFragment;
    private UsernameFragment usernameFragment;
    private PasswordFragment passwordFragment;
    private FavoritesFragment favoritesFragment;
    private AddItemFragment addItemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize AdMob
        AdMobHelper.initialize(this);
        AdMobHelper.loadBanner(binding.adView);

        // Setup bottom navigation
        setupNavigation();

        // Show Home on startup
        if (savedInstanceState == null) {
            showFragment(getHomeFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    private void setupNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                showFragment(getHomeFragment());
            } else if (itemId == R.id.nav_username) {
                showFragment(getUsernameFragment());
            } else if (itemId == R.id.nav_password) {
                showFragment(getPasswordFragment());
            } else if (itemId == R.id.nav_favorites) {
                showFragment(getFavoritesFragment());
            } else if (itemId == R.id.nav_add) {
                showFragment(getAddItemFragment());
            }
            return true;
        });
    }

    /**
     * Show the given fragment, hiding all others.
     * Uses add/show/hide so Fragments are not recreated on each switch.
     */
    private void showFragment(Fragment target) {
        String tag = target.getClass().getSimpleName();
        Fragment existing = getSupportFragmentManager().findFragmentByTag(tag);

        androidx.fragment.app.FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        // Hide all currently added fragments
        for (Fragment f : getSupportFragmentManager().getFragments()) {
            transaction.hide(f);
        }

        // Add (first time) or show (subsequent times)
        if (existing == null) {
            transaction.add(R.id.fragmentContainer, target, tag);
        } else {
            transaction.show(existing);
        }

        transaction.commit();
    }

    // --- Lazy fragment getters ---
    private Fragment getHomeFragment() {
        if (homeFragment == null) homeFragment = new HomeFragment();
        return homeFragment;
    }
    private Fragment getUsernameFragment() {
        if (usernameFragment == null) usernameFragment = new UsernameFragment();
        return usernameFragment;
    }
    private Fragment getPasswordFragment() {
        if (passwordFragment == null) passwordFragment = new PasswordFragment();
        return passwordFragment;
    }
    private Fragment getFavoritesFragment() {
        if (favoritesFragment == null) favoritesFragment = new FavoritesFragment();
        return favoritesFragment;
    }
    private Fragment getAddItemFragment() {
        if (addItemFragment == null) addItemFragment = new AddItemFragment();
        return addItemFragment;
    }

    /**
     * Programmatically switch to a tab from a fragment.
     */
    public void switchToTab(int navItemId) {
        binding.bottomNavigationView.setSelectedItemId(navItemId);
    }
}
