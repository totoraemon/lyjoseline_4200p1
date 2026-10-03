package util;

import java.util.Objects;
import java.util.Scanner;

/**
 * Utility functions for parsing user input streams and building puzzle matrices.
 */
public final class InputReader {

    private static final int GRID_SIZE = 3;

    // Suppress default constructor to prevent instantiation
    private InputReader() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    /**
     * Reads the next valid integer token from the provided stream, discarding non-integer tokens.
     *
     * @param scanner Input source stream
     * @return Next valid parsed integer
     */
    public static int readInt(Scanner scanner) {
        Objects.requireNonNull(scanner, "Scanner instance cannot be null.");

        while (!scanner.hasNextInt()) {
            System.out.print("Invalid integer input. Please enter a valid number: ");
            scanner.next(); // Discard non-integer token
        }
        return scanner.nextInt();
    }

    /**
     * Parses a 3x3 matrix from standard input token stream.
     *
     * @param scanner Input source stream
     * @return 2D integer array containing the 3x3 grid values
     */
    public static int[][] readPuzzle(Scanner scanner) {
        Objects.requireNonNull(scanner, "Scanner instance cannot be null.");

        int[][] puzzle = new int[GRID_SIZE][GRID_SIZE];
        System.out.println("Enter 9 integers (0-8) representing the 3x3 puzzle:");

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                puzzle[row][col] = readInt(scanner);
            }
        }
        return puzzle;
    }
}