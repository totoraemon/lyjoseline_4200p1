package util;

import puzzle.PuzzleState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Utility for synthesizing valid, guaranteed-solvable 8-puzzle configurations.
 * 
 * Generation strategy:
 * Applies a bounded random walk starting backward from the solved target state.
 * To prevent local oscillation, immediate undo-moves (backtracking) are pruned.
 */
public final class PuzzleGenerator {

    private static final Random RANDOM = new Random();
    private static final int DEFAULT_SCRAMBLE_STEPS = 30;

    // Suppress default constructor to enforce utility pattern
    private PuzzleGenerator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    /**
     * Generates a solvable board using the default scrambling depth (30 steps).
     *
     * @return Flat 9-character layout string of a scrambled puzzle state
     */
    public static String generateRandom() {
        return generateRandom(DEFAULT_SCRAMBLE_STEPS, RANDOM);
    }

    /**
     * Generates a solvable board state using a specified scramble depth.
     *
     * @param scrambleSteps Number of random walk moves away from the target state
     * @return Scrambled layout string
     */
    public static String generateRandom(int scrambleSteps) {
        return generateRandom(scrambleSteps, RANDOM);
    }

    /**
     * Generates a solvable board state with explicit depth and seed control for deterministic testing.
     *
     * @param scrambleSteps Number of random walk transitions
     * @param rng           Random number generator instance (allows passing seeded Random)
     * @return Scrambled layout string
     */
    public static String generateRandom(int scrambleSteps, Random rng) {
        if (scrambleSteps < 0) {
            throw new IllegalArgumentException("Scramble depth cannot be negative.");
        }
        Objects.requireNonNull(rng, "Random generator reference cannot be null.");

        String current = PuzzleState.GOAL_STATE;
        String previous = null;

        for (int step = 0; step < scrambleSteps; step++) {
            List<String> validNeighbors = new ArrayList<>(PuzzleState.getNeighbors(current));

            // Prevent immediate backtrack to the tile state we just came from
            if (previous != null && validNeighbors.size() > 1) {
                validNeighbors.remove(previous);
            }

            String selectedNext = validNeighbors.get(rng.nextInt(validNeighbors.size()));
            previous = current;
            current = selectedNext;
        }

        return current;
    }
}