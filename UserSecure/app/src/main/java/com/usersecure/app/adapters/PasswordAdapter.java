package com.usersecure.app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.usersecure.app.R;
import com.usersecure.app.utils.PasswordGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for displaying generated passwords.
 * Each item shows the password with a strength meter, Copy and Save buttons.
 */
public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> {

    private final Context context;
    private List<String> passwords = new ArrayList<>();
    private OnItemActionListener listener;
    private int lastAnimatedPosition = -1;

    public interface OnItemActionListener {
        void onCopy(String password);
        void onSave(String password);
    }

    public PasswordAdapter(Context context, OnItemActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setPasswords(List<String> passwords) {
        this.passwords = passwords;
        lastAnimatedPosition = -1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_password, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String password = passwords.get(position);
        holder.tvPassword.setText(password);

        // Setup strength meter
        String strength = PasswordGenerator.calculateStrength(password);
        int score = PasswordGenerator.getStrengthScore(password);
        holder.progressStrength.setProgress(score);
        holder.tvStrength.setText(strength);

        // Color code based on strength
        int color;
        switch (strength) {
            case "Strong": color = ContextCompat.getColor(context, R.color.colorStrengthStrong); break;
            case "Good":   color = ContextCompat.getColor(context, R.color.colorStrengthGood);   break;
            case "Fair":   color = ContextCompat.getColor(context, R.color.colorStrengthFair);   break;
            default:       color = ContextCompat.getColor(context, R.color.colorStrengthWeak);   break;
        }
        holder.tvStrength.setTextColor(color);
        holder.progressStrength.setProgressTintList(
                android.content.res.ColorStateList.valueOf(color));

        // Animate item entering
        if (position > lastAnimatedPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.item_animation_fall_down);
            holder.itemView.startAnimation(animation);
            lastAnimatedPosition = position;
        }

        holder.btnCopy.setOnClickListener(v -> {
            v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(80)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(80));
            if (listener != null) listener.onCopy(password);
        });

        holder.btnSave.setOnClickListener(v -> {
            v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(80)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(80));
            if (listener != null) listener.onSave(password);
        });
    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPassword;
        ProgressBar progressStrength;
        TextView tvStrength;
        MaterialButton btnCopy;
        MaterialButton btnSave;

        ViewHolder(View itemView) {
            super(itemView);
            tvPassword       = itemView.findViewById(R.id.tvPassword);
            progressStrength = itemView.findViewById(R.id.progressStrength);
            tvStrength       = itemView.findViewById(R.id.tvStrength);
            btnCopy          = itemView.findViewById(R.id.btnCopy);
            btnSave          = itemView.findViewById(R.id.btnSave);
        }
    }
}
