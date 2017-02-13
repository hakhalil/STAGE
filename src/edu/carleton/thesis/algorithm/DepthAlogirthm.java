package edu.carleton.thesis.algorithm;

import java.util.ArrayList;
import java.util.HashMap;

import edu.carleton.thesis.baseobj.Edge;
import edu.carleton.thesis.baseobj.Node;
import edu.carleton.thesis.baseobj.Tree;
import edu.carleton.thesis.structs.Graph;
import edu.carleton.thesis.structs.GraphNode;
import edu.carleton.thesis.structs.TreeNode;


/**
 * Class the implements the Depth First search
 * 
 * @author Hoda
 * 
 */
public class DepthAlogirthm {

	ArrayList<Tree<String>> treeList = new ArrayList<Tree<String>>();

	//Two dimensional array where each row has one duplicate node and its replica's.

	HashMap <String, ArrayList<Node<String>>> duplicateNodes = new HashMap<String, ArrayList<Node<String>>>();

	/**
	 * This method is the start point of invoking the depth first algorithm It
	 * takes a graph of type <code>Graph</code> and creates a new list of
	 * <code>Tree</code> objects that will store all the trees that can be
	 * 
	 * @param graph
	 */
	public void generateAlgorithm(Graph graph) {
		GraphNode graphNode = graph.getInitialNode();

		// A tree cannot exist without a root node. To create the root node
		// it will have a null parent (since it is root) and the same value as
		// the initial node
		// of the graph
		TreeNode root = new TreeNode(null, graphNode.getNodeValue());

		// create an initial tree and put it in the list of trees that represent
		// the graph
		// this is needed since the algorithm is recursive and it assumes the
		// presence of the tree
		// once the algorithm finds more nodes, they will be added to this
		// inital tree.
		Tree<String> tree = new Tree<String>();
		tree.setRoot(root);
		treeList.add(tree);
		generateDFS(graph, graphNode, 0);
	}

