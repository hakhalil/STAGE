package TestDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class that implements the generic test driver to automate running test cases on different case studies.
 * 
 * @author Hoda
 *
 */

public class TestDriver {

	HashMap<Integer, CaseStudyMethod> transitionToFunction = new HashMap<Integer, CaseStudyMethod>();
	//HashMap<Integer, String> stateMap = new HashMap<Integer, String>();

	/**
	 *This method reads the mapping between the transition numbers and the function names of the case study under testing.
	 *It then populates the transitionToFunction HashMap structure with the read tuples.
	 * @param fileName
	 * fileName is the name of the input file that the function reads to populate transitionToFunction HashMap. The file has to abide
	 * by the following rules
	 *  1- The first line of the file is 0-class name. //The class in this case is the name of the case study object under test.
	 *  2- Each of the following lines has first an integer, a function name, then the arguments types separated by a " " character.
	 *  example:
	 *  0-OrdSet
	 *  1-OrdSet-Integer[]
	 *  2-remove-Integer
	 *  
	 *  This means: The class name is OrdSet, transition number #1 in the original input graphs maps to a function named
	 *  OrdSet (the constructor in this case) that takes an array of Integer as a parameter, and transition #2 maps to 
	 *  a function called remove that takes an Integer as an input parameter.
	 *  TBD: 
	 *  	handle primitive types
	 *  	the code currently assumes that if the input is of type array, then it is only the only arguments
	 */
	public void readFunctionToTransitionMap(String fileName)
	{
		File fileMap = new File(fileName);
		Scanner scanMapFile = null;
		try {
			scanMapFile = new Scanner(fileMap);
			while(scanMapFile.hasNextLine())
			{
				//This splits each line to three parts transition number, corresponding function, and function types.
				String line = scanMapFile.nextLine();
				String[] parts = line.split("-");
				CaseStudyMethod classMethod = new CaseStudyMethod();
				classMethod.methodName = parts[1];
				//If the length is greater than 2, this means that the function takes arguments that we need to store
				//the type of.
				if(parts.length>2)
				{
					//arguments types are separated by a space character.
					String[] types = parts[2].split(" ");
					classMethod.argumentsTypes = new Class[types.length];
					for (int i=0; i< types.length; i++)
					{
						Class<?> c = null;
						try {
							//handling arrays
							if(types[i].contains("[]"))
							{
								String arrayElementType = types[i].replace("[]","");
								c = Array.newInstance(Class.forName("java.lang."+arrayElementType),1).getClass();
							}
							else
								c = Class.forName("java.lang."+types[i]);

						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						classMethod.argumentsTypes[i] = c;
					}
				}
				//store the map between the transition number the casestudyMethod structure that has both the function name
				//and the arguments types
				transitionToFunction.put(new Integer(parts[0]), classMethod);
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println("No such file exists ENUM");
		} finally {
			if(scanMapFile != null)
				scanMapFile.close();
		}
	}
	
	private void readStateMap(String fileName)
	{
		File fileMap = new File(fileName);
		Scanner scanMapFile = null;
		try {
			scanMapFile = new Scanner(fileMap);
			while(scanMapFile.hasNextLine())
			{
				scanMapFile.nextLine().split("-");
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println("No such file exists ENUM");
		} finally {
			if(scanMapFile != null)
				scanMapFile.close();
		}
	}
	/**
	 * RunTestCases : The method readFunctionToTransitionMap has to be called before calling RunTestCases.
	 * RunTestCases parses the file containing the test suite (The collection of test paths to be executed). It then 
	 * executes each function corresponding to each transition in the test path in the correct order. 
	 * @param fileName
	 * fileName is the name of the test suite file that has the list of testing paths to be executed
	 * It has to follow the following rules
	 * 	1-Each line of the file represents one execution path.
	 * 	2-The line is composed of a sequence of transition numbers representing methods of the case study to get executed
	 * in order. The transitions are separated by a "-" character
	 * 	3-If the method mapped to the transition number expects arguments, the arguments are listed between two paranthesis
	 * and are separated by a " " character. 
	 * example:
	 * 	1(1 8 9)-7(1)-7(4)-8(4)
	 *	1(4)-6(2)
	 * In this case, the first path to be executed is the following transitions 1,7,7,8
	 * integer arguments 1, 8 and 9 are to be passed to the method represented by transition 1.
	 * In case, the method represented by transition 1 takes an array as an arguement, then 1, 8, 9 are going to be elements
	 * in the array.
	 */
	public boolean RunTestCases(String fileName)
	{
		boolean stateIsValid = true;
		String infeasiblePath = "Infeasible ";
		File fileTestSuite = new File(fileName);
		testOracle oracle = new testOracle(fileName);
		//The first line in the transition to function mapping file, was 0-c;ass name.
		String className = transitionToFunction.get(0).methodName;

		Scanner scanFile = null;
		Scanner scanMapLine = null;
		try {

			scanFile = new Scanner(fileTestSuite);
			//Always assume the desired constructor is the first function called in the path.
			Class<CaseStudy> theClass = (Class<CaseStudy>)Class.forName("TestDriver."+className);
			//declaring a counter to keep track of the test path being executed in the current etest suite.
			int counter = 0;
			//Get the require constructor and parameters
			while(scanFile.hasNextLine())
			{
				Object objectOfClass = null;
				counter = counter + 1;
				scanMapLine = new Scanner(scanFile.nextLine());
				String testCaseNumber = "";
				if(scanMapLine.hasNext())
					testCaseNumber = scanMapLine.next();
				
				//Printing the test case being executed
				if (testCaseNumber.contains("["))
				{
					testCaseNumber = testCaseNumber.substring(testCaseNumber.lastIndexOf("[")+1, testCaseNumber.lastIndexOf("]"));
					System.out.println("Executing test case# " + testCaseNumber);
				}
				if(scanMapLine.findInLine(infeasiblePath) != null)
				{
					oracle.infeasiblePath(testCaseNumber);
				}
				else
				{
							
					//Transitions are delimited by a "-"
					scanMapLine.useDelimiter("-");
					//Assume that the first function call of all test path is the call to a constructor
					boolean testCaseSucceeded = true;
					String functionName = null;
					while(scanMapLine.hasNext() && testCaseSucceeded)
					{
						String strArguments = scanMapLine.next();
						strArguments = strArguments.trim();
						//if this is a state not an edge. States are not numeric 
						if(!strArguments.matches("-?\\d+(\\.\\d+)?") && !strArguments.contains("("))
						{
							//Integer stateNumber = Integer.valueOf(strArguments.substring(strArguments.lastIndexOf("S")+1));
							int indexOfx = strArguments.lastIndexOf("x");//trimming the x in repeated states
							if(indexOfx < 0) indexOfx = 0;
							String stateName = strArguments.substring(indexOfx);//stateMap.get(stateNumber);
							//Check if the current state of the object is the expected state
							if(objectOfClass != null){
								
								try {
									stateIsValid = stateIsValid && oracle.checkState((CaseStudy)objectOfClass, stateName,testCaseNumber, functionName);
								} catch (InvalidStateException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						else{
							int transitionNumber = 0;
							Object[] arguments = null;
							//strArguments is the list of arguments passed to the method. The arguments are separated by spaces
							//if there are passed arguments split them and store them as objects in arguments
							if(strArguments.contains("(")){
								String strInteger = strArguments.substring(0,strArguments.indexOf("("));
								strInteger.trim();//trimming is not working!
								transitionNumber = new Integer(strInteger);
								strArguments = strArguments.substring(strArguments.indexOf("(") + 1);
								strArguments = strArguments.substring(0, strArguments.indexOf(")"));
								arguments = getArguments(transitionNumber,strArguments);
							}
							else//no parameters passed
							{
								transitionNumber = new Integer(strArguments);
							}
							functionName = transitionToFunction.get(transitionNumber).methodName;
							//If the function name is qual to the class name, then this is a call to the constructor. This should
							//always be the first call in the execution path.
							if(functionName.equals(className))
							{
								Constructor<CaseStudy> constructor = theClass.getDeclaredConstructor(transitionToFunction.get(transitionNumber).argumentsTypes);
								if( constructor != null )
								objectOfClass = constructor.newInstance(arguments);
							}else{
								Class[] argumentsTypes = transitionToFunction.get(transitionNumber).argumentsTypes;
								Method methodInCaseStudy = theClass.getDeclaredMethod(functionName,argumentsTypes);
								methodInCaseStudy.invoke(objectOfClass, arguments);
							}
						}
					}
				}
							
			}
			scanMapLine.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println("No such file exists ENUM");
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		} catch (SecurityException e) {

			e.printStackTrace();
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		finally {
			if(scanFile != null) {
				scanFile.close();
			}

			if(scanMapLine != null) {
				scanMapLine.close();
			}
		}
		return stateIsValid;
	}

	/**
	 * getArguments extracts the arguments to be passed to the method corresponding to the transitionNumber and return 
	 * them as array of objects.
	 * 
	 * @param transitionNumber : The current transition to be executed which corresponds to a method name stored in the
	 * public hashmap transitionToFunction map.
	 * @param arguments : a string that read from the test suite file. The variable arguments has the list of argument
	 * values separated by a space character.
	 * @return returns an array of objects where each object as a parameter value to be passed when invoking the method.
	 */
	public Object[] getArguments(int transitionNumber, String arguments)
	{
		String[] argumentsList = null;
		argumentsList = arguments.split(" ");
		Object[] cArg = null;
		CaseStudyMethod method = transitionToFunction.get(transitionNumber);
		Class<?> argClass = method.argumentsTypes[0]; 
		//handling arrays : if the method takes an array as its parameter, we assume that this is the only argument sent.
		//TBD: handling the case where there are many arguments some of which are arrays.
		if(argClass.isArray()){
			cArg = new Object[1];
			if(arguments.length()==0){
				//Get the type of the array elements to create the array
				Class<?> componentType = method.argumentsTypes[0].getComponentType();
				//create the array
				Object arr = Array.newInstance(componentType, 0);		
				cArg[0] = arr;
			}
		}
		else
			cArg = new Object[argumentsList.length];
		
		boolean isArray = false;
		//if the argument is an array, don't loop. Send all the arguments as one object of array type.
		//else loop to create an object for each arguments
		for(int i=0; ((arguments.length()!=0)&& i<argumentsList.length )&& !isArray ;i++){
			Constructor<?> argumentClass =null;
			argClass = method.argumentsTypes[i]; 
			try {
				//handling arguments of array types
				if(argClass.isArray())
				{
					isArray = true;
					//Get the type of the array elements to create the array
					Class<?> componentType = method.argumentsTypes[i].getComponentType();
					//create the array
					Object arr = Array.newInstance(componentType, argumentsList.length);
					//populate the array with the parameters read from the file
					for(int j=0;j<argumentsList.length;j++){
						try {
							Array.set(arr, j,componentType.getDeclaredConstructor(String.class).newInstance(argumentsList[j]));
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					cArg[i]=arr;
				}
				if(!argClass.isArray()){

					argumentClass = method.argumentsTypes[i].getDeclaredConstructor(String.class);

					try {
						cArg[i] = argumentClass.newInstance(argumentsList[i]);
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}//cArg[i]=argumentsList[i];
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		

		return cArg;
	}

	@SuppressWarnings("unused")
	private void checkParameters(String[] args) {
		if (args == null || args.length != 2) {
			System.out
					.println("Usage: TestDriver <transitionToFunctionMap> <TestSuite> ");
			System.out
			.println("Incorrect number of arguments!");
			System.exit(0);
		}
//TBD: Check files format
	}
	
	public void loopOnTestSuites(String dirName)
	{
		File dir = new File(dirName);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File testSuite : directoryListing) {
			
				String fileName = testSuite.getName();
				String extension = fileName.substring(fileName.lastIndexOf('.'));
				if ( extension.equals(".txt"))
				{
					System.out.println("Running Test Suite "+ fileName);
					RunTestCases(testSuite.getAbsolutePath());
				}
			}
		} else {
			//TBD: handle errors
		}
	}
	/*
	static public void main(String[] args) {


		TestDriver driver = new TestDriver();
		driver.checkParameters(args);
//		driver.readStateMap("StateMap.txt");
		String transtionMapFileName = args[0];
		String testSuitesDirName = args[1];
		driver.readFunctionToTransitionMap(transtionMapFileName);
		driver.loopOnTestSuites(testSuitesDirName);

	}*/
	static public void main(String[] args) {


		new TestDriver();
		//driver.checkParameters(args);
//		driver.readStateMap("StateMap.txt");
		String tetJUnitFile = args[1];
		String testSuitesDirName = args[0];
		//driver.readFunctionToTransitionMap(transtionMapFileName);
		TestSuiteGenerator generator = new TestSuiteGenerator();
		try {
			generator.loopOnTestSuites(testSuitesDirName,tetJUnitFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//driver.loopOnTestSuites(testSuitesDirName);

	}
}