package scoring;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import servlets.DBConnection;
import servlets.GameLog;
import servlets.Roster;


public class Parser
{
    /**
     * The constructor. Initializes the local variables
     * 
     * @param theScore
     * @throws ClassNotFoundException
     */
    public Parser(String theScore, String outputFile, ArrayList<Integer> theLineup, ArrayList<Sub> theSubs)
            throws IOException, SQLException, InstantiationException,
            IllegalAccessException, ClassNotFoundException
    {
        score = theScore;
        writer = new StringWriter(outputFile);
        lineup = theLineup;
        subs = theSubs;
        state = 0;
        index = 0;
        outs = 0;
        inning = 1;
        batterList = new ArrayList<HashMap<String, int[]>>();
        subbedOutBatterList = new ArrayList<HashMap<String, int[]>>();
        batterCount = -1; // because batterCount is incremented at the start of
        // each AB and the first batter in the line up is 0.
        x = new HashMap<String, Integer>();
        y = new HashMap<String, Integer>();
        z = new HashMap<String, Integer>();
        h = new HashMap<String, Integer>();
        conn = DBConnection.getConnection();
        roster = new Roster(conn);
        playerStats = new GameLog(conn);
        previousCharacters = new ArrayList<String>();

        gameID = ((Integer) lineup.get(0)).intValue();
        lineup.remove(0);
        int[] tempArray;
        String tempString = "";
        HashMap<String, int[]> tempMap;
        for (int i = 0; i < lineup.size(); i++) {
            tempArray = new int[92];
            for (int j = 0; j < 92; j++) {
                tempArray[j] = 0;
            }
            tempArray[0] = ((Integer) lineup.get(i)).intValue();
            tempArray[1] = gameID;
            tempString = roster.getFirstName(((Integer) lineup.get(i))
                    .intValue())
                    + " "
                    + roster.getLastName(((Integer) lineup.get(i)).intValue());
            tempMap = new HashMap<String, int[]>();
            tempMap.put(tempString, tempArray);
            batterList.add(tempMap);
        }
        for (int i = 0; i < batterList.size(); i++) {
            batterList.get(i).values().iterator().next()[87] = i + 1;
        }
        // check for subs
        for (Sub sub : subs) {
            if (sub.getInning() == inning) {
                for (int i = 0; i < batterList.size(); ++i) {
                    if (batterList.get(i).keySet().iterator().next().equals(sub.getPlayerFirstName() + " " + sub.getPlayerLastName())) {
                        int[] tempArray2 = new int[92];
                        for (int j = 0; j < 92; j++) {
                            tempArray2[j] = 0;
                        }
                        tempArray2[0] = sub.getSubID();
                        tempArray2[1] = gameID;
                        tempArray2[87] = batterList.get(i).values().iterator().next()[87];
                        HashMap<String, int[]> tempMap2 = new HashMap<String, int[]>();
                        tempMap2.put(sub.getSubFirstName() + " " + sub.getSubLastName(), tempArray2);
                        subbedOutBatterList.add(batterList.get(i));
                        batterList.set(i, tempMap2);
                        break;
                    }
                }
            }
        }
    }

    /**
     * This method includes a loop which calls examineCharacter on each
     * character
     * 
     * @param outputFile
     * @throws SQLException
     * @throws FileNotFoundException
     */
    public void parseScore() throws SQLException
    {
        writer.write("Beginning of first inning:");
        writer.write("\n");
        printScore();
        String currentCharacter = "";
        while (true) {           
            previousCharacters.add(currentCharacter);
            currentCharacter = next();
            if (examineCharacter(currentCharacter) == -1) {
                if (batterCount == batterList.size() - 1) {
                    batterCount = 0;
                }
                else {
                    batterCount++;
                }
                calculateRBI();
                updateBaseRunners();
                printSituation();
                break;
            }
            index++;
        }
        state = 0;
        closeWriter();
        playerStats.enterStats(batterList, subbedOutBatterList);
        playerStats.close();
    }

