package TestDriver.VCR;
import TestDriver.Vcr;
import java.util.*;
import java.text.DecimalFormat;
import java.io.*;
import java.lang.Math;

public class VcrAutomatedDriver extends Thread {
	private Vcr vcr = new Vcr();
	
	// 0  - build oracle
	// >0 - run test case
	static int mutantNumber;
	static String outputFilePath;
	static private int testCaseNumber; // test case number (in the test pool)
	static String testCase;

    private int sequenceLength; // the length of the test case (number of triggers in the test case)

    private final String testpoolFilename = new String("c:\\experiment-VCR\\results\\TestPool.txt");
    private final String oracleFilename = new String("c:\\experiment-VCR\\Vcr\\VcrTestOracle.txt");

    private String oracle;
    private int compareResult = 0;
    private int lastLen = 0;
	
	int defaultSleepTime = 1;
	final int msTS = 1000;
	final int acceptableDelta = 100;

	public VcrAutomatedDriver() {
		setPriority(Thread.MAX_PRIORITY);
		start();
	}

	public void run() {
        if (mutantNumber == 0) {
            // create and record oracle
            runTest(testCase);
            appendToOracleFile();
        }
        else {
            // run test cases on mutant and record whether the test case kills the given mutant.
			try {
				runTest(testCase);
			} catch (Exception e) {
				Vcr.log.println(e.toString());
				compareResult = 1;
			}
            writeTestResultFile();
        }

		System.out.println("compareResult: " + compareResult);
		System.out.println("Vcr.log: " + Vcr.log);
		System.out.println("Oracle : " + oracle);

		System.exit(0);
	}

	public static void main(String[] args) {
        /**
        * parse the arguments.
        */
        if (args.length < 4) {
			System.err.println("Wrong num of params! Only " + args.length + " specified.");
            System.exit(1);
        }

        outputFilePath = args[0];
        mutantNumber = Integer.parseInt(args[1]);
        testCaseNumber = Integer.parseInt(args[2]);
        testCase = args[3];

        // testing
		VcrAutomatedDriver vcrDrv = new VcrAutomatedDriver();
	}

    /**
     * This method contains:
     *  1. parse test case.
     *  2. record outputs after running every trigger in the test sequence.
     * 
     * The test case is obtained from file aTestCase.txt
     * the line starts with the test case number, i.e.,
     * tc#@state1@trigger1@state2@trigger2@state3
     * e.g., 12@Idle@engineOn@Running@accelerator@Running@on@Cruising@off@Standby@engineOff@Idle	
     */
	private final void runTest(String testCase) {
        //parse test case to get triggers and resultant states.
        StringTokenizer st = new StringTokenizer(testCase, "@");
        Integer.parseInt(st.nextToken()); // testCaseNumber, alrady have this!

		if (mutantNumber != 0) {
	        readOracle();
		}
		
        sequenceLength = (st.countTokens())/2;
		// +1 as we're always referencing trigger[i+1]
        String[] trigger = new String[sequenceLength+1];
        for (int i = 0; i < sequenceLength; i++) {
            String temp = st.nextToken(); // skip a state name
            if (st.hasMoreTokens()) {
                trigger[i] = st.nextToken();
            }
        }
        
        // for trigger[i+1] referencing.
        trigger[sequenceLength] = new String("NULL");

		// the first two lines in the outputs
		Vcr.log.println("Test case number: " + testCaseNumber);
		Vcr.log.println("Sequence length: " + sequenceLength);
		
        //apply the triggers
        for (int i = 0; i < sequenceLength; i++) {
            if (trigger[i].equals("EjectButton")) {
            	eject();
            }
    	    else if (trigger[i].equals("InsertingTape1")) {
				insertTape(Tape.getMinPosition(), true);
				int tapeToMove = Tape.getMaxPosition() - Tape.getMinPosition();
				i = sleep(i, trigger[i+1], tapeToMove);
    	    }
    	    else if (trigger[i].equals("InsertingTape2")) {
				insertTape(Tape.getMinPosition(), false);
    	    }
    	    else if (trigger[i].equals("InsertingTape3")) {
				insertTape(Tape.getMaxPosition(), true);
				int tapeToMove = Tape.getMaxPosition() - Tape.getMinPosition();
				i = sleep(i, trigger[i+1], tapeToMove);
    	    }
    	    else if (trigger[i].equals("InsertingTape4")) {
				insertTape(Tape.getMaxPosition(), false);
				int tapeToMove = Tape.getMaxPosition() - Tape.getMinPosition();
				i = sleep(i, trigger[i+1], tapeToMove);
    	    }
    	    else if (trigger[i].equals("PowerButton")) {
    	    	power();
    	    }
    	    else if (trigger[i].equals("PowerButton1")) {
    	    	power();
    	    }
    	    else if (trigger[i].equals("PowerButton2")) {
    	    	Tape tape = new Tape(50, false);
    	    	vcr.testingSetTape(tape);
    	    	power();
    	    }
    	    else if (trigger[i].equals("PowerButton3")) {
    	    	Tape tape = new Tape(50, true);
    	    	vcr.testingSetTape(tape);
    	    	power();
    	    }
    	    else if (trigger[i].equals("StopButton")) {
    	    	vcrstop();
    	    }
    	    else if (trigger[i].equals("RewButton")) {
    	    	rew();
				int tapeToMove = vcr.testingGetTape().getPosition() - Tape.getMinPosition();
				i = sleep(i, trigger[i+1], tapeToMove);
    	    }
    	    else if (trigger[i].equals("FFButton")) {
    	    	ff();
				int tapeToMove = Tape.getMaxPosition() - vcr.testingGetTape().getPosition();
				i = sleep(i, trigger[i+1], tapeToMove);
    	    }
    	    else if (trigger[i].equals("PauseButton")) {
    	    	pause();
    	    }
    	    else if (trigger[i].equals("PlayButton")) {
    	    	play();
				int tapeToMove = Tape.getMaxPosition() - vcr.testingGetTape().getPosition();
				i = sleep(i, trigger[i+1], tapeToMove);
    	    }
    	    else if (trigger[i].equals("RecButton")) {
    	    	rec();
				int tapeToMove = Tape.getMaxPosition() - vcr.testingGetTape().getPosition();
				i = sleep(i, trigger[i+1], tapeToMove);
    	    }

            /*if (mutantNumber != 0) {
            	if (isKilled()) {
                  	break; // mutant killed, stop this execution
            	}
			}*/
	    }

/////////////////

        if (mutantNumber != 0) {
        	isKilled();
		}
	}

