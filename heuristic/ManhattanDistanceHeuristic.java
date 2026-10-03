package heuristic;

/**
 * H2 Heuristic: Sum of Manhattan distances for all tiles (excluding blank).
 * Manhattan distance is the number of moves (vertical + horizontal) a tile
 * needs to reach its goal position.
 */
public class ManhattanDistanceHeuristic implements Heuristic {

    private static final String GOAL_STATE = "012345678";
    private static final int GRID_SIZE = 3;
    private static final int TOTAL_TILES = GRID_SIZE * GRID_SIZE;

    // Flattened target index lookup table for tiles 0 through 8
    private static final int[] GOAL_INDICES = new int[TOTAL_TILES];

    static {
        // Pre-compute original position indices for instant lookup
        for (int index = 0; index < TOTAL_TILES; index++) {
            int tile = Character.getNumericValue(GOAL_STATE.charAt(index));
            GOAL_INDICES[tile] = index;
        }
    }

    /**
     * Calculates the sum of Manhattan distances for all non-blank tiles.
     * 
     * @param state The puzzle state representation string (e.g. "123456780")
     * @return Cumulative Manhattan distance to the goal state
     */
    @Override
    public int calculate(String state) {
        if (state == null || state.length() != TOTAL_TILES) {
            throw new IllegalArgumentException("State must be a non-null string of length " + TOTAL_TILES);
        }

        int totalDistance = 0;

        for (int currentIndex = 0; currentIndex < TOTAL_TILES; currentIndex++) {
            int tile = state.charAt(currentIndex) - '0';

            // Skip the empty space (tile 0)
            if (tile == 0) {
                continue;
            }

            int goalIndex = GOAL_INDICES[tile];

            // Convert 1D string indices into 2D grid coordinates
            int currentRow = currentIndex / GRID_SIZE;
            int currentCol = currentIndex % GRID_SIZE;
            int goalRow = goalIndex / GRID_SIZE;
            int goalCol = goalIndex % GRID_SIZE;

            totalDistance += Math.abs(currentRow - goalRow) + Math.abs(currentCol - goalCol);
        }

        return totalDistance;
    }

    @Override
    public String getName() {
        return "H2 (Manhattan Distance)";
    }
}