    private int examineCharacter(String c)
    {
        if (c.equals(END)) {
            return -1;
        }
        if (Symbols.AB_ACTIONS.contains(c)) {
            parseABAction(c);
        }
        if (Symbols.NONAB_ACTIONS.contains(c)) {
            // we don't use these
        }
        if (Symbols.DESCRIPTORS_P1.contains(c)) {
            parseP1Descriptor(c);
        }
        if (Symbols.DESCRIPTORS_P2.contains(c)) {
            parseP2Descriptor(c);
        }
        if (Symbols.DESCRIPTORS_P3.contains(c)) {
            parseP3Descriptor(c);
        }
        if (Symbols.BASERUNNERS.contains(c)) {
            parseBaseRunner(c);
        }
        if (Symbols.BASES.contains(c)) {
            parseBase(c);
        }
        if (c.equals(Symbols.BASERUNNERADVANCE)) {
            parseBaseRunnerAdvance(c);
        }
        if (c.equals(Symbols.END_OF_INNING)) {
            parsePrematureEndOfInning(c);
        }       
        if (c.equals(Symbols.EXTRA_INNING)) {
            parseExtraInning(c);
        }
        return 0;
    }

    private void parseABAction(String c)
    {
        if (batterCount == batterList.size() - 1) {
            batterCount = 0;
        }
        else {
            batterCount++;
        }
        calculateRBI();
        updateBaseRunners();
        printSituation();
        previousCharacters.clear();

        // initial sate
        if (state == 0 || state == 1 || state == 3) {
            if (c.equals(Symbols.SINGLE)) {
                writer.write(batterList.get(batterCount).keySet().iterator()
                        .next()
                        + " singled");
                batterList.get(batterCount).values().iterator().next()[2]++;
                batterList.get(batterCount).values().iterator().next()[3]++;
                batterList.get(batterCount).values().iterator().next()[8]++;
                if (isRISP()) {
                    batterList.get(batterCount).values().iterator().next()[15]++;
                }
                if (isRISP() && (outs == 2)) {
                    batterList.get(batterCount).values().iterator().next()[17]++;
                }
                if (isRISP()) {
                    batterList.get(batterCount).values().iterator().next()[16]++;
                }
                if (isRISP() && (outs == 2)) {
                    batterList.get(batterCount).values().iterator().next()[18]++;
                }
                h
                        .put(batterList.get(batterCount).keySet().iterator()
                                .next(), 1);
            }
            else if (c.equals(Symbols.DOUBLE)) {
                writer.write(batterList.get(batterCount).keySet().iterator()
                        .next()
                        + " doubled");
                batterList.get(batterCount).values().iterator().next()[2]++;
                batterList.get(batterCount).values().iterator().next()[3]++;
                batterList.get(batterCount).values().iterator().next()[5]++;
                batterList.get(batterCount).values().iterator().next()[8] = batterList
                        .get(batterCount).values().iterator().next()[8] + 2;
                if (isRISP()) {
                    batterList.get(batterCount).values().iterator().next()[15]++;
                }
                if (isRISP() && (outs == 2)) {
                    batterList.get(batterCount).values().iterator().next()[17]++;
                }
                if (isRISP()) {
                    batterList.get(batterCount).values().iterator().next()[16]++;
                }
                if (isRISP() && (outs == 2)) {
                    batterList.get(batterCount).values().iterator().next()[18]++;
                }
                h
                        .put(batterList.get(batterCount).keySet().iterator()
                                .next(), 2);
            }
            else if (c.equals(Symbols.TRIPLE)) {
                writer.write(batterList.get(batterCount).keySet().iterator()
                        .next()
                        + " tripled");
                batterList.get(batterCount).values().iterator().next()[2]++;
                batterList.get(batterCount).values().iterator().next()[3]++;
                batterList.get(batterCount).values().iterator().next()[6]++;
                batterList.get(batterCount).values().iterator().next()[8] = batterList
                        .get(batterCount).values().iterator().next()[8] + 3;
                if (isRISP()) {
                    batterList.get(batterCount).values().iterator().next()[15]++;
                }
                if (isRISP() && (outs == 2)) {
                    batterList.get(batterCount).values().iterator().next()[17]++;
                }
                if (isRISP()) {
                    batterList.get(batterCount).values().iterator().next()[16]++;
                }
                if (isRISP() && (outs == 2)) {
                    batterList.get(batterCount).values().iterator().next()[18]++;
                }
                h
                        .put(batterList.get(batterCount).keySet().iterator()
                                .next(), 3);
            }
            else if (c.equals(Symbols.HOMERUN)) {
                writer.write(batterList.get(batterCount).keySet().iterator()
                        .next()
                        + " homered");
                batterList.get(batterCount).values().iterator().next()[2]++;
                batterList.get(batterCount).values().iterator().next()[3]++;
                batterList.get(batterCount).values().iterator().next()[7]++;
                batterList.get(batterCount).values().iterator().next()[8] = batterList
                        .get(batterCount).values().iterator().next()[8] + 4;
                runsScored++;
                batterList.get(batterCount).values().iterator().next()[9]++;
                if (!x.keySet().isEmpty()) {
                    batterList.get(batterCount).values().iterator().next()[9]++;
                }
                if (!y.keySet().isEmpty()) {
                    batterList.get(batterCount).values().iterator().next()[9]++;
                }
                if (!z.keySet().isEmpty()) {
                    batterList.get(batterCount).values().iterator().next()[9]++;
                }
                batterList.get(batterCount).values().iterator().next()[4]++;
                if (isRISP()) {
                    batterList.get(batterCount).values().iterator().next()[15]++;
                }
                if (isRISP() && (outs == 2)) {
                    batterList.get(batterCount).values().iterator().next()[17]++;
                }
                if (isRISP()) {
                    batterList.get(batterCount).values().iterator().next()[16]++;
                }
                if (isRISP() && (outs == 2)) {
                    batterList.get(batterCount).values().iterator().next()[18]++;
                }
                advanceAll(4);
            }
            else if (c.equals(Symbols.REACHONERROR)) {
                writer.write(batterList.get(batterCount).keySet().iterator()
                        .next()
                        + " reached on error");
                h
                        .put(batterList.get(batterCount).keySet().iterator()
                                .next(), 1);
                batterList.get(batterCount).values().iterator().next()[2]++;
                batterList.get(batterCount).values().iterator().next()[3]++;
                batterList.get(batterCount).values().iterator().next()[8]++;
                batterList.get(batterCount).values().iterator().next()[90]++;
                if (isRISP()) {
                    batterList.get(batterCount).values().iterator().next()[15]++;
                }
                if (isRISP() && (outs == 2)) {
                    batterList.get(batterCount).values().iterator().next()[17]++;
                }
                if (isRISP()) {
                    batterList.get(batterCount).values().iterator().next()[16]++;
                }
                if (isRISP() && (outs == 2)) {
                    batterList.get(batterCount).values().iterator().next()[18]++;
                }

            }
            else if (c.equals(Symbols.ERROR)) {
                writer.write(batterList.get(batterCount).keySet().iterator()
                        .next()
                        + " reached on error");
                h
                        .put(batterList.get(batterCount).keySet().iterator()
                                .next(), 1);
                batterList.get(batterCount).values().iterator().next()[2]++;
                batterList.get(batterCount).values().iterator().next()[91]++;
                if (isRISP()) {
                    batterList.get(batterCount).values().iterator().next()[15]++;
                }
                if (isRISP() && (outs == 2)) {
                    batterList.get(batterCount).values().iterator().next()[17]++;
                }
            }
            else if (c.equals(Symbols.OUT)) {
                writer.write(batterList.get(batterCount).keySet().iterator()
                        .next()
                        + " was out");
                batterList.get(batterCount).values().iterator().next()[2]++;
                if (isRISP()) {
                    batterList.get(batterCount).values().iterator().next()[15]++;
                }
                if (isRISP() && (outs == 2)) {
                    batterList.get(batterCount).values().iterator().next()[17]++;
                }
                outs++;
            }
            else if (c.equals(Symbols.FIELDERSCHOICE)) {
                writer.write(batterList.get(batterCount).keySet().iterator()
                        .next()
                        + " reached via fielders choice");
                batterList.get(batterCount).values().iterator().next()[2]++;
                batterList.get(batterCount).values().iterator().next()[13]++;
                if (isRISP()) {
                    batterList.get(batterCount).values().iterator().next()[15]++;
                }
                if (isRISP() && (outs == 2)) {
                    batterList.get(batterCount).values().iterator().next()[17]++;
                }
                h
                        .put(batterList.get(batterCount).keySet().iterator()
                                .next(), 1);
            }
            else if (c.equals(Symbols.SACRIFICE)) {
                writer.write(batterList.get(batterCount).keySet().iterator()
                        .next()
                        + " sacrificed");
                batterList.get(batterCount).values().iterator().next()[12]++;
                outs++;
            }
            else if (c.equals(Symbols.WALK)) {
                writer.write(batterList.get(batterCount).keySet().iterator()
                        .next()
                        + " walked.");
                h
                        .put(batterList.get(batterCount).keySet().iterator()
                                .next(), 1);
                batterList.get(batterCount).values().iterator().next()[10]++;
                batterList.get(batterCount).values().iterator().next()[8]++;
                if (!x.keySet().isEmpty() && !y.keySet().isEmpty()
                        && !z.keySet().isEmpty()) {
                    batterList.get(batterCount).values().iterator().next()[9]++;
                }
                advanceAllForced();
                h
                        .put(batterList.get(batterCount).keySet().iterator()
                                .next(), 1);
            }
            else if (c.equals(Symbols.HITBYPITCH)) {
                writer.write(batterList.get(batterCount).keySet().iterator()
                        .next()
                        + " was hit by pitch.");
                if (!x.keySet().isEmpty() && !y.keySet().isEmpty()
                        && !z.keySet().isEmpty()) {
                    batterList.get(batterCount).values().iterator().next()[9]++;
                }
                advanceAllForced();
                h
                        .put(batterList.get(batterCount).keySet().iterator()
                                .next(), 1);
            }
            else if (c.equals(Symbols.HOMERUNLIMIT)) {
                writer.write(batterList.get(batterCount).keySet().iterator()
                        .next()
                        + " homered for an out (limit reached)");
                batterList.get(batterCount).values().iterator().next()[2]++;
                batterList.get(batterCount).values().iterator().next()[88]++;
                outs++;
            }

            if (state == 0) {
                state = 1;
            }
            else if (state == 1) {
                // no state change
            }
            else if (state == 3) {
                state = 1;
            }
        }
    }