	private boolean isKilled() {
		int logLen = Vcr.log.toString().length();
		int oraLen = oracle.length();

		/*if (logLen > oraLen) {
			compareResult = 1;
		}
		else {
	    	if (oracle.regionMatches(lastLen, Vcr.log.toString(), lastLen, logLen) == false) {
	    		compareResult = checkRegions(Vcr.log.toString().substring(lastLen), oracle.substring(lastLen, logLen));
	    	}
		}*/
		
		if (oraLen > logLen) {
			compareResult = 1;
		}
		else {
	    	if (oracle.regionMatches(lastLen, Vcr.log.toString(), lastLen, oraLen) == false) {
	    		compareResult = checkRegions(Vcr.log.toString().substring(lastLen, oraLen), oracle.substring(lastLen, oraLen));
	    	}
		}
		
		lastLen = logLen;

		if (compareResult == 1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	// return 0 if the same
	// return 1 if diff
	private int checkRegions(String logSub, String oraSub) {
		StringTokenizer logTok = new StringTokenizer(logSub, " ");
		StringTokenizer oraTok = new StringTokenizer(oraSub, " ");
		
		if (logTok.countTokens() != oraTok.countTokens()) {
			// it's diff
			return 1;
		}
		
		while (logTok.hasMoreTokens()) {
			String log = logTok.nextToken();
			String ora = oraTok.nextToken();
			
			if ( log.compareTo(ora) != 0 ) {
				// diff, but might be a num
				try {
					int oraNum = Integer.parseInt(ora);
					int logNum = Integer.parseInt(log);

					if (Math.abs(oraNum - logNum) > acceptableDelta) {
						// diff too big!
						// thus, diff
						return 1;
					}
					
					continue;
				}
				catch (NumberFormatException e) {
					// diff
					return 1;
				}
			}
			
		}

		// same
		return 0;
	}

	private int sleep(int i, String trigger, int tapeToMove) {
		if (trigger.equals("EndOfTapeEvent")) {
			i++;

			int speed = TapePositionAlteringState.getNormalSpeed();
			int sleepTime = msTS * tapeToMove / speed;
			sleepTime += (sleepTime * 0.1); //want it to sleep a little longer...

			controlSleep(sleepTime);
		}
		else {
			controlSleep(defaultSleepTime);
		}

		return i;
	}

	private void appendToOracleFile()
    {
        try
        {
        	// append to the oracle file
			PrintWriter outputFile = new PrintWriter(new FileWriter(oracleFilename, true));
			outputFile.println(Vcr.log);
        	outputFile.close();
      	} catch (IOException err)
		{
			System.out.println(err);
		}
	}

    private void readOracle()
    {
		// read the line that defines the expected outputs of the given test case 
		String oracleToRead = outputFilePath.concat("\\oracle.txt");

		try
		{
			BufferedReader oracleFile = new BufferedReader(new FileReader(oracleToRead));

	        oracle = oracleFile.readLine();

			oracleFile.close();
		}
		catch (IOException e)
        {
			System.out.println(e);
		}
    }

    /**
     * write the actual outputs and test result into files.
     * record whether a test case kills a given mutant.
     */
    private void writeTestResultFile()
    {
		// output the test results
        String filename1 = outputFilePath.concat("\\TestResult.txt"); // keep the filename!!!
		try
        {
			PrintWriter outputFile1 = new PrintWriter(new FileWriter(filename1, true));
	    	outputFile1.println(compareResult); // put 0 or 1 in each line
        	outputFile1.close();
      	} catch (IOException err)
		{
			System.out.println(err);
		}
		
		// output the expected outputs
		String filename2 = outputFilePath.concat("\\ActualOutput.txt"); // keep the filename!!!
        try
        {
			PrintWriter outputFile2 = new PrintWriter(new FileWriter(filename2, true));
           	outputFile2.println(Vcr.log); 
        	outputFile2.close();
      	} catch (IOException err)
		{
			System.out.println(err);
		} 
    }


	/****************************************************************************/

	private void insertTape(int pos, boolean writeProt) {
		Tape tape = new Tape(pos, writeProt);
		vcr.processEvent(InsertingTape.Instance(tape));
	}

	private void power() {
		vcr.processEvent(PowerButton.Instance());
	}

	private void eject() {
		vcr.processEvent(EjectButton.Instance());
	}

	private void rec() {
		vcr.processEvent(RecButton.Instance());
	}

	private void pause() {
		vcr.processEvent(PauseButton.Instance());
	}

	private void vcrstop() {
		vcr.processEvent(StopButton.Instance());
	}

	private void play() {
		vcr.processEvent(PlayButton.Instance());
	}

	private void rew() {
		vcr.processEvent(RewButton.Instance());
	}

	private void ff() {
		vcr.processEvent(FFButton.Instance());
	}

	private void controlSleep (int sleepTime) {
		Vcr.log.println("Driver sleeping " + sleepTime + " mseconds");

		try {
			sleep(sleepTime);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
