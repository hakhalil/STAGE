package TestDriver.OrdSet3_9Test;
import junit.framework.TestCase;

import static org.junit.Assert.*;

import org.junit.Test;
import TestDriver.*;

public class orderedset3_9_DTree717 extends TestCase{

	int preLast = 0;
	boolean result = false;
	int[] preElements = {};
	int resulting = 0;
	@Test
	public void test1() throws OverflowException {
		
		TestDriver driver = new TestDriver();
		 
//		driver.readStateMap("StateMap.txt");

		String transtionMapFileName = "../Examples/OrderedSet3_9/TestDriver/TransitionFunctionMap.txt";
		String testSuiteName = "../Examples/OrderedSet3_9/TestDriver/Depth/orderedset3_9_DTree717.txt";
		System.out.println("Running Test Suite "+ testSuiteName);
		driver.readFunctionToTransitionMap(transtionMapFileName);
		driver.RunTestCases(testSuiteName);
	}		
}

