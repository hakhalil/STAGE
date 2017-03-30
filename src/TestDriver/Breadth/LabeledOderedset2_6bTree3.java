package TestDriver.OrdSet2_6.Breadth;
import org.junit.Test;

import TestDriver.OverflowException;
import TestDriver.TestDriver;
import junit.framework.TestCase;

public class LabeledOderedset2_6bTree3 extends TestCase{

	int preLast = 0;
	boolean result = false;
	int[] preElements = {};
	int resulting = 0;
	@Test
	public void test1() throws OverflowException {
		
		TestDriver driver = new TestDriver();
		 
//		driver.readStateMap("StateMap.txt");

		String transtionMapFileName = "../Examples/TestDriver/OrdSet2_6/TransitionFunctionMap.txt";
		String testSuiteName = "../Examples/TestDriver/OrdSet2_6/Breadth/LabeledOderedset2_6bTree3.txt";
		System.out.println("Running Test Suite "+ testSuiteName);
		driver.readFunctionToTransitionMap(transtionMapFileName);
		boolean success = driver.RunTestCases(testSuiteName);
		assertTrue(success);	
	}		
}

