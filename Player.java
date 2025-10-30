/**
 * Name: Nucleus Shrestha
 * Date: 15 September 2025
 * CSC 202
 * Project 1-Player.java
 * <p>
 * Player defines the "human" player for the Hunt the Wumpus game.
 * The player gets a certain number of grenades for every wumpus in the maze.
 * <p>
 * Document Assistance(who and describe what assistance was given or received; if no assistance, declare that fact):
 * No Assistance
 */
public class Player {
    public static final int NUM_GRENADES_PER_WUMPI = 2; // number of stun grenades given for each wumpus in the maze

    private int numGrenades; // the current number of stun grenades this player has
    private boolean alive; // whether this player is alive or not

    /**
     * Creates a Player who starts alive with a supply of grenades.
     * The Player receives NUM_GRENADES_PER_WUMPI grenades for each wumpus in the game.
     *
     * @param numWumpi - Total Number of Wumpi in the maze.
     */
    public Player(int numWumpi) {
        this.alive = true;
        this.numGrenades = numWumpi * NUM_GRENADES_PER_WUMPI;
    }

    /**
     * @return - Whether the player is alive or dead.
     */
    public boolean isAlive() {
        return (this.alive);
    }

    /**
     * Sets the player's alive status to false.
     */
    public void die() {
        this.alive = false;
    }

    /**
     * @return - The player's current number of grenades.
     */
    public int getNumGrenades() {
        return this.numGrenades;
    }

    /**
     * Increases the player's number of grenades with number received as a parameter.
     *
     * @param additionalGrenades - Number of Grenades to add to player's grenades.
     */
    public void addGrenades(int additionalGrenades) {
        this.numGrenades = this.numGrenades + additionalGrenades;
    }

    /**
     * @return - If the player is alive and has enough grenades, reduces the
     * number of grenades by 1 and returns true else false.
     */
    public boolean throwGrenade() {
        if (this.alive && this.numGrenades > 0) {
            this.numGrenades--;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return - A string representation if a Player is alive and the number of grenades the player holds.
     */
    public String toString() {
        return "alive: " + this.alive + ", num grenades: " + this.numGrenades;
    }


}
