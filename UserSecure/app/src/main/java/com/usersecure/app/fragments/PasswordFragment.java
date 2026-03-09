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
import com.usersecure.app.adapters.PasswordAdapter;
import com.usersecure.app.database.AppDatabase;
import com.usersecure.app.database.PasswordEntity;
import com.usersecure.app.databinding.FragmentPasswordBinding;
import com.usersecure.app.utils.ClipboardHelper;
import com.usersecure.app.utils.PasswordGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * PasswordFragment — Lets users configure password options (length, charset),
 * then generates a list of passwords. Each password shows a strength meter.
 * Supports copy to clipboard and save to Room database.
 */
public class PasswordFragment extends Fragment {

    private FragmentPasswordBinding binding;
    private PasswordAdapter adapter;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private int passwordLength = 12;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSlider();
        setupRecyclerView();
        setupGenerateButton();
    }

    /** Bind slider to length label and track current value. */
    private void setupSlider() {
        passwordLength = (int) binding.sliderLength.getValue();
        updateLengthLabel(passwordLength);

        binding.sliderLength.addOnChangeListener((slider, value, fromUser) -> {
            passwordLength = (int) value;
            updateLengthLabel(passwordLength);
        });
    }

    private void updateLengthLabel(int length) {
        binding.tvLengthLabel.setText(getString(R.string.password_length_label, length));
    }

    private void setupRecyclerView() {
        adapter = new PasswordAdapter(requireContext(),
                new PasswordAdapter.OnItemActionListener() {
                    @Override
                    public void onCopy(String password) {
                        ClipboardHelper.copy(requireContext(), password);
                    }

                    @Override
                    public void onSave(String password) {
                        savePassword(password);
                    }
                });

        binding.rvPasswords.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPasswords.setAdapter(adapter);
    }

    private void setupGenerateButton() {
        binding.btnGenerate.setOnClickListener(v -> {
            animateButton(v);
            generatePasswords();
        });
    }

    private void animateButton(View v) {
        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(80)
                .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(80).start())
                .start();
    }

    /** Generate a batch of 5 passwords with current settings. */
    private void generatePasswords() {
        boolean useUppercase = binding.cbUppercase.isChecked();
        boolean useLowercase = binding.cbLowercase.isChecked();
        boolean useNumbers   = binding.cbNumbers.isChecked();
        boolean useSymbols   = binding.cbSymbols.isChecked();

        // At least one must be selected
        if (!useUppercase && !useLowercase && !useNumbers && !useSymbols) {
            Toast.makeText(requireContext(), "Please select at least one character type",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> generated = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            generated.add(PasswordGenerator.generate(
                    passwordLength, useUppercase, useLowercase, useNumbers, useSymbols));
        }

        adapter.setPasswords(generated);
        binding.tvEmpty.setVisibility(View.GONE);
    }

    /** Save a password to Room database on background thread. */
    private void savePassword(String password) {
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            int exists = db.passwordDao().existsByPassword(password);
            if (exists > 0) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), getString(R.string.already_saved),
                                Toast.LENGTH_SHORT).show());
            } else {
                String strength = PasswordGenerator.calculateStrength(password);
                PasswordEntity entity = new PasswordEntity(
                        password, password.length(), strength, System.currentTimeMillis());
                db.passwordDao().insert(entity);
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
