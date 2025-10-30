/**
 * Name: Nucleus Shrestha
 * Date: 25 September 2025
 * CSC 202
 * Project 1-CaveMaze.java
 * <p>
 * Incomplete class that models a maze of caves for the "Hunt the Wumpus" game.
 * The maze of caves is defined in a file where each line is the cave number, the number of
 * adjacent caves followed by the cave number of each adjacent cave and the
 * name of the cave. The caves will be stored in an ArrayList where the index of
 * the cave in the list is the cave's number. It is assumed that the file is valid.
 * <p>
 * One cave will be initialized to store CaveContents.BAT.
 * One cave will be initialized to store CaveContents.PIT.
 * Two caves will be initialized to store CaveContents.TREASURE_BOX_FULL
 * Randomly one to three caves will be initialized to store CaveContents.WUMPUS_FREE.
 * <p>
 * ADDITION TO GAME: (Describe your addition to try to earn an A or A-)
 *
 * @author Dave Reed
 * @revised by Diane Mueller
 * <p>
 * Document Assistance(who and describe what assistance was given or received; if no assistance, declare that fact):
 *
 * I got help from Himanshu while working on the strategies for the move, toss,
 * and showLocation methods. We spent some time thinking about
 * the tricky part with the bats and how to handle each cases for when the player
 * and the bats are in the same cave and when bat's relocated
 * random cave and player cave are same and so on.
 *
 *
 * For the move method, Professor Mueller helped me fix an issue where I was just
 * returning a string instead of storing it in the message variable first.
 * This caused the wrong output to show up in the terminal, and we corrected
 * it so the messages displayed properly.
 *
 * For the showLocation method, Professor Mueller helped me debug and reorganize
 * the code to match the sample output. At first I was only checking for
 * WUMPUS_FREE, but I learned that I should also check for WUMPUS_CAPTURED.
 * I also was accidentally adding the same warning more than once, and Professor Mueller
 * pointed out that I should check the ArrayList before adding duplicates.
 *
 * For the toss method, Professor Mueller worked with me on arranging the code
 * so the output matched the sample output we were given, and to make sure the
 * messages printed in the right order.
 *
 * CodeHelp helped me debug the IndexOutOfBoundException in toss() method. I was not checking if the tunnel
 * provided was valid or not. Since, I was not checking the validity of the tunnel,
 * I got an error while doing some tests.
 */

import java.util.*;
import java.io.*;

public class CaveMaze {
    private static final int START_CAVE_NUM = 0;  //the cave the player starts from
    private static final int MIN_NUM_CAVES = 8;   //minimum number of caves to accommodate contents

    private Random randGen = new Random();   //used to generate random numbers

    private Cave currentCave;           // The cave the player is in
    private final ArrayList<Cave> caveList;      // All of the caves in the maze
    private int numWumpi;                  // Number of uncaught wumpi in the cave maze

    /**
     * Constructs a CaveMaze from the data found in a file.
     *
     * @param filename the name of the cave data file
     * @throws FileNotFoundException if the filename received as a parameter
     *   is not found in the project.
     */
    public CaveMaze(String filename) throws FileNotFoundException {
        Scanner infile = new Scanner(new File(filename));

        this.caveList = new ArrayList<Cave>();

        while (infile.hasNextLine()) {
            int caveNum = infile.nextInt();
            int numAdj = infile.nextInt();
            int[] adjCaveList = new int[numAdj];
            for (int a = 0; a < adjCaveList.length; a++) {
                adjCaveList[a] = infile.nextInt();
            }
            String caveName = infile.nextLine().trim();
            Cave cave = new Cave(caveName, adjCaveList);
            this.caveList.add(caveNum, cave);
        }
        infile.close();

        if (this.caveList.size() < MIN_NUM_CAVES) {
            System.out.println("Data file fails to have enough caves to continue the game.");
            System.out.println("Teminating execution.");
            System.exit(0);
        }

        // Initialize the current cave
        this.currentCave = caveList.get(START_CAVE_NUM);
        this.currentCave.markAsVisited();

        // Pits
        int index = findRandomEmptyCave();
        caveList.get(index).setCaveContents(CaveContents.PIT);


        // Bats
        index = findRandomEmptyCave();
        caveList.get(index).setCaveContents(CaveContents.BATS);

        // Two Treasure Boxes
        for (int i = 0; i < 2; i++) {
            index = findRandomEmptyCave();
            caveList.get(index).setCaveContents(CaveContents.TREASURE_BOX_FULL);
        }


        // Wumpii
        numWumpi = randGen.nextInt(3) + 1;
        for (int i = 0; i < numWumpi; i++) {
            index = findRandomEmptyCave();
            caveList.get(index).setCaveContents(CaveContents.WUMPUS_FREE);
        }


    }

