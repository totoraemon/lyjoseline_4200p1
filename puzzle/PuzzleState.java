package puzzle;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides static utilities for managing, validating, and mutating 8-puzzle board configurations.
 * 
 * Grid mapping specifications:
 * - Layout is modeled as a flat 9-character string in row-major order.
 * - The character '0' represents the vacant space (blank tile).
 * - Solvability is determined by evaluating invariant parity via inversion count.
 */
public class PuzzleState {

    public static final String GOAL_STATE = "012345678";
    public static final char EMPTY_TILE = '0';
    public static final int GRID_DIMENSION = 3;
    public static final int BOARD_CAPACITY = GRID_DIMENSION * GRID_DIMENSION;

    // Direction vectors for generating neighboring state transitions [rowDelta, colDelta]
    private static final int[][] MOVE_OFFSETS = {
        {-1, 0}, // Move blank UP
        {1, 0},  // Move blank DOWN
        {0, -1}, // Move blank LEFT
        {0, 1}   // Move blank RIGHT
    };

    /**
     * Verifies whether a string conforms to a standard 8-puzzle configuration.
     * Checks for proper length and distinct character distribution from '0' to '8'.
     *
     * @param state Flat string layout to analyze
     * @return true if the layout is structurally sound; false otherwise
     */
    public static boolean isValidState(String state) {
        if (state == null || state.length() != BOARD_CAPACITY) {
            return false;
        }

        int digitMask = 0;
        for (int idx = 0; idx < BOARD_CAPACITY; idx++) {
            char tileChar = state.charAt(idx);
            if (tileChar < '0' || tileChar > '8') {
                return false;
            }

            int tileVal = tileChar - '0';
            int bitFlag = 1 << tileVal;

            // Detect duplicate tile values using bitwise tracking
            if ((digitMask & bitFlag) != 0) {
                return false;
            }
            digitMask |= bitFlag;
        }

        return true;
    }

    /**
     * Generates all valid neighboring board configurations reachable by single-tile sliding moves.
     *
     * @param state Current board configuration
     * @return List containing 2 to 4 valid adjacent state representations
     */
    public static List<String> getNeighbors(String state) {
        if (!isValidState(state)) {
            throw new IllegalArgumentException("Cannot generate transitions for an invalid board layout.");
        }

        int emptyIndex = state.indexOf(EMPTY_TILE);
        int emptyRow = emptyIndex / GRID_DIMENSION;
        int emptyCol = emptyIndex % GRID_DIMENSION;

        List<String> transitions = new ArrayList<>(4);

        for (int[] offset : MOVE_OFFSETS) {
            int nextRow = emptyRow + offset[0];
            int nextCol = emptyCol + offset[1];

            // Verify grid boundary boundaries
            if (nextRow >= 0 && nextRow < GRID_DIMENSION && nextCol >= 0 && nextCol < GRID_DIMENSION) {
                int targetIndex = nextRow * GRID_DIMENSION + nextCol;
                transitions.add(swapTiles(state, emptyIndex, targetIndex));
            }
        }

        return transitions;
    }

    /**
     * Constructs a new state string by swapping two element positions.
     */
    private static String swapTiles(String layout, int pos1, int pos2) {
        char[] elements = layout.toCharArray();
        char temp = elements[pos1];
        elements[pos1] = elements[pos2];
        elements[pos2] = temp;
        return new String(elements);
    }

    /**
     * Calculates total tile inversions (excluding the empty space).
     * An inversion occurs whenever a larger-numbered tile precedes a smaller-numbered tile.
     *
     * @param state Board layout string
     * @return Total count of pair inversions
     */
    public static int countInversions(String state) {
        if (!isValidState(state)) {
            throw new IllegalArgumentException("Invalid state supplied for inversion calculation.");
        }

        int[] numericalTiles = new int[BOARD_CAPACITY - 1];
        int writeIndex = 0;

        // Filter out zero to isolate numbered tiles
        for (int readIndex = 0; readIndex < BOARD_CAPACITY; readIndex++) {
            int value = state.charAt(readIndex) - '0';
            if (value != 0) {
                numericalTiles[writeIndex++] = value;
            }
        }

        int totalInversions = 0;
        for (int i = 0; i < numericalTiles.length - 1; i++) {
            for (int j = i + 1; j < numericalTiles.length; j++) {
                if (numericalTiles[i] > numericalTiles[j]) {
                    totalInversions++;
                }
            }
        }

        return totalInversions;
    }

    /**
     * Tests whether an 8-puzzle board can reach the target state.
     * For standard 3x3 boards, solvability requires an even number of tile inversions.
     *
     * @param state Board layout to verify
     * @return true if solvable; false if impossible to solve
     */
    public static boolean isSolvable(String state) {
        return countInversions(state) % 2 == 0;
    }

    /**
     * Flattens a 2D matrix layout into a single row-major string representation.
     *
     * @param puzzle 3x3 matrix representation
     * @return Compact 9-character layout string
     */
    public static String toString(int[][] puzzle) {
        if (puzzle == null || puzzle.length != GRID_DIMENSION || puzzle[0].length != GRID_DIMENSION) {
            throw new IllegalArgumentException("Grid array dimensions must be exactly 3x3.");
        }

        StringBuilder buffer = new StringBuilder(BOARD_CAPACITY);
        for (int row = 0; row < GRID_DIMENSION; row++) {
            for (int col = 0; col < GRID_DIMENSION; col++) {
                buffer.append(puzzle[row][col]);
            }
        }
        return buffer.toString();
    }

    /**
     * Formats a flat state string into a multi-line visual grid representation for terminal logging.
     *
     * @param state Flat layout string
     * @return Multi-line formatted grid visualization
     */
    public static String toGridString(String state) {
        if (state == null || state.length() != BOARD_CAPACITY) {
            return "Invalid State Format";
        }

        return String.format("%c %c %c\n%c %c %c\n%c %c %c",
            state.charAt(0), state.charAt(1), state.charAt(2),
            state.charAt(3), state.charAt(4), state.charAt(5),
            state.charAt(6), state.charAt(7), state.charAt(8)
        );
    }
}