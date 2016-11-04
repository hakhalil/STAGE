package edu.carleton.thesis.baseobj;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * A base template class that represents a tree which is compromised of a value
 * and a root node that might contains other nodes
 * 
 * @author Hoda
 * 
 * @param <T>
 *            - the type of the tree
 */
public class Tree<T> {

	// tree identifier
	int treeNumber;

	// root node
	Node<T> root;

	// current node which is used as a pointer to navigate from last known
	// position
	T currentNode;

	// storages of all nodes that are in the tree (root + its children + their
	// children).
	// having this in a <code>HashMap</code> provides easy access
	HashMap<T, Node<T>> allNodes = new HashMap<T, Node<T>>();

	ArrayList<ArrayList<Edge<T>>> paths;

	/**
	 * Create a replica tree identical to the current object
	 */
	public Tree<T> clone() {
		Tree<T> theClone = new Tree<T>();
		// theClone.setTreeNumber(getTreeNumber());
		theClone.setRoot(getRoot().clone());
		theClone.setCurrentNode(getCurrentNode());

		Iterator<T> iterator = allNodes.keySet().iterator();
		while (iterator.hasNext()) {
			Node<T> node = allNodes.get(iterator.next());
			theClone.updateListOfAllNodes(node.clone());
		}

		return theClone;
	}

	public int getTreeNumber() {
		return treeNumber;
	}

	public void setTreeNumber(int treeNumber) {
		this.treeNumber = treeNumber;
	}

	public Node<T> getRoot() {
		return root;
	}

	public void setRoot(Node<T> root) {
		this.root = root;
		currentNode = root.getNodeValue();
		updateListOfAllNodes(root);
	}

	/**
	 * Traverse the tree looking for a node that has the value <code>id</code>
	 * 
	 * @param id
	 * @return
	 */
	/*
	 * public Node<T> findNodeByID(T id) { Node<T> foundNode = null;
	 * findNode(id, root, foundNode); return foundNode; }
	 */

	/**
	 * Recursive function to find the child of a child
	 * 
	 * @param id
	 *            - value we are looking for
	 * @param node
	 *            - current node to search
	 * @param foundNode
	 *            - the found node or null otherwise
	 */
	/*
	 * public void findNode(T id, Node<T> node, Node<T> foundNode) { if (id ==
	 * node.getNodeValue()) { foundNode = node; return; } else { if
	 * (node.getChildren() != null) { Iterator<T> itr =
	 * node.getChildren().keySet().iterator(); while (itr.hasNext()) { T value =
	 * itr.next(); Node<T> child = node.getChildren().get(value);
	 * findNode(value, child, foundNode); } } } }
	 */

	public Node<T> getNodebyValue(T id) {
		return allNodes.get(id);
	}

	public void addEdgeToRoot(Edge<T> edge) {

		addEdgeToNode(root.getNodeValue(), edge);
	}

	public Node<T> deleteNode(T id) {
		Node<T> deletedNode = null;
		deletedNode = allNodes.get(id);
		if (deletedNode != null) {
			allNodes.get(deletedNode.getParent()).deleteChildNode(id);
			allNodes.remove(id);
		}
		return deletedNode;
	}

	protected Node<T> createNode(T parent, T nodeValue) {
		return new Node<T>(parent, nodeValue);
	}

	/**
	 * This method adds an edge to the list of outgoing edges on the parent
	 * node. It also CREATES the destination node of that edge and add it to the
	 * list of all nodes in the tree. It also updates the list of leading paths
	 * of the destination Node
	 * 
	 * @param parentValue
	 * @param edge
	 */
	public void addEdgeToNode(T parentValue, Edge<T> edge) {
		Node<T> parentNode = getNodebyValue(parentValue);
		if (null != parentNode) {

			// set the parent of the node on the other side of the edge (i.e. the 
			// destination node) to be the parent whose value is <code>parentValue</code>
			T nodeValue = edge.getDestinationNodeValue();
			Node<T> destinationNode = createNode(parentValue, nodeValue);

			// add the edge to the parent
			parentNode.addEdge(edge);

			// add new node to all nodes
			updateListOfAllNodes(destinationNode);

			// Update the parent node in the list of all nodes
			updateListOfAllNodes(parentNode);
		} else {
			System.err.println("Tree " + treeNumber
					+ " has no node with value " + parentValue);
		}
	}

