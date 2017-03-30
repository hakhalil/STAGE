package edu.carleton.thesis.algorithm;

import java.util.ArrayList;
import java.util.Iterator;

import edu.carleton.thesis.baseobj.Edge;
import edu.carleton.thesis.baseobj.Node;
import edu.carleton.thesis.baseobj.Tree;
import edu.carleton.thesis.structs.Graph;
import edu.carleton.thesis.structs.GraphNode;
import edu.carleton.thesis.structs.TreeNode;



public class BreadthAlgorithm {
	ArrayList<Tree<String>> treeList = new ArrayList<Tree<String>>();

	public Tree<String> generateBFTree(Graph graph) {

		GraphNode graphNode = graph.getInitialNode();
		Tree<String> tree = new Tree<String>();
		// A tree cannot exist without a root node. To create the root node
		// it will have a null parent (since it is root) and the same value as
		// the initial node of the graph 
		TreeNode root = new TreeNode(null, graphNode.getNodeValue());
		tree.setRoot(root);
		//A queue of graph nodes that were not processed yet.
		ArrayList<GraphNode> graphNodesQueue = new ArrayList<GraphNode>();
		graphNodesQueue.add(graphNode);
		do{
			//get the first element of the queue
			graphNode = graphNodesQueue.get(0);
			graphNodesQueue.remove(0);
			if(!graphNode.isLeaf()&&!graphNode.isBranched())
			{
				Node<String> currentNode= tree.getNodebyValue(graphNode.getNodeValue());
				ArrayList<Edge<String>> outgoingEdges = graphNode.getOutgoingEdges();
				graphNode.setBranched(true);
				for(int i=0;i<outgoingEdges.size();i++)
				{
					Edge<String> graphEdge = outgoingEdges.get(i);
					String treeNodeValue = graphEdge.getDestinationNodeValue();
					while(tree.getNodebyValue(treeNodeValue)!=null)
					{
						treeNodeValue = 'x'+treeNodeValue;
					}
					Edge<String> treeEdge = new Edge<String>(graphEdge.getEdgeValue(), treeNodeValue);			
					tree.addEdgeToNode(currentNode.getNodeValue(), treeEdge);
					GraphNode currentGraphNode = graph.getNodebyValue(graphEdge.getDestinationNodeValue());
					if(!currentGraphNode.isBranched())
						graphNodesQueue.add(currentGraphNode);

				}
			}

		}while(graphNodesQueue.size()!=0);

		return tree;
	}
	
	public void createForest(ArrayList<Tree<String>> forest)
	{
		//initialTree is the original tree created by the depth first algorithm. initialTree will be used to generate the rest of the trees
		//in the forest representing all the possible trees that could come out of the input graph.
		Tree<String> initialTree = forest.get(0);
		Iterator<String> iterator = initialTree.getAllNodes().keySet().iterator();
		//iterate on the tree nodes to check for nodes that are duplicates to exchange their subtrees. Duplicate nodes are created when
		//the graph traversal hits the same node twice. This means that there are more than one possible path to reach this node from
		//the initial node of the graph. 
		while (iterator.hasNext()) {
			//rotate on all the tree nodes.
			int treesToUpdate = forest.size();
			Node<String> node = initialTree.getAllNodes().get(iterator.next());
			if(node!= null)
			{
				String originalNodeValue = node.getNodeValue();
				//if the originalNodeValue starts with "0" this means its a duplicate node, and it has been already handled
				//in a previous iteration when it is original node got handled. Example: when node2 is the initial node, the subtree
				//rooted at node2 gets exchanged with the subtree rooted at node02. So, when node02 is the originalNodeValue, the iteration
				//is skipped.
				if (!originalNodeValue.startsWith("x")) {
					String rootString = initialTree.getRoot().getNodeValue();
					//if the node is the root, we don't exchange any subtrees.
					if (node.getParent() != null && !originalNodeValue.equals(rootString)) {
						Tree<String> sister = null;
						String secondNodeValue = "x" + originalNodeValue;
						do { 
							// Check if originalNode and SecondNode are on the same level
							boolean onTheSameLevel = initialTree.areNodesOnTheSameLevel(originalNodeValue,secondNodeValue);
							//loop on the subtrees for the previously handled nodes, to create copies of them. This is to handle
							//all possible combination of paths. 
							if(onTheSameLevel)
							{
								for (int i=0;i<treesToUpdate;i++){
									sister = forest.get(i).createSisterTree(originalNodeValue, secondNodeValue);
									if (sister != null )
									{
										sister.setTreeNumber(forest.size());
										forest.add(sister);
									}
								}
							}
							secondNodeValue = "x" + secondNodeValue;

							//stop looping when there are no more duplicate nodes for the original node.
						} while (initialTree.getNodebyValue(secondNodeValue)!=null);

					}
				}
			}

		}
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
	public ArrayList<Tree<String>> generateAllPossibleTrees(Tree<String> initialTree) 
	{
		ArrayList<Tree<String>> allPossibleTrees = new ArrayList<Tree<String>>();
		allPossibleTrees.add(initialTree);
		createForest(allPossibleTrees);
		return allPossibleTrees;
	}
	

}

