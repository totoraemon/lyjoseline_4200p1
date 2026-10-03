package model;

import java.util.Objects;

/**
 * Encapsulates a search state within the $A^*$ pathfinding tree.
 * 
 * Core state representation:
 * - Tracks path cost ($g$), estimated total cost ($f$), and a unique sequence ID.
 * - Sequence IDs guarantee deterministic tie-breaking inside PriorityQueue implementations.
 */
public class Node implements Comparable<Node> {

    private final String state;
    private final int g;
    private final int f;
    private final long id;

    /**
     * Initializes a search tree node with path metrics and ordering metadata.
     *
     * @param state Flat string layout representing the current board
     * @param g     Path cost accumulated from the initial node (depth)
     * @param f     Evaluation score combining exact cost and heuristic ($f = g + h$)
     * @param id    Monotonically increasing token used to resolve equal $f$-score ties
     */
    public Node(String state, int g, int f, long id) {
        if (state == null) {
            throw new IllegalArgumentException("Board state representation cannot be null.");
        }
        this.state = state;
        this.g = g;
        this.f = f;
        this.id = id;
    }

    /**
     * Gets the string layout of the current puzzle state.
     */
    public String getState() {
        return state;
    }

    /**
     * Gets the path cost accumulated from the root node ($g$-score).
     */
    public int getG() {
        return g;
    }

    /**
     * Gets the projected total trajectory cost ($f$-score).
     */
    public int getF() {
        return f;
    }

    /**
     * Gets the unique identifier assigned during creation.
     */
    public long getId() {
        return id;
    }

    /**
     * Priority comparison protocol for frontier queues:
     * 1. Primary ordering: Lower $f$-score (closer estimated path to goal).
     * 2. Secondary ordering: Lower $id$ (FIFO tie-breaking).
     */
    @Override
    public int compareTo(Node other) {
        int fComparison = Integer.compare(this.f, other.f);
        if (fComparison != 0) {
            return fComparison;
        }
        return Long.compare(this.id, other.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node other = (Node) obj;
        return Objects.equals(this.state, other.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }

    @Override
    public String toString() {
        return String.format("Node{state='%s', g=%d, f=%d, id=%d}", state, g, f, id);
    }
}