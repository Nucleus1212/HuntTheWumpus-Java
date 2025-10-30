/**
 * Enumeration class CaveContents - the possible contents of a "Hunt the Wumpus" cave.
 *   @author Dave Reed
 *   @revised Diane Mueller
 *   @version 11/4/17
 */
public enum CaveContents {
    EMPTY, WUMPUS_FREE, WUMPUS_CAPTURED, BATS, PIT, TREASURE_BOX_FULL, TREASURE_BOX_EMPTY;
    
    public static final int NUM_GRENADES_IN_TREASURE_BOX = 2;
}