    /**
     * Helper method to find a random empty cave.
     *
     * @return the index of a random cave.
     */
    private int findRandomEmptyCave() {
        int index;
        do {
            index = randGen.nextInt(caveList.size());
        } while (index == START_CAVE_NUM || caveList.get(index).getCaveContents() != CaveContents.EMPTY);
        return index;
    }

    /**
     * Moves the player from the current cave along the specified tunnel, marking
     * the new cave as visited.
     *
     * @param tunnelNum - the number of the tunnel to be traversed (1 through number of tunnels)
     * @param player - the player roaming the caves
     *
     * @return the message depending on the tunnel and the result depending on the
     * contents of the cave the tunnel leads to.
     */
    public String move(int tunnelNum, Player player) {
        if (tunnelNum < 1 || tunnelNum > currentCave.getNumAdjacent()) {
            return "There is no tunnel number " + tunnelNum;
        } else {
            int caveNum = currentCave.getAdjCaveNumber(tunnelNum);
            currentCave = caveList.get(caveNum);
            currentCave.markAsVisited();
            String message = "Moving down tunnel " + tunnelNum + "...";

            CaveContents contents = currentCave.getCaveContents();

            if (contents == CaveContents.WUMPUS_FREE) {
                player.die();
                message += "\nYou've entered a cave with a wumpus...CHOMP CHOMP CHOMP!";
            } else if (contents == CaveContents.PIT) {
                player.die();
                message += "\nYou've fallen into the bottomless pit!";
            } else if (contents == CaveContents.BATS) {
                int batsIndex = caveNum;

                // Drop the player into a random empty cave
                int playerNewIndex = findRandomEmptyCave();
                currentCave = caveList.get(playerNewIndex);
                currentCave.markAsVisited();

                caveList.get(batsIndex).setCaveContents(CaveContents.EMPTY); // Remove the bats from the cave
                // Place bats into a different empty cave
                int batsNewIndex;
                do {
                    batsNewIndex = findRandomEmptyCave();
                } while (batsNewIndex == playerNewIndex || batsIndex == batsNewIndex);

                caveList.get(batsNewIndex).setCaveContents(CaveContents.BATS);

                message += "\nThe bats dropped you into another cave!";

            } else if (contents == CaveContents.TREASURE_BOX_FULL) {
                player.addGrenades(CaveContents.NUM_GRENADES_IN_TREASURE_BOX);
                currentCave.setCaveContents(CaveContents.TREASURE_BOX_EMPTY);
                message += "\nYou found a treasure box with " + CaveContents.NUM_GRENADES_IN_TREASURE_BOX +
                        " stun grenades!";
            }

            return message;
        }
    }

