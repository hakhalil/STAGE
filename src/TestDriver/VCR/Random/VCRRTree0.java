package TestDriver.VCR.Random;
import org.junit.Test;

import TestDriver.OverflowException;
import TestDriver.TestDriver;
import junit.framework.TestCase;


public class VCRRTree0 extends TestCase{

	int preLast = 0;
	boolean result = false;
	int[] preElements = {};
	int resulting = 0;
	@Test
	public void test1() throws OverflowException {
		
		TestDriver driver = new TestDriver();
		 
//		driver.readStateMap("StateMap.txt");

		String transtionMapFileName = "../Examples/TestDriver/VCR/TransitionFunctionMap.txt";
		String testSuiteName = "../Examples/TestDriver/VCR/Random/VCRRTree0.txt";
		System.out.println("Running Test Suite "+ testSuiteName);
		driver.readFunctionToTransitionMap(transtionMapFileName);
		boolean Success = driver.RunTestCases(testSuiteName);
		assertTrue(Success);	
		}		
}