	/**
	 * generateDFS contains the logic of generating a set of trees that traverse
	 * the input graph recursively through using depth first traversal.
	 * 
	 * @param graph
	 * @param graphNode
	 * @param treeIndex
	 * @return
	 */
	private int generateDFS(Graph graph, GraphNode graphNode, int treeIndex) {

		// need to traverse the graph in such a way that we can go through
		// all possible permutations of edges coming out of a given node.
		// This way we can ensure we will not miss any possible trees. for
		// example if Node A has 3 outgoing edges (a, b, c) then I can start
		// traversing the graph by visiting all children of Node A by
		// passing through the following edges (abc,
		// acb, bac, bca, cab,cba). Those are all possible permutations

		int lastTreeToUpdate = treeIndex;
		int lastIndex = treeIndex;
		//TBD: This is not needed. The new algorithm uses the first permutation only
		ArrayList<Edge<String>> allpermutations = graphNode.getOutgoingEdges();
				//.generateAllPermutations(graph);

		Tree<String> parentTree;
		parentTree = treeList.get(treeIndex).clone();
		// each of the permutations has a sequence of edges we can go
		// through. Here we loop through all sequences to get each sequence and
		// get those edges to build possible trees out of them.
		for (int counter = 0; counter < 1; counter++) {

			// get a sequence of edges
			ArrayList<Edge<String>> sequence = graphNode.getOutgoingEdges();//.get(counter);

			Tree<String> tree;
			// each sequence corresponds to a new possible tree. We create a
			// tree for each sequence except for the first sequence, since we
			// already
			// have a tree. For the first sequence, we build on the tree we
			// already have.
			if (counter > 0) {
				tree = parentTree.clone();
				treeList.add(tree);
				treeIndex = treeList.size() - 1;
				tree.setTreeNumber(treeIndex); // setting the tree identifier.
				// this is to tell the caller function the range of trees that
				// has been created
				// in order to add to them the new subtrees that were found
				lastTreeToUpdate = treeIndex;
				lastIndex = treeIndex;
			}
			// loop on all edges in the sequence
			for (int seqCounter = 0; seqCounter < sequence.size(); seqCounter++) {

				// retrieve the actual edge through its value (its number ID)
				//String edgeValue = sequence.get(seqCounter);
				Edge<String> graphEdge = sequence.get(seqCounter);

				GraphNode nextNode = graph.getNodebyValue(graphEdge
						.getDestinationNodeValue());
				lastTreeToUpdate = lastIndex;
				for (int treesToUpdate = treeIndex; treesToUpdate <= lastTreeToUpdate; treesToUpdate++) {
					String graphDestinationNodeValue = graphEdge
							.getDestinationNodeValue();
					String treeNodeValue = graphDestinationNodeValue;
					Tree<String> currentTree = treeList.get(treesToUpdate);
					Tree<String> firstTreeInSequence = treeList.get(treeIndex);
					String currentNode = firstTreeInSequence.getCurrentNode();
					currentTree.setCurrentNode(currentNode);
					boolean nodeExists = (currentTree
							.getNodebyValue(graphDestinationNodeValue) != null);


					boolean nodeVisited = nodeExists;
					// treeNodeValue=graphDestinationNodeValue;
					while (nodeExists) {
						// to avoid having more than one node with the same
						// value. The node values are used as keys
						// for the hashmap table in the tree. Duplicate keys
						// would overwrite the nodes.
						treeNodeValue =  'x' + treeNodeValue;
						nodeExists = (currentTree.getNodebyValue(treeNodeValue) != null);
						// make sure not to use this node again to avoid loops
						nodeVisited = true;
					}
					// clone the graph edge into a tree edge
					Edge<String> treeEdge = new Edge<String>(
							graphEdge.getEdgeValue(), treeNodeValue);


					// The tree knows what is the current node, by default it is
					// the root.
					// New treeEdge was added to the outgoing edges of the
					// current node.
					// when adding an edge to a node, the destination node (new
					// tree node) is added implicitly

					currentTree.addEdgeToNode(currentTree.getCurrentNode(),
							treeEdge);

					//storing all duplicate nodes
					if(nodeVisited)
					{
						Node<String> currentTreeNode = currentTree.getNodebyValue(graphDestinationNodeValue);
						ArrayList<Node<String>> nodeReplicas = duplicateNodes.get(graphDestinationNodeValue);
						if(nodeReplicas==null){
							nodeReplicas = new ArrayList<Node<String>>();
							nodeReplicas.add(currentTreeNode);

						}
						Node<String> destNode = currentTree.getNodebyValue(treeNodeValue);
						nodeReplicas.add(destNode);
						duplicateNodes.put(graphDestinationNodeValue, nodeReplicas);
					}	

					// Making sure that the next node has children to be
					// traversed and added to the tree.
					if (!nextNode.isLeaf() && !nodeVisited) {

						// Setting the currentNode to be the DestinationNode.
						// The subtree that is going to be created
						// in the recursive call should be added to the
						// destination node.
						currentTree.setCurrentNode(treeEdge
								.getDestinationNodeValue());
						lastIndex = generateDFS(graph, nextNode, treesToUpdate);
						// resetting currentNode to its original value before
						// recursively calling the algorithm.
						// to continue exploring the rest of the edges of the
						// currentNode.
						currentTree.setCurrentNode(currentNode);

					}
				}
			}
		}
		//
		lastTreeToUpdate = lastIndex;
		return lastTreeToUpdate;
	}

	public void saveTrees() {
		String treePathsFileName = null;
		for (int treesCount = 0; treesCount < treeList.size(); treesCount++) {
			treeList.get(treesCount).generateDOTfile(
					"Tree" + treesCount + ".dot");
			treeList.get(treesCount).printPaths( treePathsFileName);
		}
	}

	public ArrayList<Tree<String>> getTreeList() {
		return treeList;
	}

	public ArrayList<ArrayList<Node<String>>> allCombinations = new ArrayList<ArrayList<Node<String>>>();

