package TestDriver.OrdSet3_9Test;
import junit.framework.TestCase;

import static org.junit.Assert.*;

import org.junit.Test;
import TestDriver.*;

public class ReplaceThis extends TestCase{

	int preLast = 0;
	boolean result = false;
	int[] preElements = {};
	int resulting = 0;
	@Test
	public void test1() throws OverflowException {
		
		TestDriver driver = new TestDriver();
		 
//		driver.readStateMap("StateMap.txt");

		String transtionMapFileName = "../Examples/OrderedSet3_9/TestDriver/TransitionFunctionMap.txt";
		String testSuiteName = "../Examples/OrderedSet3_9/TestDriver/Breadth/ReplaceThis.txt";
		System.out.println("Running Test Suite "+ testSuiteName);
		driver.readFunctionToTransitionMap(transtionMapFileName);
		driver.RunTestCases(testSuiteName);
	}		
}

