package heuristic;

/**
 * Description: Interface for heuristic functions used in A* search.
 * Heuristics will be used to estimate the cost to reach the goal state from a given state.
 */
public interface Heuristic {

    /**
     * Description: Calculates the heuristic value for the given puzzle state.
     * 
     * @param state The puzzle state to evaluate
     * @return Estimated cost to reach the goal
     */
    int calculate(String state);

    /**
     * @return The name of this heuristic.
     */
    String getName();
}