	public T getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(T currentNode) {
		this.currentNode = currentNode;
	}

	public void printTree() {

		printNode(root);
	}

	/**
	 * a method to represent the tree in a textual format
	 * 
	 * @param node
	 */
	private void printNode(Node<T> node) {
		if (node.isLeaf()) {
			return;
		} else {
			for (int i = 0; node.getOutgoingEdges() != null
					&& i < node.getOutgoingEdges().size(); i++) {
				Edge<T> edge = node.getOutgoingEdges().get(i);
				T nodeID = edge.getDestinationNodeValue();
				Node<T> destNode = getNodebyValue(nodeID);

				printNode(destNode);
				System.out.println(node.getNodeValue() + "--"
						+ edge.getEdgeValue().toString() + "-->"
						+ destNode.getNodeValue().toString());
			}

		}
	}

	public void updateListOfAllNodes(Node<T> node) {
		allNodes.put(node.getNodeValue(), node);
	}

	/**
	 * Method that traverses all the nodes in the tree to return a
	 * representation of the tree in a .DOT format
	 * 
	 * @return
	 */
	public StringBuilder getTreeAsString() {
		// create the file header
		StringBuilder tree = new StringBuilder("Tree" + treeNumber + " {\n");

		// get the root node
		Node<T> parentNode = getNodebyValue(getRoot().getNodeValue());

		// recursively build the output of each node
		getNodeAsString(parentNode, tree);

		// add the file termination
		tree.append("\n}");
		return tree;

	}

	/**
	 * Adds the textual representation of a given node to the tree textual
	 * representation in a .DOT format
	 * 
	 * @param node
	 *            - <code>Node</code> that needs to be displayed
	 * @param nodeString
	 *            <code>String</code> that will be host all the data of all
	 *            nodes
	 */
	public void getNodeAsString(Node<T> node, StringBuilder nodeString) {

		if (node.isLeaf()) {
			return;
		} else {
			// only continue if the current node has edges
			for (int i = 0; node.getOutgoingEdges() != null
					&& i < node.getOutgoingEdges().size(); i++) {

				// retrieve each edge, find its destination node and print its
				// value, then get the nodes
				// of that destination node
				Edge<T> edge = node.getOutgoingEdges().get(i);

				T nodeID = edge.getDestinationNodeValue();
				Node<T> destNode = getNodebyValue(nodeID);
				nodeString.append( node.getNodeValue().toString()
						+ " -> " + destNode.getNodeValue().toString()
						+ " [label=" + edge.getEdgeValue().toString() + "]\n");
				getNodeAsString(destNode, nodeString);

			}

		}

	}
	/**
	 * isAncestorNode checks if the secondNode is a parent or an ancestor of the firstNode. It traces back the parents of the firstNode until
	 * it reaches the root of the tree, or an ancestor of firstNode that is equal to secondNode. It returns true if it hits an ancestor of 
	 * firstNode that has a value equals to secondNode.
	 * @param firstNode the node whose ancestors are be traced.
	 * @param secondNode the node that is ancestors are checked against to check if it is one of the parents/grandparents of the firstNode.
	 * @return returns true if the secondNode is in the path from the root to the firstNode. i.e. it is one of the firstNode's ancestors.
	 */
	public boolean isAncestorNode(Node<T> firstNode, Node<T> secondNode)
	{
		//check if one of the nodes is an ancestor of the other
		boolean isAncestor = false;
		Node<T> parentNode = firstNode;
		do{
			if(parentNode.getNodeValue() != root.getNodeValue()){
				parentNode = this.getNodebyValue(parentNode.getParent());
				isAncestor = (parentNode.getNodeValue()== secondNode.getNodeValue());
			}
		}while(!(isAncestor || parentNode.getNodeValue()==root.getNodeValue()));

		return isAncestor;
	}

