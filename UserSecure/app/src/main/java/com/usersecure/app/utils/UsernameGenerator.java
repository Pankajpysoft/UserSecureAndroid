package com.usersecure.app.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utility class for generating creative usernames.
 * Combines adjectives, nouns, and optional numeric suffixes
 * across three styles: Cool, Funny, and Professional.
 */
public class UsernameGenerator {

    private static final Random random = new Random();

    // ---- Cool Style Word Banks ----
    private static final String[] COOL_ADJECTIVES = {
        "Shadow", "Neon", "Cyber", "Storm", "Blaze", "Frost", "Iron",
        "Phantom", "Dark", "Nova", "Quantum", "Hyper", "Ultra", "Omega",
        "Alpha", "Delta", "Turbo", "Stealth", "Nexus", "Void"
    };
    private static final String[] COOL_NOUNS = {
        "Wolf", "Hawk", "Viper", "Dragon", "Phoenix", "Titan", "Sage",
        "Raven", "Ghost", "Knight", "Blade", "Hunter", "Ranger", "Ninja",
        "Comet", "Spike", "Jet", "Flash", "Pulse", "Strike"
    };

    // ---- Funny Style Word Banks ----
    private static final String[] FUNNY_ADJECTIVES = {
        "Fluffy", "Bouncy", "Grumpy", "Silly", "Wobbly", "Sneaky",
        "Chubby", "Dizzy", "Zany", "Goofy", "Wacky", "Klutzy",
        "Sleepy", "Giggly", "Doofy", "Bumbling", "Quirky", "Loopy",
        "Wiggly", "Snappy"
    };
    private static final String[] FUNNY_NOUNS = {
        "Pickle", "Noodle", "Waffle", "Potato", "Penguin", "Muffin",
        "Biscuit", "Nugget", "Pudding", "Pancake", "Blobfish", "Hamster",
        "Platypus", "Squirrel", "Hedgehog", "Jellybean", "Turnip",
        "Gumdrop", "Snickerdoodle", "Bumblebee"
    };

    // ---- Professional Style Word Banks ----
    private static final String[] PROF_ADJECTIVES = {
        "Smart", "Elite", "Prime", "Swift", "Sharp", "Clear", "Bold",
        "Pure", "True", "Keen", "Bright", "Expert", "Skilled", "Modern",
        "Focused", "Creative", "Reliable", "Dynamic", "Strategic", "Agile"
    };
    private static final String[] PROF_NOUNS = {
        "Dev", "Coder", "Analyst", "Hacker", "Builder", "Designer",
        "Engineer", "Maker", "Creator", "Strategist", "Manager", "Expert",
        "Consultant", "Advisor", "Architect", "Specialist", "Professional",
        "Executive", "Innovator", "Leader"
    };

    /**
     * Generate a list of usernames for the given style.
     *
     * @param count  number of usernames to generate (1–20)
     * @param style  "Cool", "Funny", or "Professional"
     * @return list of generated username strings
     */
    public static List<String> generate(int count, String style) {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            results.add(generateOne(style));
        }
        return results;
    }

    /**
     * Generate a single username for the given style.
     */
    public static String generateOne(String style) {
        String adjective;
        String noun;

        switch (style) {
            case "Funny":
                adjective = FUNNY_ADJECTIVES[random.nextInt(FUNNY_ADJECTIVES.length)];
                noun = FUNNY_NOUNS[random.nextInt(FUNNY_NOUNS.length)];
                break;
            case "Professional":
                adjective = PROF_ADJECTIVES[random.nextInt(PROF_ADJECTIVES.length)];
                noun = PROF_NOUNS[random.nextInt(PROF_NOUNS.length)];
                break;
            case "Cool":
            default:
                adjective = COOL_ADJECTIVES[random.nextInt(COOL_ADJECTIVES.length)];
                noun = COOL_NOUNS[random.nextInt(COOL_NOUNS.length)];
                break;
        }

        // 60% chance to append a number suffix (10–999)
        boolean addNumber = random.nextFloat() < 0.6f;
        String suffix = addNumber ? String.valueOf(random.nextInt(990) + 10) : "";

        return adjective + noun + suffix;
    }
}
