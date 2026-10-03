import heuristic.ManhattanDistanceHeuristic;
import heuristic.MisplacedTilesHeuristic;
import model.SearchResult;
import puzzle.PuzzleState;
import solver.AStarSolver;
import util.InputReader;
import util.PuzzleGenerator;

import java.util.List;
import java.util.Scanner;

/**
 * CS 4200 Project 1 - 8-Puzzle Solver using A* Search
 * 
 * Main application entry point orchestrating CLI interaction:
 * - Solicits puzzle acquisition strategy (randomly generated vs. manual matrix input)
 * - Validates structural soundess and solvability (inversion parity)
 * - Solves the configuration using both H1 (Misplaced Tiles) and H2 (Manhattan Distance)
 * - Renders step-by-step path transitions and comparative search cost metrics
 */
public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== CS 4200 Project 1: 8-Puzzle Solver ===");
            System.out.println("Select Input Method:");
            System.out.println("[1] Random (Guaranteed Solvable)");
            System.out.println("[2] Manual Entry");
            
            int inputMethod = InputReader.readInt(scanner);
            String startState = acquireInitialState(scanner, inputMethod);

            if (startState == null) {
                return;
            }

            // Verify input validity
            if (!PuzzleState.isValidState(startState)) {
                System.out.println("Invalid puzzle state: Must contain digits 0-8 exactly once (0 represents blank).");
                return;
            }

            System.out.println("\nInitial Board Configuration:");
            System.out.println(PuzzleState.toGridString(startState));

            // Verify solvability invariant via inversion parity
            if (!PuzzleState.isSolvable(startState)) {
                System.out.println("Puzzle is unsolvable.");
                return;
            }

            System.out.println("\nSelect Preferred Heuristic for Path Rendering:");
            System.out.println("[1] H1 (Misplaced Tiles)");
            System.out.println("[2] H2 (Manhattan Distance)");
            
            int heuristicChoice = InputReader.readInt(scanner);
            if (heuristicChoice != 1 && heuristicChoice != 2) {
                System.out.println("Invalid heuristic selection. Exiting program.");
                return;
            }

            // Execute A* search under both heuristics for performance evaluation
            AStarSolver solverH1 = new AStarSolver(new MisplacedTilesHeuristic());
            AStarSolver solverH2 = new AStarSolver(new ManhattanDistanceHeuristic());

            SearchResult resultH1 = solverH1.solve(startState);
            SearchResult resultH2 = solverH2.solve(startState);

            if (resultH1 == null || resultH2 == null) {
                System.out.println("No solution found.");
                return;
            }

            // Render step-by-step solution sequence for the requested heuristic
            SearchResult chosenResult = (heuristicChoice == 1) ? resultH1 : resultH2;
            renderSolutionSteps(chosenResult.getPath());

            // Output empirical comparative benchmark metrics
            printPerformanceComparison(resultH1, resultH2);
        }
    }

    /**
     * Reads or generates the starting state string based on user choice.
     */
    private static String acquireInitialState(Scanner scanner, int inputMethod) {
        if (inputMethod == 1) {
            return PuzzleGenerator.generateRandom();
        } else if (inputMethod == 2) {
            int[][] puzzleGrid = InputReader.readPuzzle(scanner);
            return PuzzleState.toString(puzzleGrid);
        } else {
            System.out.println("Invalid input method selection. Please choose 1 or 2.");
            return null;
        }
    }

    /**
     * Prints each step transition of the solution trajectory to stdout.
     */
    private static void renderSolutionSteps(List<String> solutionPath) {
        System.out.println("\nSolution Found!");
        System.out.println("Total Moves (Path Cost): " + (solutionPath.size() - 1));

        for (int step = 1; step < solutionPath.size(); step++) {
            System.out.println("\nStep " + step + ":");
            System.out.println(PuzzleState.toGridString(solutionPath.get(step)));
        }
    }

    /**
     * Prints comparative performance summary metrics for both heuristic searches.
     */
    private static void printPerformanceComparison(SearchResult r1, SearchResult r2) {
        System.out.println("\n----------------------------------------");
        System.out.println("      PERFORMANCE BENCHMARK REPORT      ");
        System.out.println("----------------------------------------");
        System.out.println("H1 Search Cost (Nodes Generated): " + r1.getNodesGenerated());
        System.out.println("H2 Search Cost (Nodes Generated): " + r2.getNodesGenerated());
        System.out.printf("H1 Execution Time:               %.3f ms%n", r1.getTimeMs());
        System.out.printf("H2 Execution Time:               %.3f ms%n", r2.getTimeMs());
        System.out.println("----------------------------------------");
    }
}