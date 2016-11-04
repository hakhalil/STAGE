package edu.carleton.thesis.statetest;

import java.util.ArrayList;
import java.util.List;

import edu.carleton.thesis.algorithm.BreadthAlgorithm;
import edu.carleton.thesis.algorithm.DepthAlogirthm;
import edu.carleton.thesis.algorithm.RandomAlgorithm;
import edu.carleton.thesis.algorithm.RoundTripAlgorithm;
import edu.carleton.thesis.baseobj.Edge;
import edu.carleton.thesis.baseobj.Tree;
import edu.carleton.thesis.structs.Graph;

/**
 * Class that has the command line interface
 * 
 * @author Hoda
 * 
 */
public class StateBasedTest {
	static public void main(String[] args) {
		StateBasedTest sbTest = new StateBasedTest();
		sbTest.checkParameters(args);

		long startTime = System.currentTimeMillis();
		
		switch ((args[0]).getBytes()[0]) {
		case 'D':
		case 'd':
			sbTest.runDepthFirstSearch(args);
			break;
		case 'B':
		case 'b':
			sbTest.runBreadthSearch(args);
			break;
		case 'R':
		case 'r':
			sbTest.runRandomAlg(args);
			break; 
		case 'T':
		case 't':
			sbTest.runRTripAlg(args);
			break;
	
		}
		
	
	}

	private void runBreadthSearch(String[] args) {
		String inputFileName = args[1];
		String outputFileName = args[2];

		Graph inputGraph = Graph.constructGraph(inputFileName);
		BreadthAlgorithm breadthGenerator = new BreadthAlgorithm();
		long startTime = System.currentTimeMillis();
		Tree<String> tree = breadthGenerator.generateBFTree(inputGraph);
		ArrayList<Tree<String>> forest = breadthGenerator.generateAllPossibleTrees(tree);
		System.out.println("Execution time is: " + (System.currentTimeMillis() - startTime) + " seconds");
		boolean identical = breadthGenerator.compareAllTrees(forest);
		int index = outputFileName.indexOf(".dot");

		// remove the file extension so we can create multiple file names based
		// on the entered value
		if (index != -1)
			outputFileName = outputFileName.substring(0, index);

		
		/*
		 * for(int treesCount=0; depthGenerator.getTreeList() != null &&
		 * treesCount< depthGenerator.getTreeList().size(); treesCount++) {
		 * depthGenerator
		 * .getTreeList().get(treesCount).generateDOTfile(outputFileName
		 * +treesCount+".dot");
		 * depthGenerator.getTreeList().get(treesCount).printPaths();
		 * 
		 * }
		 */

		String treePathsFileName = outputFileName;
		for (int treesCount = 0; forest != null && treesCount < forest.size(); treesCount++) {
			forest.get(treesCount).generateDOTfile(outputFileName + treesCount + ".dot");
			forest.get(treesCount).printPaths(treePathsFileName);

		}

	}
	private void runRandomAlg(String[] args) {
		String inputFileName = args[1];
		String outputFileName = args[2];

		Graph inputGraph = Graph.constructGraph(inputFileName);
		RandomAlgorithm randomGenerator = new RandomAlgorithm();
		randomGenerator.CreateForest(inputGraph);

		int index = outputFileName.indexOf(".dot");

		// remove the file extension so we can create multiple file names based
		// on the entered value
		if (index != -1)
			outputFileName = outputFileName.substring(0, index);

		
		String treePathsFileName = outputFileName;
		ArrayList<Tree<String>> forest = randomGenerator.getTreeList();
		boolean identical = randomGenerator.compareAllTrees(forest);
		for (int treesCount = 0; forest != null && treesCount < forest.size(); treesCount++) {
			forest.get(treesCount).generateDOTfile(outputFileName + treesCount + ".dot");
			forest.get(treesCount).printPaths(treePathsFileName);

		}

	}

	private void runRTripAlg(String[] args) {
		String inputFileName = args[1];
		String outputFileName = args[2];

		Graph inputGraph = Graph.constructGraph(inputFileName);
		RoundTripAlgorithm RTGenerator = new RoundTripAlgorithm();
		RTGenerator.generateRoundTripPaths(inputGraph);

		// remove the file extension so we can create multiple file names based
		// on the entered value

		int index = outputFileName.indexOf(".dot");
		if (index != -1)
			outputFileName = outputFileName.substring(0, index);
		String pathsFileName = outputFileName;
		RTGenerator.printPaths(inputGraph, pathsFileName);
		
		//tree.printPaths(treePathsFileName);


	}
	private void runDepthFirstSearch(String[] args) {
		String inputFileName = args[1];
		String outputFileName = args[2];

		// Read the graph from a .DOT file and construct that graph in memory
		Graph inputGraph = Graph.constructGraph(inputFileName);

		DepthAlogirthm depthGenerator = new DepthAlogirthm();
		ArrayList<String> combination = new ArrayList<String>();

		// A graph can produce multiple trees based on the various paths we can
		// reach each of the
		// nodes that has multiple incoming edges (duplicate nodes). Creating
		// all the combinations of
		// of those paths to be able to generate the different trees
		long startTime = System.currentTimeMillis();
		depthGenerator.createPathCombinations(inputGraph, combination, 0);

		// sorting the duplicate paths as this makes it easier in creating the
		// different trees
		depthGenerator.sortPossibleCombinations();

		// generate any of the possible trees (just one is enough)
		depthGenerator.generateAlgorithm(inputGraph);

		// use the generated tree and the previously identified combination of
		// paths to create the rest of
		// the possible trees
		depthGenerator.constructTrees();
		System.out.println("Execution time is: " + (System.currentTimeMillis() - startTime) + " seconds");
		int index = outputFileName.indexOf(".dot");
		String treePathsFileName = null;
		// remove the file extension so we can create multiple file names based
		// on the entered value
		if (index != -1)
			outputFileName = outputFileName.substring(0, index);
		treePathsFileName = outputFileName;
		ArrayList<Tree<String>> forest = depthGenerator.getTreeList();
		for (int treesCount = 0; forest != null && treesCount < forest.size(); treesCount++) {
			forest.get(treesCount).generateDOTfile(outputFileName + treesCount + ".dot");
			forest.get(treesCount).printPaths(treePathsFileName);

		}

	}

	private void checkParameters(String[] args) {
		if (args == null || args.length != 3) {
			System.out.println("Usage: StateBasedTest <algorithm type> <input file> <output file>");
			printErrorInAlgoValue();
			System.exit(0);
		} else if (!checkAlgoType(args)) {
			printErrorInAlgoValue();
			System.exit(0);
		}
		String inputFileName = args[1];
		String outputFileName = args[2];
		if (!checkFileNameFormat(inputFileName) || !checkFileNameFormat(outputFileName)) {
			printErrorFileNameFormat();
			System.exit(0);
		}
	}

	private void printErrorFileNameFormat() {
		System.out.println("File name should be [Drive:\\folder\\[folder]\\]filename.DOT ");
	}

	private void printErrorInAlgoValue() {
		System.out.println("algorithm Type values: ");
		System.out.println("D = Depath First\r\nB = Breadth First \r\nR = Random \r\nT = Round Trip");
	}

	private boolean checkAlgoType(String[] args) {
		boolean rightValue = false;
		if (args[0].equalsIgnoreCase("D") || args[0].equalsIgnoreCase("B") || args[0].equalsIgnoreCase("R") || args[0].equalsIgnoreCase("T"))
			rightValue = true;
		return rightValue;
	}

	private boolean checkFileNameFormat(String fileName) {
		boolean rightValue = true;

		return rightValue;
	}
}