    /**
     * Attempts to toss a stun grenade through the specified tunnel. The message returned
     * depends on whether the tunnel number is valid, whether the player actually had
     * any grenades, and finally the contents of the bombed cave.
     *
     * @param tunnelNum - the number of the tunnel(1 through number of tunnels) leading to cave to be bombed
     * @param player - the player roaming the caves
     *
     * @return the message depending on the validity of the tunnel number, whether the player
     * had any grenades, the contents of the bombed cave.
     */
    public String toss(int tunnelNum, Player player) {
        if (tunnelNum < 1 || tunnelNum > currentCave.getNumAdjacent()) {
            return "There is no tunnel number " + tunnelNum;
        }

        if (player.getNumGrenades() <= 0) {
            return "You have no stun grenades to throw!";
        } else {
            player.throwGrenade();
        }

        int caveNum = currentCave.getAdjCaveNumber(tunnelNum);
        Cave caveTarget = caveList.get(caveNum);
        CaveContents contents = caveTarget.getCaveContents();

        if (contents == CaveContents.WUMPUS_FREE) {
            caveTarget.setCaveContents(CaveContents.WUMPUS_CAPTURED);
            numWumpi--;
            return "You caught a wumpus!";
        } else if (contents == CaveContents.WUMPUS_CAPTURED) {
            return "What a waste. That wumpus was already captured!";
        } else if (contents == CaveContents.BATS) {

            // Remove bats from the cave
            caveTarget.setCaveContents(CaveContents.EMPTY);

            //Place bats into another empty cave
            int batsNewIndex;
            do {
                batsNewIndex = findRandomEmptyCave();
            } while (batsNewIndex == caveNum);


            if (batsNewIndex == caveList.indexOf(currentCave)) {
                int playerNewIndex = findRandomEmptyCave();
                currentCave = caveList.get(playerNewIndex);
                currentCave.markAsVisited();

                int batsFinalIndex;
                do {
                    batsFinalIndex = findRandomEmptyCave();
                } while (batsFinalIndex == playerNewIndex || batsFinalIndex == caveNum);
                caveList.get(batsFinalIndex).setCaveContents(CaveContents.BATS);

                return "Missed, dagnabit!\nThe bats were startled and flew to another cave!" +
                        "\nHow unfortunate, the bats landed in your cave!" + "\nThe bats have dropped you into another cave!";
            } else {
                caveList.get(batsNewIndex).setCaveContents(CaveContents.BATS);
                return "Missed, dagnabit!\nThe bats were startled and flew to another cave!";
            }
        } else {

            return "Missed, dagnabit!";
        }
    }

    /**
     * Displays the current cave name and the names of adjacent caves. Caves that
     * have not yet been visited are displayed as "unknown". It also gives clues
     * warning of the wumpus, bats, or pit in a cave that can be reached via a tunnel
     * from the current cave.
     *
     * @return the message giving the current location and clues from the adjacent caves
     */
    public String showLocation() {
        String message = "You are currently in " + this.currentCave.getCaveName();
        ArrayList<String> warnings = new ArrayList<String>();


        for (int i = 1; i <= currentCave.getNumAdjacent(); i++) {
            int caveNum = currentCave.getAdjCaveNumber(i);
            Cave adjCave = caveList.get(caveNum);
            message += "\n    (" + i + ") " + adjCave.getCaveName();
            CaveContents contents = adjCave.getCaveContents();

            if (contents == CaveContents.WUMPUS_FREE || contents == CaveContents.WUMPUS_CAPTURED) {
                if (!warnings.contains("You smell an awful stench coming from somewhere nearby")) {
                    warnings.add("You smell an awful stench coming from somewhere nearby");
                }
            } else if (contents == CaveContents.PIT) {
                warnings.add("You feel a draft coming from one of the tunnels.");
            } else if (contents == CaveContents.BATS) {
                warnings.add("You hear the flapping of the wings close by.");
            }
        }

        Collections.shuffle(warnings);

        for (String w : warnings) {
            message = message + "\n" + w;
        }
        return message;
    }


    /**
     * Reports the number of wumpi remaining in the maze
     *
     * @return the number of wumpi remaining in the maze
     */
    public int getNumWumpi() {
        return numWumpi;
    }

    /**
     * Reports whether there are any wumpi remaining.
     *
     * @return true if there is still a wumpus in some cave
     */
    public boolean stillWumpi() {
        return (numWumpi != 0);
    }


    // returns a string with one cave's information per line
    // Prof Mueller used this to help in debugging her program!
    private String cavesAsString() {
        String caveInfo = "";
        for (int i = 0; i < caveList.size(); i++) {
            caveInfo = caveInfo + "Cave Num: " + i + ", " + caveList.get(i) + "\n";

        }
        return caveInfo;
    }
}