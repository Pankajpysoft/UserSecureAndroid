package com.usersecure.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.usersecure.app.databinding.FragmentFavoritesBinding;

/**
 * FavoritesFragment — Shows two tabs: Usernames and Passwords.
 * Each tab displays saved favorites from the Room database
 * with Copy and Delete actions.
 */
public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager();
    }

    private void setupViewPager() {
        FavoritesPagerAdapter adapter = new FavoritesPagerAdapter(this);
        binding.viewPagerFav.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayoutFav, binding.viewPagerFav, (tab, position) -> {
            tab.setText(position == 0 ? "Usernames" : "Passwords");
        }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /** Inner adapter for the favorites tabs. */
    private static class FavoritesPagerAdapter extends FragmentStateAdapter {
        FavoritesPagerAdapter(Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return FavoritesTabFragment.newInstance(position == 0 ? "usernames" : "passwords");
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
