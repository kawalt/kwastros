package scoring;
import java.util.ArrayList;


public final class Symbols
{
    public static final String SINGLE = "s";
    public static final String DOUBLE = "d";
    public static final String TRIPLE = "t";
    public static final String HOMERUN = "n";
    public static final String WALK = "w";
    public static final String HITBYPITCH = "u";
    public static final String REACHONERROR = "r";
    public static final String SACRIFICE = "c";
    public static final String OUT = "o";
    public static final String FIELDERSCHOICE = "i";
    
    public static final String GROUNDBALL = "g";
    public static final String FLYBALL = "f";
    public static final String LINEDRIVE = "l";
    public static final String BUNT = "b";
    public static final String STRIKEOUT = "k";
    
    public static final String PITCHER = "1";
    public static final String CATCHER = "2";
    public static final String FIRST = "3";
    public static final String SECOND = "4";
    public static final String THIRD = "5";
    public static final String SHORTSTOP = "6";
    public static final String LEFT = "7";
    public static final String CENTER = "8";
    public static final String RIGHT = "9";
    
    public static final String WILDPITCH = "m";
    public static final String PASSBALL = "p";
    public static final String STOLENBASE = "v";
    public static final String PICKOFF = "j";
    public static final String HOMERUNLIMIT = "q";
    public static final String ERROR = "e"; // reach on error, no hit
    
    public static final String BASERUNNERADVANCE = "a";
    
    public static final String BASERUNNER1 = "x";
    public static final String BASERUNNER2 = "y";
    public static final String BASERUNNER3 = "z";
    public static final String HITTER = "h";
    
    public static final String FIRSTBASE = "1";
    public static final String SECONDBASE = "2";
    public static final String THIRDBASE = "3";
    public static final String HOMEBASE = "4";
    
    public static final String END_OF_INNING = "#";
    public static final String EXTRA_INNING = "^";
    
    public static ArrayList<String> AB_ACTIONS = new ArrayList<String>();
    public static ArrayList<String> NONAB_ACTIONS = new ArrayList<String>();
    public static ArrayList<String> DESCRIPTORS_P1 = new ArrayList<String>();
    public static ArrayList<String> DESCRIPTORS_P2 = new ArrayList<String>();
    public static ArrayList<String> DESCRIPTORS_P3 = new ArrayList<String>();
    public static ArrayList<String> BASERUNNERS = new ArrayList<String>();
    public static ArrayList<String> BASES = new ArrayList<String>();
    
    static 
    {
        AB_ACTIONS.add(SINGLE);
        AB_ACTIONS.add(DOUBLE);
        AB_ACTIONS.add(TRIPLE);
        AB_ACTIONS.add(HOMERUN);
        AB_ACTIONS.add(WALK);
        AB_ACTIONS.add(HITBYPITCH);
        AB_ACTIONS.add(REACHONERROR);
        AB_ACTIONS.add(ERROR);
        AB_ACTIONS.add(SACRIFICE);
        AB_ACTIONS.add(OUT);
        AB_ACTIONS.add(FIELDERSCHOICE);
        AB_ACTIONS.add(HOMERUNLIMIT);
        
        NONAB_ACTIONS.add(WILDPITCH);
        NONAB_ACTIONS.add(PASSBALL);
        NONAB_ACTIONS.add(STOLENBASE);
        NONAB_ACTIONS.add(PICKOFF);
        
        DESCRIPTORS_P1.add(GROUNDBALL);
        DESCRIPTORS_P1.add(FLYBALL);
        DESCRIPTORS_P1.add(LINEDRIVE);
        DESCRIPTORS_P1.add(BUNT);
        DESCRIPTORS_P1.add(STRIKEOUT);
        
        DESCRIPTORS_P2.add(PITCHER);
        DESCRIPTORS_P2.add(CATCHER);
        DESCRIPTORS_P2.add(FIRST);
        DESCRIPTORS_P2.add(SECOND);
        DESCRIPTORS_P2.add(THIRD);
        DESCRIPTORS_P2.add(SHORTSTOP);
        DESCRIPTORS_P2.add(LEFT);
        DESCRIPTORS_P2.add(CENTER);
        DESCRIPTORS_P2.add(RIGHT);

        DESCRIPTORS_P3.add(CENTER);
        
        BASERUNNERS.add(BASERUNNER1);
        BASERUNNERS.add(BASERUNNER2);
        BASERUNNERS.add(BASERUNNER3);
        BASERUNNERS.add(HITTER);
        
        BASES.add(FIRSTBASE);
        BASES.add(SECONDBASE);
        BASES.add(THIRDBASE);
        BASES.add(HOMEBASE);
    }    
}
