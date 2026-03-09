package com.usersecure.app.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.usersecure.app.R;
import com.usersecure.app.adapters.UsernameAdapter;
import com.usersecure.app.database.AppDatabase;
import com.usersecure.app.database.UsernameEntity;
import com.usersecure.app.databinding.FragmentUsernameBinding;
import com.usersecure.app.utils.ClipboardHelper;
import com.usersecure.app.utils.UsernameGenerator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * UsernameFragment — Lets users choose a style and count, then generates a list of usernames.
 * Each username can be copied to clipboard or saved to the Room database as a favorite.
 */
public class UsernameFragment extends Fragment {

    private FragmentUsernameBinding binding;
    private UsernameAdapter adapter;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUsernameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupDropdown();
        setupRecyclerView();
        setupGenerateButton();
    }

    /** Populate the style dropdown with Cool, Funny, Professional options. */
    private void setupDropdown() {
        String[] styles = {"Cool", "Funny", "Professional"};
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                styles
        );
        binding.actvStyle.setAdapter(dropdownAdapter);
        binding.actvStyle.setText(styles[0], false);
    }

    private void setupRecyclerView() {
        adapter = new UsernameAdapter(requireContext(),
                new UsernameAdapter.OnItemActionListener() {
                    @Override
                    public void onCopy(String username) {
                        ClipboardHelper.copy(requireContext(), username);
                    }

                    @Override
                    public void onSave(String username) {
                        saveUsername(username);
                    }
                });

        binding.rvUsernames.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvUsernames.setAdapter(adapter);
    }

    private void setupGenerateButton() {
        binding.btnGenerate.setOnClickListener(v -> {
            animateButton(v);
            generateUsernames();
        });
    }

    /** Animate a button press with a brief scale bounce. */
    private void animateButton(View v) {
        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(80)
                .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(80).start())
                .start();
    }

    /** Read inputs and generate usernames. */
    private void generateUsernames() {
        String countStr = binding.etCount.getText() != null
                ? binding.etCount.getText().toString().trim() : "5";
        int count = TextUtils.isEmpty(countStr) ? 5 : Integer.parseInt(countStr);
        count = Math.min(Math.max(count, 1), 20); // clamp 1–20

        String style = binding.actvStyle.getText().toString().trim();
        if (TextUtils.isEmpty(style)) style = "Cool";

        List<String> generated = UsernameGenerator.generate(count, style);
        adapter.setUsernames(generated);

        // Show/hide empty state
        binding.tvEmpty.setVisibility(generated.isEmpty() ? View.VISIBLE : View.GONE);
    }

    /** Save a username to the Room database on a background thread. */
    private void saveUsername(String username) {
        String style = binding.actvStyle.getText().toString().trim();

        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            int exists = db.usernameDao().existsByUsername(username);
            if (exists > 0) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), getString(R.string.already_saved),
                                Toast.LENGTH_SHORT).show());
            } else {
                UsernameEntity entity = new UsernameEntity(username, style,
                        System.currentTimeMillis());
                db.usernameDao().insert(entity);
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), getString(R.string.saved),
                                Toast.LENGTH_SHORT).show());
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
