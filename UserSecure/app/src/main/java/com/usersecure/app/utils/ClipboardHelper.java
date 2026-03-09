package com.usersecure.app.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

/**
 * Helper class for copying text to the system clipboard and showing a confirmation toast.
 */
public class ClipboardHelper {

    private static final String CLIP_LABEL = "UserSecure";

    /**
     * Copy the given text to the system clipboard and show a toast.
     *
     * @param context the calling context
     * @param text    the text to copy
     */
    public static void copy(Context context, String text) {
        ClipboardManager clipboard =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(CLIP_LABEL, text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
        }
    }
}
