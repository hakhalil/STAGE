package TestDriver.ATM.Random;
import junit.framework.TestCase;

import static org.junit.Assert.*;

import org.junit.Test;

import TestDriver.OverflowException;
import TestDriver.TestDriver;


public class randomTreeTree0 extends TestCase{

	int preLast = 0;
	boolean result = false;
	int[] preElements = {};
	int resulting = 0;
	@Test
	public void test1() throws OverflowException {
		
		TestDriver driver = new TestDriver();
		 
//		driver.readStateMap("StateMap.txt");

		String transtionMapFileName = "../Examples/TestDriver/ATM/TransitionFunctionMap.txt";
		String testSuiteName = "../Examples/TestDriver/ATM/Random/randomTreeTree0.txt";
		System.out.println("Running Test Suite "+ testSuiteName);
		driver.readFunctionToTransitionMap(transtionMapFileName);
		driver.RunTestCases(testSuiteName);
	}		
}

