package TestDriver.CruiseControl.Breadth;
import org.junit.Test;

import TestDriver.OverflowException;
import TestDriver.TestDriver;
import junit.framework.TestCase;


public class CruiseControl_BTree1 extends TestCase{

	int preLast = 0;
	boolean result = false;
	int[] preElements = {};
	int resulting = 0;
	@Test
	public void test1() throws OverflowException {
		
		TestDriver driver = new TestDriver();
		 
//		driver.readStateMap("StateMap.txt");

		String transtionMapFileName = "../Examples/TestDriver/CruiseControl/TransitionFunctionMap.txt";
		String testSuiteName = "../Examples/TestDriver/CruiseControl/Breadth/CruiseControl_BTree1.txt";
		System.out.println("Running Test Suite "+ testSuiteName);
		driver.readFunctionToTransitionMap(transtionMapFileName);
		boolean success = driver.RunTestCases(testSuiteName);
		assertTrue(success);
	}		
}

