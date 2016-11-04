package TestDriver;

import java.io.FileWriter;
import java.io.IOException;


public class testOracle {
	private String testSuiteLogFileName;
	private FileWriter fw;

	public testOracle(String testSuiteName) {
		testSuiteLogFileName = testSuiteName.substring(0, testSuiteName.lastIndexOf(".")) + ".log";
		try {
			fw = new FileWriter(testSuiteLogFileName, false);

		} catch (IOException e) {
			System.err.println(e);
		}

	}

	public boolean checkState(CaseStudy caseStudyObject, String state, String testCaseNumber, String functionName) throws InvalidStateException
	{
	
		state = state.replaceFirst("^x+(?!$)","");
		logResult(state.equals(caseStudyObject.getCurrentState()), testCaseNumber, functionName, caseStudyObject.getCurrentState());
		return state.equals(caseStudyObject.getCurrentState());
	}

	private void logResult(boolean testCaseSucceeded, String testCaseNumber, String functionName, String falseState) throws InvalidStateException
	{
		if(!testCaseSucceeded){
			
			try {

				System.out.println("Test case #"+testCaseNumber+" has failed in function " + functionName + " FALSE STATE IS : " + falseState + "\r\n" );
				fw.write("Test case #"+testCaseNumber+" has failed in function " + functionName + " FALSE STATE IS : " + falseState + "\r\n" );
				fw.flush();
		//		throw new InvalidStateException("Invalid State");

			} catch (IOException e) {
				System.err.println(e);
			} 
		}
		
	}

	public void infeasiblePath(String testCaseNumber) {

		try {
			fw.write("Test case #" + testCaseNumber + " is infeasible! \r\n");
			fw.flush();

		} catch (IOException e) {
			System.err.println(e);
		}

	}
}
