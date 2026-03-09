package com.usersecure.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.usersecure.app.R;
import com.usersecure.app.adapters.FavoritePasswordAdapter;
import com.usersecure.app.adapters.FavoriteUsernameAdapter;
import com.usersecure.app.database.AppDatabase;
import com.usersecure.app.database.PasswordEntity;
import com.usersecure.app.database.UsernameEntity;
import com.usersecure.app.databinding.FragmentFavoritesTabBinding;
import com.usersecure.app.utils.ClipboardHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * FavoritesTabFragment — Tab page inside FavoritesFragment.
 * Shows either saved usernames or passwords from Room DB,
 * with Copy and Delete buttons per item.
 */
public class FavoritesTabFragment extends Fragment {

    private static final String ARG_TYPE = "fav_type";
    private FragmentFavoritesTabBinding binding;
    private String type;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static FavoritesTabFragment newInstance(String type) {
        FavoritesTabFragment fragment = new FavoritesTabFragment();
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
        binding = FragmentFavoritesTabBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = (getArguments() != null)
                ? getArguments().getString(ARG_TYPE, "usernames") : "usernames";

        if ("usernames".equals(type)) {
            setupUsernamesFavorites();
        } else {
            setupPasswordsFavorites();
        }
    }

    /** Show all saved username favorites with copy and delete. */
    private void setupUsernamesFavorites() {
        FavoriteUsernameAdapter adapter = new FavoriteUsernameAdapter(requireContext(),
                new FavoriteUsernameAdapter.OnItemActionListener() {
                    @Override
                    public void onCopy(UsernameEntity item) {
                        ClipboardHelper.copy(requireContext(), item.username);
                    }

                    @Override
                    public void onDelete(UsernameEntity item) {
                        executor.execute(() -> {
                            AppDatabase.getInstance(requireContext())
                                    .usernameDao().delete(item);
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(requireContext(),
                                            getString(R.string.deleted),
                                            Toast.LENGTH_SHORT).show());
                        });
                    }
                });

        binding.rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFavorites.setAdapter(adapter);

        AppDatabase.getInstance(requireContext()).usernameDao()
                .getAllUsernames().observe(getViewLifecycleOwner(), items -> {
            if (items == null || items.isEmpty()) {
                binding.rvFavorites.setVisibility(View.GONE);
                binding.tvEmpty.setVisibility(View.VISIBLE);
                binding.tvEmpty.setText(getString(R.string.no_fav_usernames));
            } else {
                binding.rvFavorites.setVisibility(View.VISIBLE);
                binding.tvEmpty.setVisibility(View.GONE);
                adapter.setItems(items);
            }
        });
    }

    /** Show all saved password favorites with copy and delete. */
    private void setupPasswordsFavorites() {
        FavoritePasswordAdapter adapter = new FavoritePasswordAdapter(requireContext(),
                new FavoritePasswordAdapter.OnItemActionListener() {
                    @Override
                    public void onCopy(PasswordEntity item) {
                        ClipboardHelper.copy(requireContext(), item.password);
                    }

                    @Override
                    public void onDelete(PasswordEntity item) {
                        executor.execute(() -> {
                            AppDatabase.getInstance(requireContext())
                                    .passwordDao().delete(item);
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(requireContext(),
                                            getString(R.string.deleted),
                                            Toast.LENGTH_SHORT).show());
                        });
                    }
                });

        binding.rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFavorites.setAdapter(adapter);

        AppDatabase.getInstance(requireContext()).passwordDao()
                .getAllPasswords().observe(getViewLifecycleOwner(), items -> {
            if (items == null || items.isEmpty()) {
                binding.rvFavorites.setVisibility(View.GONE);
                binding.tvEmpty.setVisibility(View.VISIBLE);
                binding.tvEmpty.setText(getString(R.string.no_fav_passwords));
            } else {
                binding.rvFavorites.setVisibility(View.VISIBLE);
                binding.tvEmpty.setVisibility(View.GONE);
                adapter.setItems(items);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