	ArrayList<ArrayList<String>> possibleCombinations = new ArrayList<ArrayList<String>>();
	
	/**
	 * Method that uses a bubble sort to order the paths of each node from shortest to longest.
	 * This sorting makes it easier to create the various trees which are created by repositioning the
	 * duplicate nodes within the tree. By sorting, each time we need to reposition, we don't have to recreate the
	 * tree but we can simply move the subtree around from one place to the other
	 */
	public void sortPossibleCombinations()
	{
		for(int i=0; i<possibleCombinations.size(); i++){
			//sort each array of paths
			int size = possibleCombinations.get(i).size();
			do{
				int n=0;

				for(int j = 1; j <size; j++){
					/* if this pair is out of order */
					if (possibleCombinations.get(i).get(j-1).length() > possibleCombinations.get(i).get(j).length())
					{     /* swap them and remember something changed */
						String temp = possibleCombinations.get(i).get(j-1);
						possibleCombinations.get(i).set(j-1,possibleCombinations.get(i).get(j));
						possibleCombinations.get(i).set(j, temp);
						n=j;
					}
				}
				size = n;
			}while (size!=0);
		}
	}

	public void constructTrees()
	{
		Tree<String> tree = this.treeList.get(0);

		for (int i=0; i<possibleCombinations.size(); i++){
			ArrayList<String> combination = possibleCombinations.get(i);
			Tree<String> newTree = tree.clone();
			boolean theTreeHasChanged = false;
			for(int j=0;j<combination.size();j++)
			{			
				String path = combination.get(j);
				String[] edges = path.split(",");
				Node<String> currentNode = newTree.getRoot();
				for( int k=0; k<edges.length;k++)
				{
					String edgeValue = edges[k];
					Edge<String> edge = currentNode.getEdgeByID(edgeValue);
					String nodeToBeSwitched = edge.getDestinationNodeValue();
					Node<String> secondNode = newTree.getNodebyValue(nodeToBeSwitched);
					currentNode = newTree.getNodebyValue(nodeToBeSwitched.replaceFirst("^x+(?!$)", ""));
					Node<String> firstNode = currentNode;
					if( firstNode != secondNode){
						newTree.switchNodes(firstNode, secondNode);
						theTreeHasChanged = true;
					}
				}

			}
			if(theTreeHasChanged){
				newTree.setTreeNumber(treeList.size());
				treeList.add(newTree);
			}

		}
	}
	/**
	 * This is a recursive method that creates combinations of leading paths. 
	 * Each leading path is leading to one of the MIEN nodes (i.e. with more than 1 incoming edge).
	 * All MIEN nodes must be present in all combinations. We make sure that the 
	 * combinations are selected in a way that does not violate the depth first traversal of the graph.
	 * 
	 * Note that a graph can generate multiple trees, and the difference between those trees are where 
	 * the MIEN nodes are located. We position them based on their leading path.
	 * 
	 * With each recursion we explore one of the MIEN nodes
	 * 
	 * @param graph
	 * @param combination : initially (before the recursion) empty
	 * @param currentIndex : index of the MIEN node the paths to which are currently examined
	 */
	public void createPathCombinations(Graph graph, ArrayList<String> combination, int currentIndex) 
	{
		//Check that there is any duplicate (MIEN) nodes before starting, otherwise, there is no need for this work
		GraphNode graphNode = null;
		if(currentIndex< graph.getDuplicateNodes().size()){
			graphNode = graph.getDuplicateNodes().get(currentIndex);
		}
		else
			return;

		//For the current node, go through all leading paths one by one to use them
		//to build a tree
		//loop on all the paths leading to the current node
		for(int curNodePathsIndexj=0; curNodePathsIndexj<graphNode.leadingPaths.size();curNodePathsIndexj++){
			String path = graphNode.leadingPaths.get(curNodePathsIndexj);
			boolean pathRejected = false;

			@SuppressWarnings("unchecked")
			ArrayList<String> localCombination = (ArrayList<String>)combination.clone();
			
			//Creating a set of leading paths (a combination of them) each paths starts from the root of the tree and reaches one MIEN node
			//To ensure depth first traversal, the path of each of the nodes should have the maximum 
			//similarity with the other paths (i.e. has the longest possible common sequence of edges between the paths)
			//The loop will not be entered the first time the function is called since initially the array combination is empty
			
			//Loop on each path leading to one MIEN node that(the path) has been previously added to the tree
			for(int setOfPathsIndex=0; setOfPathsIndex<combination.size()&&!pathRejected;setOfPathsIndex++){
				
				//Get the intersection between <code>path</code> of the current node and the path selected 
				//for previous nodes. That is to say, how many edges in path
				//of the current node are similar to the edges in the path of the previously SELECTED MIEN node path
				//Getting the common prefix between the path that we are intending to add and the path leading to each MIEN node that 
				//has been added to the tree in a previous call to the current function
				String currentPathIntersection = this.greatestCommonPrefix(path, combination.get(setOfPathsIndex));
				
				//While we are not sure that the last step satisfies the depth first criteria, we continue by going through 
				//the following loop which compares each of the paths leading to the previously investigated duplicate node with  
				//all the paths in the current duplicate node, the permutations are created into tuples and the tuple with  
				//the longest intersection represents the tuple that respects most the depth first traversal because the
				//longest intersection translates to the deepest common path between the two nodes
				
				//For the MIEN node that has already been added to the tree and whose path is being examined 
				//against the current path that we want to add, we loop on the other paths leading to this MIEN node
				for(int z=0; !pathRejected && z<graph.getDuplicateNodes().get(setOfPathsIndex).leadingPaths.size();z++){

					String otherPathsInteresection = this.greatestCommonPrefix(path, graph.getDuplicateNodes().get(setOfPathsIndex).leadingPaths.get(z));
					//If there is another paths leading to MIEN node (Y) that has longer common prefix with the path leading to MIEN node (X), 
					//Then the currently examined path should be rejected
					if((otherPathsInteresection.length()>currentPathIntersection.length()))
						pathRejected = true;
					else
						pathRejected = false;
				}
				
				if( pathRejected)
				{
					pathRejected = false;
					//loop on the paths of duplicatenodes(k) to make sure they do not conflict with an of my
					//paths
					for(int l=0; !pathRejected && l<graphNode.leadingPaths.size();l++){
						if( this.greatestCommonPrefix(combination.get(setOfPathsIndex), graphNode.leadingPaths.get(l)).length()> currentPathIntersection.length())
							pathRejected = true;
						else
							pathRejected = false;
					}
				}
			}
			if(!pathRejected){
				localCombination.add(path);
				//The counter points to the next MIEN node the paths of which are to be processed in the coming recursive call to createPathCombinations
				int counter=currentIndex+1;
				//recursion
				createPathCombinations(graph, localCombination,counter);
				
				//Normally trees are created by integrating a set of leading paths that leads to all
				//duplicate nodes. Therefore, we are trying to make sure that we have a combination of enough
				//paths that represents all duplicate nodes
				if(localCombination.size()==graph.getDuplicateNodes().size())
					possibleCombinations.add(localCombination);
			}

		}

	}

	public String greatestCommonPrefix(String a, String b) {
		int minLength = Math.min(a.length(), b.length());
		for (int i = 0; i < minLength; i++) {
			if (a.charAt(i) != b.charAt(i)) {
				return a.substring(0, i);
			}
		}
		return a.substring(0, minLength);
	}
	public boolean compareAllTrees(ArrayList<Tree<String>> allPossibleTrees)
	{
		boolean identical = false;
		for (int i=0;i<allPossibleTrees.size()&&(!identical);i++)
		{
			for(int j=(i+1);j<allPossibleTrees.size()&& (!identical);j++)
			{
				identical = allPossibleTrees.get(i).compareTree(allPossibleTrees.get(j));
			}

		}
		return identical;
	}

}