package com.usersecure.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.usersecure.app.databinding.FragmentHomeBinding;

/**
 * HomeFragment — displays tabs for "Username Generator" and "Password Generator".
 * Each tab shows recent generated items. Also has a FAB for quick generation.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager();
    }

    private void setupViewPager() {
        HomePagerAdapter adapter = new HomePagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        // Connect tabs with ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Usernames");
            } else {
                tab.setText("Passwords");
            }
        }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Inner PagerAdapter — creates HomeTabFragment for each tab.
     */
    private static class HomePagerAdapter extends FragmentStateAdapter {
        HomePagerAdapter(Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return HomeTabFragment.newInstance(position == 0 ? "usernames" : "passwords");
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
