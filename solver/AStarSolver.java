package solver;

import heuristic.Heuristic;
import model.Node;
import model.SearchResult;
import puzzle.PuzzleState;

import java.util.*;

/**
 * Executes an A* graph search traversal to solve 8-puzzle instances.
 * 
 * Algorithm Characteristics:
 * - Uses a priority queue ordered by total evaluation score ($f = g + h$), depth level ($g$), and node creation ID.
 * - Maintains a visited set (`closed`) to prevent redundant state node expansions.
 * - Utilizes parent tracking maps to reconstruct optimal solution trajectories.
 */
public class AStarSolver {

    private final Heuristic heuristic;

    /**
     * Initializes an A* solver configured with a chosen heuristic function.
     *
     * @param heuristic Evaluation heuristic (e.g. H1 Misplaced Tiles, H2 Manhattan Distance)
     */
    public AStarSolver(Heuristic heuristic) {
        if (heuristic == null) {
            throw new IllegalArgumentException("Heuristic function reference cannot be null.");
        }
        this.heuristic = heuristic;
    }

    /**
     * Runs the A* graph search algorithm to discover the shortest solution path.
     *
     * @param start Flat string layout of the initial 8-puzzle board state
     * @return SearchResult containing optimal path and execution metrics; null if unsolvable
     */
    public SearchResult solve(String start) {
        if (!PuzzleState.isValidState(start)) {
            throw new IllegalArgumentException("Initial board configuration is invalid.");
        }

        // Fast return for unsolvable puzzle states
        if (!PuzzleState.isSolvable(start)) {
            return null;
        }

        long startTimeNano = System.nanoTime();

        // Priority Queue tie-breaker order: f-value -> g-value -> insertion ID
        Comparator<Node> nodeComparator = Comparator
                .comparingInt(Node::getF)
                .thenComparingInt(Node::getG)
                .thenComparingLong(Node::getId);

        PriorityQueue<Node> openSet = new PriorityQueue<>(nodeComparator);
        Map<String, Integer> bestGScore = new HashMap<>();
        Map<String, String> parentMap = new HashMap<>();
        Set<String> closedSet = new HashSet<>();

        long pushSequenceId = 0;
        int nodesGenerated = 0;

        // Seed search root node
        int initialH = heuristic.calculate(start);
        openSet.add(new Node(start, 0, initialH, pushSequenceId++));
        bestGScore.put(start, 0);
        parentMap.put(start, null);
        nodesGenerated++;

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            String currentState = currentNode.getState();

            // Ignore duplicate entries popped from openSet
            if (closedSet.contains(currentState)) {
                continue;
            }
            closedSet.add(currentState);

            // Goal test upon node expansion
            if (currentState.equals(PuzzleState.GOAL_STATE)) {
                long durationNano = System.nanoTime() - startTimeNano;
                double executionTimeMs = durationNano / 1_000_000.0;
                List<String> solutionPath = reconstructPath(parentMap, currentState);
                return new SearchResult(solutionPath, nodesGenerated, executionTimeMs);
            }

            int currentG = bestGScore.get(currentState);

            for (String neighborState : PuzzleState.getNeighbors(currentState)) {
                if (closedSet.contains(neighborState)) {
                    continue;
                }

                nodesGenerated++;
                int tentativeG = currentG + 1;
                Integer knownG = bestGScore.get(neighborState);

                // Relax edge if shorter path discovered
                if (knownG == null || tentativeG < knownG) {
                    bestGScore.put(neighborState, tentativeG);
                    parentMap.put(neighborState, currentState);

                    int h = heuristic.calculate(neighborState);
                    openSet.add(new Node(neighborState, tentativeG, tentativeG + h, pushSequenceId++));
                }
            }
        }

        return null; // Search exhausted without reaching goal
    }

    /**
     * Backtracks parent references to construct the full path sequence from initial state to goal.
     */
    private List<String> reconstructPath(Map<String, String> parentMap, String goalState) {
        List<String> path = new ArrayList<>();
        String current = goalState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);
        return path;
    }
}