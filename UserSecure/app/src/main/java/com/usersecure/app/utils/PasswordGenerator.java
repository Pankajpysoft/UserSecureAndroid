package com.usersecure.app.utils;

import java.security.SecureRandom;

/**
 * Utility class for generating secure, customizable passwords.
 * Supports uppercase, lowercase, numbers, and symbols.
 * Uses SecureRandom for cryptographically strong randomness.
 */
public class PasswordGenerator {

    // Character sets
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS   = "0123456789";
    private static final String SYMBOLS   = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generate a password with the given options.
     *
     * @param length       desired password length (4–20)
     * @param useUppercase include uppercase letters
     * @param useLowercase include lowercase letters
     * @param useNumbers   include digits
     * @param useSymbols   include special symbols
     * @return generated password string, or empty string if no character type selected
     */
    public static String generate(int length, boolean useUppercase, boolean useLowercase,
                                  boolean useNumbers, boolean useSymbols) {
        StringBuilder pool = new StringBuilder();
        StringBuilder password = new StringBuilder();

        // Build character pool and ensure at least one char from each selected set
        if (useUppercase) {
            pool.append(UPPERCASE);
            password.append(UPPERCASE.charAt(secureRandom.nextInt(UPPERCASE.length())));
        }
        if (useLowercase) {
            pool.append(LOWERCASE);
            password.append(LOWERCASE.charAt(secureRandom.nextInt(LOWERCASE.length())));
        }
        if (useNumbers) {
            pool.append(NUMBERS);
            password.append(NUMBERS.charAt(secureRandom.nextInt(NUMBERS.length())));
        }
        if (useSymbols) {
            pool.append(SYMBOLS);
            password.append(SYMBOLS.charAt(secureRandom.nextInt(SYMBOLS.length())));
        }

        // If no type selected, fall back to all alphanumeric
        if (pool.length() == 0) {
            pool.append(UPPERCASE).append(LOWERCASE).append(NUMBERS);
        }

        // Fill remaining length with random chars from pool
        while (password.length() < length) {
            password.append(pool.charAt(secureRandom.nextInt(pool.length())));
        }

        // Shuffle the password so guaranteed chars aren't always at the start
        return shuffle(password.toString().substring(0, length));
    }

    /**
     * Shuffle the characters in a string using Fisher-Yates.
     */
    private static String shuffle(String input) {
        char[] chars = input.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = secureRandom.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }

    /**
     * Calculate password strength based on character diversity and length.
     *
     * @param password the password to evaluate
     * @return "Weak", "Fair", "Good", or "Strong"
     */
    public static String calculateStrength(String password) {
        if (password == null || password.isEmpty()) return "Weak";

        int score = 0;

        // Length scoring
        if (password.length() >= 8)  score++;
        if (password.length() >= 12) score++;
        if (password.length() >= 16) score++;

        // Character variety scoring
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[a-z].*")) score++;
        if (password.matches(".*\\d.*"))   score++;
        if (password.matches(".*[^A-Za-z0-9].*")) score++;

        // Map score to label
        if (score >= 6) return "Strong";
        if (score >= 4) return "Good";
        if (score >= 2) return "Fair";
        return "Weak";
    }

    /**
     * Get a 0–100 strength score for use in a ProgressBar.
     */
    public static int getStrengthScore(String password) {
        String strength = calculateStrength(password);
        switch (strength) {
            case "Strong": return 100;
            case "Good":   return 70;
            case "Fair":   return 40;
            default:       return 20;
        }
    }
}
