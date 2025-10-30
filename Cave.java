/**
 * Name: Nucleus Shrestha
 * Date: 20 September 2025
 * CSC 202
 * Project 1-Cave.java
 * <p>
 * Cave describes one cave in the cave maze including its name,
 * the caves that can be reaches via a tunnel from this cave,
 * whether the player has visited this maze yet, and the contents
 * of the cave.
 * <p>
 * Document Assistance(who and describe what assistance was given or received; if no assistance, declare that fact):
 * No Assistance
 */

import java.util.*;

public class Cave {

    // Instance Variables
    private String name;

    private int[] tunnel;

    private CaveContents contents;

    private boolean isVisited;

    /**
     * Creates a new Cave with the given name and list of adjacent caves.
     * The Cave is initialized with EMPTY contents and marked as not visited.
     *
     * @param name - the name of the cave
     * @param adjCaveArray - the array of cave numbers that are connected
     */
    public Cave(String name, int[] adjCaveArray) {
        this.name = name;
        this.tunnel = adjCaveArray;
        contents = CaveContents.EMPTY;
        isVisited = false;
    }

    /**
     * Returns the number of the cave that can be reached with the given tunnel index.
     * If the tunnel index is invalid, this method return -1.
     *
     * @param tunnelNum - the index od the tunnel to follow
     * @return - the cave number to be reached or -1 if the index is invalid
     */
    public int getAdjCaveNumber(int tunnelNum) {
        if (tunnel == null || tunnelNum < 1 || tunnelNum > tunnel.length) {
            return -1;
        }

        return tunnel[tunnelNum - 1];

    }

    /**
     * Return the name of the cave associated if it has been visited.
     * If not visited, it returns "unknown".
     *
     * @return - the cave name if visited, otherwise "unknown".
     */
    public String getCaveName() {
        if (isVisited) {
            return name;
        } else {
            return "unknown";
        }
    }

    /**
     * Returns the contents of this cave.
     *
     * @return - the CaveContents value this cave contains.
     */
    public CaveContents getCaveContents() {
        return contents;
    }

    /**
     * Returns the number pf caves that can be reached through
     * tunnels from this cave.
     *
     * @return - the number of adjacent caves.
     */
    public int getNumAdjacent() {
        return tunnel.length;
    }

    /**
     * Marks this cave as visited by the player.
     */
    public void markAsVisited() {
        isVisited = true;
    }

    /**
     * Changes the contents of this cave to the contents received.
     * @param contents - the new contents of the cave.
     */
    public void setCaveContents(CaveContents contents) {
        this.contents = contents;
    }

    /**
     * Returns a string representation of this cave in the format:
     * Cave name: X, Adj Cave Array: X, Visited: X, Contents: X
     * @return - a string describing this cave
     */
    public String toString() {
        return "Cave name: " + name + ", Adj Cave Array: " + Arrays.toString(tunnel) + ", Visited: "
                + isVisited + ", Contents: " + contents;
    }


}