	public Node<T> findCommonAncestor(Node<T> firstNode, Node<T> secondNode)
	{
		Node<T> parentNodeOfFirst = this.getNodebyValue(firstNode.getParent());
		Node<T> commonAncestor = this.getNodebyValue(secondNode.getParent());
		while ((commonAncestor!=null) && (parentNodeOfFirst!=null)&& parentNodeOfFirst!=commonAncestor){
			commonAncestor = this.getNodebyValue(commonAncestor.getParent());
			if((commonAncestor==null))
				parentNodeOfFirst = this.getNodebyValue(firstNode.getParent());
		}
		return commonAncestor;
	}
	public boolean areNodesOnTheSameLevel(T firstNodeValue, T secondNodeValue)
	{
		boolean onTheSameLevel = false;
		Node<T> firstNode = this.getNodebyValue(firstNodeValue);
		Node<T> secondNode = this.getNodebyValue(secondNodeValue);
		Node<T> firstNodeParent = firstNode;
		Node<T> secondNodeParent = secondNode;
		do{
			if(secondNodeParent == firstNodeParent){
				onTheSameLevel = true;
			}else if ((secondNodeParent != null) && ( firstNodeParent != null)){
				firstNodeParent = this.getNodebyValue(firstNodeParent.getParent());
				secondNodeParent = this.getNodebyValue(secondNodeParent.getParent());
			}

		}while( (firstNodeParent!=null) && (secondNodeParent!=null) && !onTheSameLevel);

		return onTheSameLevel;
	}
	/**
	 * switches the position of two nodes in the tree
	 * 
	 * @param firstNodeValue 
	 *            : the value of the first node to be switched
	 * @param secondNodeValue
	 *            : the value of the second node to be switched
	 */
	public void switchNodes(Node<T> firstNode, Node<T> secondNode) {

		Node<T> firstNodeParent = this.allNodes.get(firstNode.getParent());
		Node<T> secondNodeParent = this.allNodes.get(secondNode.getParent());


		// delete the link between nodes and their parents
		Edge<T> firstEdge = firstNodeParent.getLeadingEdge(firstNode.getNodeValue());
		Edge<T> secondEdge = secondNodeParent.getLeadingEdge(secondNode.getNodeValue());
		firstNodeParent.deleteChildNode(firstNode.getNodeValue());
		secondNodeParent.deleteChildNode(secondNode.getNodeValue());
		// Set the destination nodes of the edges to the new nodes
		secondEdge.setDestinationNodeValue(firstNode.getNodeValue());
		firstEdge.setDestinationNodeValue(secondNode.getNodeValue());

		// put the edges in the correct position in the tree
		firstNodeParent.addEdge(firstEdge);
		secondNodeParent.addEdge(secondEdge);
		
		//Set the parent of the node to the new parent node value
		firstNode.setParent(secondNodeParent.getNodeValue());
		secondNode.setParent(firstNodeParent.getNodeValue());
		if(firstNodeParent.getNodeValue()==root.getNodeValue())
			root = firstNodeParent.clone();
		if(secondNodeParent.getNodeValue()==root.getNodeValue())
			root = secondNodeParent.clone();
	}

	/**
	 * Creates a copy of the current tree with the subtrees located at
	 * firstNodeValue and secondNodeValue switched and returns the resulting
	 * tree.
	 * 
	 * @param firstNodeValue
	 * @param secondNodeValue
	 * @return a copy of the current tree with the subtrees located at
	 *         firstNodeValue and secondNodeValue switched.
	 */
	public Tree<T> createSisterTree(T firstNodeValue, T secondNodeValue) {
		Tree<T> sister = null;
		Node<T> firstNode = this.getNodebyValue(firstNodeValue);
		Node<T>	secondNode = this.getNodebyValue(secondNodeValue);
		if( firstNode!= null && secondNode != null){
			//check if the secondNode is in the path from the root to the firstNode or vice versa. In such case, switching the two subtrees
			//rooted at firstNode and secondNode, would not make sense because we will lose one of the subtrees. Also, it means that one
			//of the graph nodes will be branched twice, which violates the original algorithm terminating conditions.
			boolean isAncestor = (isAncestorNode(firstNode,secondNode)|| isAncestorNode(secondNode,firstNode));
			//check if the two nodes are leaves, then exchanging the subtrees would result in an identical tree. In this case, we skip
			//the exchange.
			if ( !isAncestor && !(secondNode.isLeaf()&&firstNode.isLeaf())) {
				//making a copy of the current tree, then switching the subtrees rooted at firstNode and secondNode.
				sister = this.clone();
				//If there is a sister tree created, it gets added to the list of all possible trees.
				if (sister != null){
					
					firstNode = sister.getNodebyValue(firstNodeValue);
					secondNode = sister.getNodebyValue(secondNodeValue);
					sister.switchNodes(firstNode, secondNode);
				}
			}

		}
		return sister;
	}

