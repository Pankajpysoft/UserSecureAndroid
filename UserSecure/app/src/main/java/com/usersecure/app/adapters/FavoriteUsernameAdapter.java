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
import com.usersecure.app.database.UsernameEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for displaying favorite usernames from the Room database.
 * Each item shows the username with Copy and Delete action buttons.
 */
public class FavoriteUsernameAdapter extends RecyclerView.Adapter<FavoriteUsernameAdapter.ViewHolder> {

    private final Context context;
    private List<UsernameEntity> items = new ArrayList<>();
    private OnItemActionListener listener;
    private int lastAnimatedPosition = -1;

    public interface OnItemActionListener {
        void onCopy(UsernameEntity item);
        void onDelete(UsernameEntity item);
    }

    public FavoriteUsernameAdapter(Context context, OnItemActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setItems(List<UsernameEntity> items) {
        this.items = items;
        lastAnimatedPosition = -1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_favorite_username, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsernameEntity item = items.get(position);
        holder.tvUsername.setText(item.username);

        // Animate item on first appearance
        if (position > lastAnimatedPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.item_animation_fall_down);
            holder.itemView.startAnimation(animation);
            lastAnimatedPosition = position;
        }

        holder.btnCopy.setOnClickListener(v -> {
            if (listener != null) listener.onCopy(item);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        MaterialButton btnCopy;
        MaterialButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            btnCopy    = itemView.findViewById(R.id.btnCopy);
            btnDelete  = itemView.findViewById(R.id.btnDelete);
        }
    }
}
