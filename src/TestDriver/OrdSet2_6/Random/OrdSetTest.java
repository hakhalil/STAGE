package TestDriver.OrdSet2_6.Random;
import junit.framework.TestCase;
import static org.junit.Assert.*;

import org.junit.Test;
import TestDriver.OverflowException;
import TestDriver.TestDriver;

public class ReplaceThis extends TestCase{

	int preLast = 0;
	boolean result = false;
	int[] preElements = {};
	int resulting = 0;
	@Test
	public void test1() throws OverflowException {
		
		TestDriver driver = new TestDriver();
		 
//		driver.readStateMap("StateMap.txt");

		String transtionMapFileName = "../Examples/TestDriver/OrdSet2_6/TransitionFunctionMap.txt";
		String testSuiteName = "../Examples/TestDriver/OrdSet2_6/Random/ReplaceThis.txt";
		System.out.println("Running Test Suite "+ testSuiteName);
		driver.readFunctionToTransitionMap(transtionMapFileName);
		boolean success = driver.RunTestCases(testSuiteName);
		assertTrue(success);	
	}		
}

