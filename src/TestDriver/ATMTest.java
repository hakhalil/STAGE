package TestDriver;
import org.junit.Test;

import junit.framework.TestCase;


public class ReplaceThis extends TestCase{

	int preLast = 0;
	boolean result = false;
	int[] preElements = {};
	int resulting = 0;
	@Test
	public void test1() throws OverflowException {
		
		TestDriver driver = new TestDriver();
		 
//		driver.readStateMap("StateMap.txt");

		String transtionMapFileName = "../Examples/ATM/TransitionFunctionMap.txt";
		String testSuiteName = "../Examples/ATM/Breadth/ReplaceThis.txt";
		System.out.println("Running Test Suite "+ testSuiteName);
		driver.readFunctionToTransitionMap(transtionMapFileName);
		driver.RunTestCases(testSuiteName);
	}		
}

