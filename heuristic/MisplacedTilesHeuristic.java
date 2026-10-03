package heuristic;

/**
 * Computes the H1 Misplaced Tiles heuristic value for an 8-puzzle board.
 * 
 * Evaluation mechanism:
 * Counts every tile currently sitting in an incorrect grid position relative 
 * to the target state. The empty spot ('0') is explicitly excluded from this count.
 * 
 * Theoretical properties:
 * - Admissible: Each misplaced tile must move at least once, guaranteeing 
 *   this function never overestimates the remaining cost.
 * - Dominance: Less informed than H2 (Manhattan Distance), typically resulting 
 *   in a larger number of generated search nodes.
 */
public class MisplacedTilesHeuristic implements Heuristic {

    private static final String TARGET_LAYOUT = "012345678";
    private static final char[] TARGET_TILES = TARGET_LAYOUT.toCharArray();
    private static final char EMPTY_SPOT = '0';
    private static final int BOARD_SIZE = TARGET_TILES.length;

    /**
     * Determines how many numbered tiles are out of place.
     * 
     * @param state Flat string representation of the current 3x3 layout
     * @return Total count of off-target tiles
     * @throws IllegalArgumentException if state is null or incorrectly formatted
     */
    @Override
    public int calculate(String state) {
        if (state == null || state.length() != BOARD_SIZE) {
            throw new IllegalArgumentException("Expected state length: " + BOARD_SIZE);
        }

        int count = 0;

        for (int idx = 0; idx < BOARD_SIZE; idx++) {
            char tile = state.charAt(idx);

            // Ignore the empty space and correctly positioned tiles
            if (tile != EMPTY_SPOT && tile != TARGET_TILES[idx]) {
                count++;
            }
        }

        return count;
    }

    @Override
    public String getName() {
        return "H1 (Misplaced Tiles)";
    }
}