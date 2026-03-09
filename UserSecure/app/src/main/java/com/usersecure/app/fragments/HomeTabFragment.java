package com.usersecure.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.usersecure.app.adapters.FavoritePasswordAdapter;
import com.usersecure.app.adapters.FavoriteUsernameAdapter;
import com.usersecure.app.adapters.UsernameAdapter;
import com.usersecure.app.database.AppDatabase;
import com.usersecure.app.database.PasswordEntity;
import com.usersecure.app.database.UsernameEntity;
import com.usersecure.app.databinding.FragmentHomeTabBinding;
import com.usersecure.app.utils.ClipboardHelper;

import java.util.List;

/**
 * HomeTabFragment — A tab page displayed inside HomeFragment's ViewPager2.
 * Shows the most recent 5 saved usernames OR passwords depending on the type arg.
 */
public class HomeTabFragment extends Fragment {

    private static final String ARG_TYPE = "tab_type";
    private FragmentHomeTabBinding binding;
    private String type; // "usernames" or "passwords"

    public static HomeTabFragment newInstance(String type) {
        HomeTabFragment fragment = new HomeTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeTabBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = (getArguments() != null) ? getArguments().getString(ARG_TYPE, "usernames") : "usernames";

        if ("usernames".equals(type)) {
            setupUsernamesList();
        } else {
            setupPasswordsList();
        }
    }

    private void setupUsernamesList() {
        FavoriteUsernameAdapter adapter = new FavoriteUsernameAdapter(requireContext(),
                new FavoriteUsernameAdapter.OnItemActionListener() {
                    @Override
                    public void onCopy(UsernameEntity item) {
                        ClipboardHelper.copy(requireContext(), item.username);
                    }
                    @Override
                    public void onDelete(UsernameEntity item) {
                        // No-op in Home tab (read-only list)
                    }
                });
        binding.rvRecentUsernames.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentUsernames.setAdapter(adapter);

        AppDatabase.getInstance(requireContext()).usernameDao()
                .getRecentUsernames().observe(getViewLifecycleOwner(), items -> {
            if (items == null || items.isEmpty()) {
                binding.rvRecentUsernames.setVisibility(View.GONE);
                binding.tvEmptyState.setVisibility(View.VISIBLE);
                binding.tvEmptyState.setText("No recent usernames.\nGo to Username tab to generate!");
            } else {
                binding.rvRecentUsernames.setVisibility(View.VISIBLE);
                binding.tvEmptyState.setVisibility(View.GONE);
                adapter.setItems(items);
            }
        });
    }

    private void setupPasswordsList() {
        FavoritePasswordAdapter adapter = new FavoritePasswordAdapter(requireContext(),
                new FavoritePasswordAdapter.OnItemActionListener() {
                    @Override
                    public void onCopy(PasswordEntity item) {
                        ClipboardHelper.copy(requireContext(), item.password);
                    }
                    @Override
                    public void onDelete(PasswordEntity item) {
                        // No-op in Home tab (read-only list)
                    }
                });
        binding.rvRecentUsernames.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentUsernames.setAdapter(adapter);

        AppDatabase.getInstance(requireContext()).passwordDao()
                .getRecentPasswords().observe(getViewLifecycleOwner(), items -> {
            if (items == null || items.isEmpty()) {
                binding.rvRecentUsernames.setVisibility(View.GONE);
                binding.tvEmptyState.setVisibility(View.VISIBLE);
                binding.tvEmptyState.setText("No recent passwords.\nGo to Password tab to generate!");
            } else {
                binding.rvRecentUsernames.setVisibility(View.VISIBLE);
                binding.tvEmptyState.setVisibility(View.GONE);
                adapter.setItems(items);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