	/**
	 * Write the string that contains the tree data into a file using the .DOT
	 * format
	 * 
	 * @param outputFileName
	 */
	public void generateDOTfile(String outputFileName) {
		StringBuilder dotFileBody = new StringBuilder("digraph ");
		dotFileBody.append(getTreeAsString());
		FileWriter fw = null;
		try {
			fw = new FileWriter(outputFileName);
			fw.write(dotFileBody.toString());
			fw.flush();
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			try {
				if (fw != null)
					fw.close();
			} catch (Exception e1) {
				System.err.println(e1);
			}

		}
	}

	public void traverse() {
		// assume root != NULL
		paths = new ArrayList<ArrayList<Edge<T>>>();
		traverse(allNodes.get(root.getNodeValue()), new ArrayList<Edge<T>>());
	}

	/**
	 * Method that will find all testing paths within a tree
	 * 
	 * @param treeNode
	 * @param path
	 */
	private void traverse(Node<T> treeNode, ArrayList<Edge<T>> path) {

		if (treeNode.isLeaf()) {
			paths.add(path);
		} else {
			for (int i = 0; treeNode.getOutgoingEdges() != null
					&& i < treeNode.getOutgoingEdges().size(); i++) {
				Edge<T> edge = treeNode.getOutgoingEdges().get(i);
				ArrayList<Edge<T>> newPath = new ArrayList<Edge<T>>(path);
				newPath.add(edge);
				traverse(this.getNodebyValue(edge.getDestinationNodeValue()),
						newPath);
			}
		}
	}

	/**
	 * Print all the paths that were found through the travese method
	 */
	public void printPaths(String treePathsFileName) {
		traverse();
		StringBuilder pathsFile = new StringBuilder("");

		for (int i = 0; i < paths.size(); i++) {
			pathsFile.append("[" + i + "] " + root.getNodeValue());
			for (int j = 0; j < paths.get(i).size(); j++) {
				Edge<T> edge = paths.get(i).get(j);
				pathsFile.append("-" + edge.getEdgeValue() + "-"
						+ edge.getDestinationNodeValue());

			}
			pathsFile.append(System.lineSeparator());
		}

		FileWriter fw = null;
		try {
			fw = new FileWriter(treePathsFileName + "Tree" + this.treeNumber + ".txt");
			fw.write(pathsFile.toString());
			fw.flush();

		} catch (IOException e) {
			System.err.println(e);
		} finally {
			try {
				if (fw != null)
					fw.close();
			} catch (Exception e1) {
				System.err.println(e1);
			}
		}

	}

	public HashMap<T, Node<T>> getAllNodes() {
		return allNodes;
	}

	public boolean compareTree(Tree<T> tree)
	{
		boolean identical = true;
		if (this.allNodes.size()== tree.allNodes.size()){
			Iterator<T> iterator = allNodes.keySet().iterator();
			while (iterator.hasNext()&&identical) {
				Node<T> node = allNodes.get(iterator.next());
				Node<T> foreignTreeNode = tree.getNodebyValue(node.getNodeValue());
				if(foreignTreeNode!=null){
					if((node.isLeaf()&&!foreignTreeNode.isLeaf())|(foreignTreeNode.isLeaf()&&!node.isLeaf())){
						identical=false;
					}
					if(!(node.isLeaf()|foreignTreeNode.isLeaf())){
						if(node.getOutgoingEdges().size()==foreignTreeNode.getOutgoingEdges().size()){
							for(int j=0;j<node.getOutgoingEdges().size()&& identical ;j++){
								Edge<T> edge = node.getOutgoingEdges().get(j);
								Edge<T> foreignEdge = foreignTreeNode.getEdgeByID(edge.getEdgeValue());
								if (foreignEdge==null | foreignEdge.destinationNodeValue!=edge.destinationNodeValue) 
									identical=false;
							}
						}
						else{
							identical=false;
						}
					}

				}
				else{
					identical=false;
				}

			}

		}
		else{
			identical=false;
		}

		return identical;
	}
}