    private void parseP1Descriptor(String c)
    {
        if (state == 1 && c.equals(Symbols.STRIKEOUT)) {
            writer.write(" on a strikeout.");
            batterList.get(batterCount).values().iterator().next()[11]++;
            // no state change
        }
        if (state == 1 && !c.equals(Symbols.STRIKEOUT)) {
            state = 2;
            if (c.equals(Symbols.GROUNDBALL)) {
                writer.write(" on a groundball");
            }
            if (c.equals(Symbols.FLYBALL)) {
                writer.write(" on a flyball");
            }
            if (c.equals(Symbols.LINEDRIVE)) {
                writer.write(" on a linedrive");
            }
            if (c.equals(Symbols.BUNT)) {
                writer.write(" on a bunt");
            }
        }
    }

    private void parseP2Descriptor(String c)
    {
        if (state == 2) {
            state = 3;
            if (c.equals(Symbols.PITCHER)) {
                writer.write(" to the pitcher.");
                if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[19]++;
                }
                else if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[20]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[21]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[22]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[23]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[24]++;
                }
            }
            if (c.equals(Symbols.CATCHER)) {
                writer.write(" to the catcher.");
                if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[25]++;
                }
                else if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[26]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[27]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[28]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[29]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[30]++;
                }
            }
            if (c.equals(Symbols.FIRST)) {
                writer.write(" to first.");
                if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[31]++;
                }
                else if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[32]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[33]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[34]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[35]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[36]++;
                }
            }
            if (c.equals(Symbols.SECOND)) {
                writer.write(" to second.");
                if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[37]++;
                }
                else if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[38]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[39]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[40]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[41]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[42]++;
                }
            }
            if (c.equals(Symbols.THIRD)) {
                writer.write(" to third.");
                if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[43]++;
                }
                else if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[44]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[45]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[46]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[47]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[48]++;
                }
            }
            if (c.equals(Symbols.SHORTSTOP)) {
                writer.write(" to short.");
                if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[49]++;
                }
                else if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[50]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[51]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[52]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[53]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[54]++;
                }
            }
            if (c.equals(Symbols.LEFT)) {
                writer.write(" to left.");
                if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[55]++;
                }
                else if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[56]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[57]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[58]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[59]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[60]++;
                }
            }
            if (c.equals(Symbols.CENTER)) {
                writer.write(" to center.");
                if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[61]++;
                }
                else if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[62]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[63]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[64]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[65]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[66]++;
                }
            }
            if (c.equals(Symbols.RIGHT)) {
                writer.write(" to right.");
                if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[67]++;
                }
                else if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[68]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[69]++;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[70]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[71]++;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[72]++;
                }
            }
        }
    }

    private void parseP3Descriptor(String c)
    {
        if (state == 3) {
            // state stays the same
            if (c.equals(Symbols.CENTER)
                    && (previousCharacters.get(previousCharacters.size() - 1)
                            .equals(Symbols.LEFT))) {
                writer.write("center.");
                if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[73]++;
                    batterList.get(batterCount).values().iterator().next()[55]--;
                }
                else if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[74]++;
                    batterList.get(batterCount).values().iterator().next()[56]--;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[75]++;
                    batterList.get(batterCount).values().iterator().next()[57]--;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[76]++;
                    batterList.get(batterCount).values().iterator().next()[58]--;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[77]++;
                    batterList.get(batterCount).values().iterator().next()[59]--;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[78]++;
                    batterList.get(batterCount).values().iterator().next()[60]--;
                }
            }
            if (c.equals(Symbols.CENTER)
                    && (previousCharacters.get(previousCharacters.size() - 1)
                            .equals(Symbols.RIGHT))) {
                writer.write("center.");
                if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[79]++;
                    batterList.get(batterCount).values().iterator().next()[67]--;
                }
                else if (previousCharacters.contains(Symbols.FLYBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[80]++;
                    batterList.get(batterCount).values().iterator().next()[68]--;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[81]++;
                    batterList.get(batterCount).values().iterator().next()[69]--;
                }
                else if (previousCharacters.contains(Symbols.LINEDRIVE)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[82]++;
                    batterList.get(batterCount).values().iterator().next()[70]--;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsOut(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[83]++;
                    batterList.get(batterCount).values().iterator().next()[71]--;
                }
                else if (previousCharacters.contains(Symbols.GROUNDBALL)
                        && containsHit(previousCharacters)) {
                    batterList.get(batterCount).values().iterator().next()[84]++;
                    batterList.get(batterCount).values().iterator().next()[72]--;
                }
            }
        }
    }

    private void parseBaseRunner(String c)
    {
        if (state == 3) {
            state = 6;
            if (c.equals("x")) {
                writer
                        .write(" " + x.keySet().iterator().next()
                                + " was out at");
            }
            if (c.equals("y")) {
                writer
                        .write(" " + y.keySet().iterator().next()
                                + " was out at");
            }
            if (c.equals("z")) {
                writer
                        .write(" " + z.keySet().iterator().next()
                                + " was out at");
            }
            if (c.equals("h")) {
                writer
                        .write(" " + h.keySet().iterator().next()
                                + " was out at");
            }
            if (previousCharacters.contains(Symbols.OUT)
                    && !previousCharacters.contains(Symbols.FLYBALL)) {
                batterList.get(batterCount).values().iterator().next()[14]++;
            }
        }
        else if (state == 4) {
            state = 6;
            if (c.equals("x")) {
                writer
                        .write(" " + x.keySet().iterator().next()
                                + " was out at");
            }
            if (c.equals("y")) {
                writer
                        .write(" " + y.keySet().iterator().next()
                                + " was out at");
            }
            if (c.equals("z")) {
                writer
                        .write(" " + z.keySet().iterator().next()
                                + " was out at");
            }
            if (c.equals("h")) {
                writer
                        .write(" " + h.keySet().iterator().next()
                                + " was out at");
            }
        }
        else if (state == 5) {
            state = 6;
            if (c.equals("x")) {
                writer.write(" " + x.keySet().iterator().next()
                        + " advanced to");
            }
            if (c.equals("y")) {
                writer.write(" " + y.keySet().iterator().next()
                        + " advanced to");
            }
            if (c.equals("z")) {
                writer.write(" " + z.keySet().iterator().next()
                        + " advanced to");
            }
            if (c.equals("h")) {
                writer.write(" " + h.keySet().iterator().next()
                        + " advanced to");
            }
        }
    }

    private void parseBase(String c)
    {
        if (state == 6) {
            state = 3;
            if (c.equals(Symbols.FIRSTBASE)) {
                writer.write(" first.");
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("x")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("x", 1);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("y")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("y", 1);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("z")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("z", 1);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("h")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("h", 1);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("x")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("x");
                    outs++;
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("y")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("y");
                    outs++;
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("z")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("z");
                    outs++;
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("h")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("h");
                    outs++;
                }
            }
            if (c.equals(Symbols.SECONDBASE)) {
                writer.write(" second.");
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("x")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("x", 2);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("y")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("y", 2);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("z")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("z", 2);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("h")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("h", 2);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("x")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("x");
                    outs++;
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("y")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("y");
                    outs++;
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("z")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("z");
                    outs++;
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("h")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("h");
                    outs++;
                }
            }
            if (c.equals(Symbols.THIRDBASE)) {
                writer.write(" third.");
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("x")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("x", 3);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("y")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("y", 3);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("z")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("z", 3);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("h")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("h", 3);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("x")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("x");
                    outs++;
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("y")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("y");
                    outs++;
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("z")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("z");
                    outs++;
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("h")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("h");
                    outs++;
                }
            }
            if (c.equals(Symbols.HOMEBASE)) {
                writer.write(" home.");
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("x")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("x", 4);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("y")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("y", 4);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("z")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("z", 4);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("h")
                        && previousCharacters
                                .get(previousCharacters.size() - 2).equals(
                                        Symbols.BASERUNNERADVANCE)) {
                    advanceRunnerToBase("h", 4);
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("x")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("x");
                    outs++;
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("y")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("y");
                    outs++;
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("z")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("z");
                    outs++;
                }
                if (previousCharacters.get(previousCharacters.size() - 1)
                        .equals("h")
                        && !previousCharacters.get(
                                previousCharacters.size() - 2).equals(
                                Symbols.BASERUNNERADVANCE)) {
                    eraseBaseRunner("h");
                    outs++;
                }
            }
        }
    }

    private void parseBaseRunnerAdvance(String c)
    {
        if (state == 3) {
            state = 5;
        }
        else if (state == 4) {
            state = 5;
        }
    }
    
    private void parsePrematureEndOfInning(String c) {
        outs = 3;
    }
    
    private void parseExtraInning(String c) {       
        updateInning();
        outs = 1;
        x.put(batterList.get(batterCount).keySet().iterator().next(), 2);     
    }

    private void advanceRunnerToBase(String runner, int base)
    {
        if (runner.equals("x")) {
            x.put(x.keySet().iterator().next(), base);
        }
        if (runner.equals("y")) {
            y.put(y.keySet().iterator().next(), base);
        }
        if (runner.equals("z")) {
            z.put(z.keySet().iterator().next(), base);
        }
        if (runner.equals("h")) {
            h.put(h.keySet().iterator().next(), base);
        }
    }

    private void advanceAll(int base)
    {
        if (!x.isEmpty()) {
            x.put(x.keySet().iterator().next(), base);
        }
        if (!y.isEmpty()) {
            y.put(y.keySet().iterator().next(), base);
        }
        if (!z.isEmpty()) {
            z.put(z.keySet().iterator().next(), base);
        }
    }

    private void advanceAllByNumber(int bases)
    {
        if (!x.isEmpty()) {
            x.put(x.keySet().iterator().next(), x.values().iterator().next()
                    + bases);
        }
        if (!y.isEmpty()) {
            y.put(y.keySet().iterator().next(), y.values().iterator().next()
                    + bases);
        }
        if (!z.isEmpty()) {
            z.put(z.keySet().iterator().next(), z.values().iterator().next()
                    + bases);
        }
    }

    private void advanceAllForced()
    {
        if (!x.isEmpty() && !y.isEmpty() && !z.isEmpty()) {
            advanceAllByNumber(1);
        }
        else if (!x.isEmpty() && x.values().iterator().next() == 2) {
            if (!y.isEmpty() && y.values().iterator().next() == 1) {
                advanceAllByNumber(1);
            }
        }
        else if (!x.isEmpty() && x.values().iterator().next() == 1) {
            advanceAllByNumber(1);
        }
    }

    private void eraseBaseRunner(String runner)
    {
        if (runner.equals("x")) {
            x.clear();
            if (!y.isEmpty()) {
                x.clear();
                x.put(y.keySet().iterator().next(), y.values().iterator()
                        .next());
                y.clear();
            }
            if (!z.isEmpty()) {
                y.clear();
                y.put(z.keySet().iterator().next(), z.values().iterator()
                        .next());
                z.clear();
            }
        }
        if (runner.equals("y")) {
            y.clear();
            if (!z.isEmpty()) {
                y.clear();
                y.put(z.keySet().iterator().next(), z.values().iterator()
                        .next());
                z.clear();
            }
        }
        if (runner.equals("z")) {
            z.clear();
        }
        if (runner.equals("h")) {
            h.clear();
        }
    }

    private void updateBaseRunners()
    {
        if (!x.isEmpty() && x.values().iterator().next() == 4) {
            if (previousCharacters.contains("n")
                    || previousCharacters.get(previousCharacters.size() - 1)
                            .equals(Symbols.WALK)
                    || previousCharacters.get(previousCharacters.size() - 1)
                            .equals(Symbols.HITBYPITCH)) {
                writer.write(" " + x.keySet().iterator().next() + " scored.");
            }
            for (int i = 0; i < batterList.size(); i++) {
                if (batterList.get(i).keySet().iterator().next().equals(
                        x.keySet().iterator().next())) {
                    batterList.get(i).values().iterator().next()[4]++;
                }
            }
            runsScored++;
            x.clear();
        }
        if (!y.isEmpty() && y.values().iterator().next() == 4) {
            if (previousCharacters.contains("n")
                    || previousCharacters.get(previousCharacters.size() - 1)
                            .equals(Symbols.WALK)
                    || previousCharacters.get(previousCharacters.size() - 1)
                            .equals(Symbols.HITBYPITCH)) {
                writer.write(" " + y.keySet().iterator().next() + " scored.");
            }
            for (int i = 0; i < batterList.size(); i++) {
                if (batterList.get(i).keySet().iterator().next().equals(
                        y.keySet().iterator().next())) {
                    batterList.get(i).values().iterator().next()[4]++;
                }
            }
            runsScored++;
            y.clear();
        }
        if (!z.isEmpty() && z.values().iterator().next() == 4) {
            if (previousCharacters.contains("n")
                    || previousCharacters.get(previousCharacters.size() - 1)
                            .equals(Symbols.WALK)
                    || previousCharacters.get(previousCharacters.size() - 1)
                            .equals(Symbols.HITBYPITCH)) {
                writer.write(" " + z.keySet().iterator().next() + " scored.");
            }
            for (int i = 0; i < batterList.size(); i++) {
                if (batterList.get(i).keySet().iterator().next().equals(
                        z.keySet().iterator().next())) {
                    batterList.get(i).values().iterator().next()[4]++;
                }
            }
            runsScored++;
            z.clear();
        }
        if (x.isEmpty() && !y.isEmpty()) {
            x.clear();
            x.put(y.keySet().iterator().next(), y.values().iterator().next());
            y.clear();
        }
        if (y.isEmpty() && !z.isEmpty()) {
            y.clear();
            y.put(z.keySet().iterator().next(), z.values().iterator().next());
            z.clear();
        }
        if (x.isEmpty() && !y.isEmpty()) {
            x.clear();
            x.put(y.keySet().iterator().next(), y.values().iterator().next());
            y.clear();
        }
        if (!h.isEmpty()) {
            if (x.isEmpty()) {
                x.clear();
                x.put(h.keySet().iterator().next(), h.values().iterator()
                        .next());
            }
            else {
                if (y.isEmpty()) {
                    y.clear();
                    y.put(h.keySet().iterator().next(), h.values().iterator()
                            .next());
                }
                else {
                    z.clear();
                    z.put(h.keySet().iterator().next(), h.values().iterator()
                            .next());
                }
            }
            h.clear();
        }
    }

    private void updateInning()
    {
        if (outs == 3) {
            outs = 0;
            x.clear();
            y.clear();
            z.clear();
            h.clear();
            inning++;
            
            switch (inning) {
            case 2:
                writer.write("End of inning.\n\nBeginning of second inning");
                break;
            case 3:
                writer.write("End of inning.\n\nBeginning of third inning");
                break;
            case 4:
                writer.write("End of inning.\n\nBeginning of fourth inning");
                break;
            case 5:
                writer.write("End of inning.\n\nBeginning of fifth inning");
                break;
            case 6:
                writer.write("End of inning.\n\nBeginning of sixth inning");
                break;
            case 7:
                writer.write("End of inning.\n\nBeginning of seventh inning");
                break;
            case 8:
                writer.write("End of inning.\n\nBeginning of eighth inning");
                break;
            case 9:
                writer.write("nd of inning.\n\nBeginning of ninth inning");
                break;
            case 10:
                writer.write("End of inning.\n\nBeginning of eleventh inning");
                break;
            case 11:
                writer.write("End of inning.\n\nBeginning of twelfth inning");
                break;
            default:
                System.out.println("Invalid inning:");
                break;
            }
            writer.write(" ");
            printScore();
            writer.write(".\n\n");
        }
    }

    private void printScore()
    {
        writer.write("(total runs scored so far: " + runsScored + ")");
    }

    private void printSituation()
    {
        writer.write("\n");
        if (!x.isEmpty()) {
            writer.write(x.keySet().iterator().next() + " on");
            if (x.values().iterator().next() == 3) {
                writer.write(" third");
            }
            else if (x.values().iterator().next() == 2) {
                writer.write(" second");
            }
            else if (x.values().iterator().next() == 1) {
                writer.write(" first");
            }
            if (!y.isEmpty()) {
                writer.write(", " + y.keySet().iterator().next() + " on");
                if (y.values().iterator().next() == 2) {
                    writer.write(" second");
                }
                else if (y.values().iterator().next() == 1) {
                    writer.write(" first");
                }
                if (!z.isEmpty()) {
                    writer.write(", " + z.keySet().iterator().next() + " on");
                    if (z.values().iterator().next() == 1) {
                        writer.write(" first.");
                    }
                }
                else if (z.isEmpty() && !y.isEmpty()) {
                    writer.write(".");
                }
            }
            else {
                writer.write(".");
            }
        }
        if (!x.isEmpty()) {
            writer.write(" ");
        }
        if (outs == 0) {
            if (inning != 1) {
                writer.write("Nobody out.");
            }
        }
        else if (outs == 1) {
            writer.write("One out.");
        }
        else if (outs == 2) {
            writer.write("Two out.");
        }

        writer.write("\n\n");
        
        updateInning();
        
        // check for subs
        for (Sub sub : subs) {
            if (sub.getInning() == inning) {
                for (int i = 0; i < batterList.size(); ++i) {
                    if (batterList.get(i).keySet().iterator().next().equals(sub.getPlayerFirstName() + " " + sub.getPlayerLastName())) {
                        int[] tempArray = new int[92];
                        for (int j = 0; j < 92; j++) {
                            tempArray[j] = 0;
                        }
                        tempArray[0] = sub.getSubID();
                        tempArray[1] = gameID;
                        tempArray[87] = batterList.get(i).values().iterator().next()[87];
                        HashMap<String, int[]> tempMap = new HashMap<String, int[]>();
                        tempMap.put(sub.getSubFirstName() + " " + sub.getSubLastName(), tempArray);
                        subbedOutBatterList.add(batterList.get(i));
                        batterList.set(i, tempMap);
                        break;
                    }
                }
            }
        }
        writer.flush();
    }

    private boolean isRISP()
    {
        if (!x.isEmpty()
                && (x.values().iterator().next() == 2 || x.values().iterator()
                        .next() == 3)) {
            return true;
        }
        else {
            return false;
        }
    }

    private void calculateRBI()
    {
        for (int i = 0; i < previousCharacters.size(); i++) {
            if ((previousCharacters.get(i).equals("x")
                    || previousCharacters.get(i).equals("y") || previousCharacters
                    .get(i).equals("z"))
                    && previousCharacters.get(i + 1).equals(Symbols.HOMEBASE)
                    && previousCharacters.get(i - 1).equals(
                            Symbols.BASERUNNERADVANCE)) {
                if (batterCount == 0) {
                    batterList.get(batterList.size() - 1).values().iterator()
                            .next()[9]++;
                }
                else {
                    batterList.get(batterCount - 1).values().iterator().next()[9]++;
                }
            }
        }
    }

    /**
     * This method returns the next character (as a string)
     * 
     * @return the next character in the string, or "END" if end is reached
     */
    private String next()
    {
        if (index == score.length()) {
            return END;
        }
        String c = score.substring(index, index + 1);
        return c;
    }

    private void closeWriter()
    {
        writer.close();
    }

    private boolean containsHit(ArrayList<String> characters)
    {
        if (characters.contains(Symbols.SINGLE)
                || characters.contains(Symbols.DOUBLE)
                || characters.contains(Symbols.TRIPLE)
                || characters.contains(Symbols.HOMERUN)) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean containsOut(ArrayList<String> characters)
    {
        if (characters.contains(Symbols.OUT)
                || characters.contains(Symbols.FIELDERSCHOICE)
                || characters.contains(Symbols.REACHONERROR)
                || characters.contains(Symbols.ERROR)
                || characters.contains(Symbols.SACRIFICE)
                || characters.contains(Symbols.HOMERUNLIMIT)) {
            return true;
        }
        else {
            return false;
        }
    }

    private int gameID;
    private String score;
    private ArrayList<Integer> lineup;
    private ArrayList<Sub> subs;
    private int index;
    private int state;
    private StringWriter writer;
    private final String END = "END";
    private ArrayList<HashMap<String, int[]>> batterList;
    private ArrayList<HashMap<String, int[]>> subbedOutBatterList;
    private int batterCount;
    private int outs;
    private int inning;
    private int runsScored;
    private Connection conn;
    private Roster roster;
    private GameLog playerStats;
    private ArrayList<String> previousCharacters;

    // base runners
    private Map<String, Integer> x;
    private Map<String, Integer> y;
    private Map<String, Integer> z;
    private Map<String, Integer> h;
}
