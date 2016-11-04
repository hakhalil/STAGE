
package edu.carleton.thesis.algorithm;

import java.util.ArrayList;
import java.util.Random;

import edu.carleton.thesis.baseobj.Edge;
import edu.carleton.thesis.baseobj.Node;
import edu.carleton.thesis.baseobj.Tree;
import edu.carleton.thesis.structs.Graph;
import edu.carleton.thesis.structs.GraphNode;
import edu.carleton.thesis.structs.TreeNode;

/**
 * @author Hoda
 *
 *This class generates a random traversal tree of a given graph.
 *Purpose: to have a base to compare other algorithms with
 */
public class RandomAlgorithm {

	ArrayList<Tree<String>> treeList = new ArrayList<Tree<String>>();
	public ArrayList<Tree<String>> getTreeList() {
		return treeList;
	}
	private static final int number_of_trees =10;	
	public void CreateForest(Graph graph) {

		//Random generator to select a random edge that could be added to the tree
		for( int i=0;i<number_of_trees; i++){

			GraphNode graphNode = graph.getInitialNode();
			Tree<String> tree = new Tree<String>();
			tree.setTreeNumber(i);
			//A queue of graph nodes that were not processed yet.
			ArrayList<Edge<String>> EdgesEligibleTobeAdded = new ArrayList<Edge<String>>();
			EdgesEligibleTobeAdded.addAll(graphNode.getOutgoingEdges());

			// A tree cannot exist without a root node. To create the root node
			// it will have a null parent (since it is root) and the same value as
			// the initial node of the graph 
			TreeNode root = new TreeNode(null, graphNode.getNodeValue());
			tree.setRoot(root);

			Random generator = new Random();
			do{

				//randomly pick an edge to be added to the tree
				int edgeIndex = generator.nextInt(EdgesEligibleTobeAdded.size());
				Edge<String> edgeToBeAdded = EdgesEligibleTobeAdded.get(edgeIndex);
				//Getting the source node of the edge.
				graphNode = graph.getNodebyValue(edgeToBeAdded.getDestinationNodeValue());
				//graphNode = graph.getNodebyValue(graph.getNodebyValue((edgeToBeAdded.getDestinationNodeValue())).getParent());
				Node<String> currentNode= tree.getNodebyValue(graph.getSourceNode(edgeToBeAdded));
				//Add the edges branching from the root node to the list of edges that could be added to the tree
				if((tree.getNodebyValue(graphNode.getNodeValue())==null)&&!graphNode.isLeaf())
					EdgesEligibleTobeAdded.addAll(graphNode.getOutgoingEdges());
				String treeNodeValue = edgeToBeAdded.getDestinationNodeValue();
				while(tree.getNodebyValue(treeNodeValue)!=null)
				{
					treeNodeValue = 'x'+treeNodeValue;
				}
				Edge<String> treeEdge = new Edge<String>(edgeToBeAdded.getEdgeValue(), treeNodeValue);			
				tree.addEdgeToNode(currentNode.getNodeValue(), treeEdge);
				//removing the edge that has been already added to the tree
				EdgesEligibleTobeAdded.remove(edgeIndex);
			}while(EdgesEligibleTobeAdded.size()!=0);
			treeList.add(tree);
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

}