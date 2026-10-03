package model;

import java.util.Collections;

import java.util.List;

/**
 * Encapsulates the quantitative outputs and execution metrics of a search algorithm.
 * 
 * Core responsibilities:
 * - Stores the ordered sequence of board states leading from start to goal.
 * - Tracks runtime performance metrics (expanded/generated nodes count and execution duration).
 */
public class SearchResult {

    private final List<String> path;
    private final int nodesGenerated;
    private final double executionTimeMs;

    /**
     * Initializes a search performance container.
     *
     * @param path            Ordered sequence of state strings from start to goal (null or empty if unsolvable)
     * @param nodesGenerated  Total count of search tree nodes created during evaluation
     * @param executionTimeMs Total runtime taken to execute the search algorithm in milliseconds
     */
    public SearchResult(List<String> path, int nodesGenerated, double executionTimeMs) {
        // Defensive copy to guarantee immutability of the solution path
        this.path = (path != null) ? List.copyOf(path) : Collections.emptyList();
        this.nodesGenerated = Math.max(0, nodesGenerated);
        this.executionTimeMs = Math.max(0.0, executionTimeMs);
    }

    /**
     * Indicates whether a solution trajectory was successfully identified.
     *
     * @return true if a non-empty path exists; false otherwise
     */
    public boolean isSolvable() {
        return !path.isEmpty();
    }

    /**
     * Returns an unmodifiable view of the complete solution sequence.
     */
    public List<String> getPath() {
        return path;
    }

    /**
     * Returns the total step count (depth) required to reach the goal.
     * 
     * @return Number of moves in the path, or 0 if unsolvable
     */
    public int getSolutionCost() {
        return path.isEmpty() ? 0 : path.size() - 1;
    }

    /**
     * Returns the total volume of generated nodes across the entire search lifecycle.
     */
    public int getNodesGenerated() {
        return nodesGenerated;
    }

    /**
     * Returns the wall-clock execution duration recorded in milliseconds.
     */
    public double getTimeMs() {
        return executionTimeMs;
    }

    @Override
    public String toString() {
        return String.format(
            "SearchResult{solvable=%b, solutionCost=%d, nodesGenerated=%d, timeMs=%.3f ms}",
            isSolvable(), getSolutionCost(), nodesGenerated, executionTimeMs
        );
    }
}