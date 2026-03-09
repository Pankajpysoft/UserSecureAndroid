package com.usersecure.app.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.usersecure.app.R;
import com.usersecure.app.database.AppDatabase;
import com.usersecure.app.database.PasswordEntity;
import com.usersecure.app.database.UsernameEntity;
import com.usersecure.app.databinding.FragmentAddItemBinding;
import com.usersecure.app.utils.ClipboardHelper;
import com.usersecure.app.utils.PasswordGenerator;
import com.usersecure.app.utils.UsernameGenerator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AddItemFragment — Allows users to manually enter or auto-generate a username or password,
 * then copy it to clipboard or save it to the Room database.
 * A RadioGroup selects the item type (Username / Password).
 */
public class AddItemFragment extends Fragment {

    private FragmentAddItemBinding binding;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean isUsername = true; // true = Username, false = Password

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupTypeSelector();
        setupButtons();
    }

    /** Listen for Radio button changes to switch between username/password mode. */
    private void setupTypeSelector() {
        binding.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            isUsername = (checkedId == R.id.rbUsername);
            binding.tilInput.setHint(isUsername
                    ? getString(R.string.add_username_hint)
                    : getString(R.string.add_password_hint));
            binding.etInput.setText(""); // Clear on type switch
        });
    }

    private void setupButtons() {
        // Auto-generate button
        binding.btnAutoGenerate.setOnClickListener(v -> {
            animateButton(v);
            String generated;
            if (isUsername) {
                generated = UsernameGenerator.generateOne("Cool");
            } else {
                generated = PasswordGenerator.generate(12, true, true, true, false);
            }
            binding.etInput.setText(generated);
            binding.etInput.setSelection(generated.length()); // move cursor to end
        });

        // Copy button
        binding.btnCopy.setOnClickListener(v -> {
            String text = getInputText();
            if (!TextUtils.isEmpty(text)) {
                ClipboardHelper.copy(requireContext(), text);
            } else {
                Toast.makeText(requireContext(), "Nothing to copy!", Toast.LENGTH_SHORT).show();
            }
        });

        // Save button
        binding.btnSave.setOnClickListener(v -> {
            animateButton(v);
            String text = getInputText();
            if (TextUtils.isEmpty(text)) {
                Toast.makeText(requireContext(), "Please enter or generate an item first",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (isUsername) {
                saveUsername(text);
            } else {
                savePassword(text);
            }
        });
    }

    private String getInputText() {
        return (binding.etInput.getText() != null)
                ? binding.etInput.getText().toString().trim() : "";
    }

    private void animateButton(View v) {
        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(80)
                .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(80).start())
                .start();
    }

    private void saveUsername(String username) {
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            int exists = db.usernameDao().existsByUsername(username);
            if (exists > 0) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), getString(R.string.already_saved),
                                Toast.LENGTH_SHORT).show());
            } else {
                db.usernameDao().insert(new UsernameEntity(
                        username, "Manual", System.currentTimeMillis()));
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), getString(R.string.saved),
                            Toast.LENGTH_SHORT).show();
                    binding.etInput.setText("");
                });
            }
        });
    }

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
                db.passwordDao().insert(new PasswordEntity(
                        password, password.length(), strength, System.currentTimeMillis()));
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), getString(R.string.saved),
                            Toast.LENGTH_SHORT).show();
                    binding.etInput.setText("");
                });
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
