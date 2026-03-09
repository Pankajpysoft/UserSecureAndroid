package com.usersecure.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.usersecure.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for displaying generated usernames.
 * Each item shows the username with Copy and Save action buttons.
 * Includes animated item transitions.
 */
public class UsernameAdapter extends RecyclerView.Adapter<UsernameAdapter.ViewHolder> {

    private final Context context;
    private List<String> usernames = new ArrayList<>();
    private OnItemActionListener listener;
    private int lastAnimatedPosition = -1;

    public interface OnItemActionListener {
        void onCopy(String username);
        void onSave(String username);
    }

    public UsernameAdapter(Context context, OnItemActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
        lastAnimatedPosition = -1;
        notifyDataSetChanged();
    }

    public void addUsername(String username) {
        usernames.add(0, username);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_username, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String username = usernames.get(position);
        holder.tvUsername.setText(username);

        // Animate item entering
        if (position > lastAnimatedPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.item_animation_fall_down);
            holder.itemView.startAnimation(animation);
            lastAnimatedPosition = position;
        }

        holder.btnCopy.setOnClickListener(v -> {
            // Scale button animation
            v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(80)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(80));
            if (listener != null) listener.onCopy(username);
        });

        holder.btnSave.setOnClickListener(v -> {
            v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(80)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(80));
            if (listener != null) listener.onSave(username);
        });
    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        MaterialButton btnCopy;
        MaterialButton btnSave;

        ViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            btnCopy    = itemView.findViewById(R.id.btnCopy);
            btnSave    = itemView.findViewById(R.id.btnSave);
        }
    }
}
