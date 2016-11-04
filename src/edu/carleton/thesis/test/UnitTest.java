package edu.carleton.thesis.test;

import edu.carleton.thesis.algorithm.DepthAlogirthm;
import edu.carleton.thesis.baseobj.Edge;
import edu.carleton.thesis.baseobj.Node;
import edu.carleton.thesis.baseobj.Tree;
import edu.carleton.thesis.structs.Graph;
import edu.carleton.thesis.structs.GraphNode;

/**
 * Unit Testing class to try out each method that was created before all the methods are integrated together
 * @author Hoda
 *
 */
public class UnitTest {

	/**
	 * Test printing an object of type <code>Tree</code>
	 */
	public void Print() {
		testTreeAdd().printTree();
	}
	
	/**
	 * Test adding objects of types <code>Node</code> and <code>Edge</code>  to 
	 *  an object of type <code>Tree</code>
	 * 
	 */
	public Tree<String> testTreeAdd() {	
		Tree<String> tree = new Tree<String>();
		
		Node<String> root = new Node<String>(null, "A");
		tree.setRoot(root);
		
		Edge<String> edge = new Edge<String>("1", "B");
		tree.addEdgeToRoot(edge);
		
		edge = new Edge<String>("2", "C");
		tree.addEdgeToRoot(edge);
		
		edge = new Edge<String>("3", "D");
		tree.addEdgeToNode("C", edge);
		
		edge = new Edge<String>("4", "E");
		tree.addEdgeToRoot(edge);
		
		edge = new Edge<String>("5", "F");
		tree.addEdgeToNode("E", edge);
		
		edge = new Edge<String>("6", "G");
		tree.addEdgeToNode("E", edge);
		
		
		return tree;
	}

	/**
	 * Test adding objects of types <code>GraphNode</code> and <code>Edge</code>  to 
	 *  an object of type <code>Graph</code>
	 * 
	 */
	public Graph testGraphAdd() {	
		GraphNode initialNode = new GraphNode(null,"0");
		Graph inputGraph = new Graph(initialNode);
//		inputGraph.setInitialNode(initialNode);
			
		Edge<String> edge = new Edge<String>("1", "1");
		inputGraph.addEdgeToRoot(edge);
		
		edge = new Edge<String>("2", "2");
		inputGraph.addEdgeToRoot(edge);
		
		edge = new Edge<String>("3", "3");
		inputGraph.addEdgeToRoot( edge);
		
		edge = new Edge<String>("4", "4");
		inputGraph.addEdgeToNode("2", edge);

		edge = new Edge<String>("5", "3");
		inputGraph.addEdgeToNode( "4", edge);

		edge = new Edge<String>("6", "3");
		inputGraph.addEdgeToNode("2", edge);
		
		edge = new Edge<String>("7","4");
		inputGraph.addEdgeToNode("1", edge);
	/*	edge = new Edge<String>("7", "0");
		inputGraph.addEdgeToNode("1", edge);
		edge = new Edge<String>("6", "0");
		inputGraph.addEdgeToNode("2", edge);
		edge = new Edge<String>("6", "4");
		inputGraph.addEdgeToNode("2", edge);
		
		edge = new Edge<String>("7", "3");
		inputGraph.addEdgeToNode("4", edge);
		
		inputGraph.generateDOTfile("graph.dot");
	*/	
		return inputGraph;
	}

	/**
	 * 
	 * @param inputFileName
	 * @return
	 */
	public Graph testConstructGraph(String inputFileName)
	{
		//GraphNode initialNode = new GraphNode(null,"0");
		//Graph inputGraph = new Graph();
		Graph inputGraph = Graph.constructGraph(inputFileName);
		//inputGraph.generateDOTfile("Output.dot");
		//inputGraph.getNodebyValue("1").generateAllPermutations(inputGraph);
		return inputGraph;
	}
	
	/**
	 * Test the depth first traversing algorithm
	 * @param inputGraph - The graph that will be traversed in a depth first manner
	 */
	public void testDFS(Graph inputGraph)
	{
		DepthAlogirthm DepthGenerator = new DepthAlogirthm();
		DepthGenerator.generateAlgorithm(inputGraph);
		DepthGenerator.saveTrees();
		
	}

	public static void main(String[] args) {
		
 		UnitTest ut = new UnitTest();
		//ut.testTreeAdd();
 		//ut.testGraphAdd();
 		
		Graph inputGraph = ut.testConstructGraph("input.dot");
 		ut.testDFS(inputGraph);
 		ut.testTreeAdd().generateDOTfile("hoda.dot");
 		
	}
	
	
}
