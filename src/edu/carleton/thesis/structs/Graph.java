package edu.carleton.thesis.structs;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import edu.carleton.thesis.baseobj.Edge;
import edu.carleton.thesis.baseobj.Node;
import edu.carleton.thesis.baseobj.Tree;
import edu.carleton.thesis.io.ReadWriteDOTFile;

/**
 * 
 * Class that represents a graph of nodes of type <code>GraphNode</code> and edges of type <code>Edge</code> 
 * @author Hoda
 *
 */
public class Graph extends Tree<String>{
	
	//starting state of the graph
	GraphNode initialNode;
	//this is an array of the nodes with more than one ingoing edge
	private ArrayList<GraphNode> duplicateNodes;

	public ArrayList<GraphNode> getDuplicateNodes() {
		return duplicateNodes;
	}
	public void setDuplicateNodes(ArrayList<GraphNode> duplicateNodes) {
		this.duplicateNodes = duplicateNodes;
	}
	private Graph() {
		setDuplicateNodes(new ArrayList<GraphNode>());
	}
	public Graph(GraphNode _initialNode) {
		this.setInitialNode(_initialNode);
		setDuplicateNodes(new ArrayList<GraphNode>());
	}

	public GraphNode getInitialNode() {
		return initialNode;
	}

	public void setInitialNode(GraphNode initialNode) {
		this.initialNode = initialNode;
		updateListOfAllNodes(this.initialNode );
	}

	/**
	 * Create this graph by reading its meta data from a .DOT file
	 * @param inputFileName - name of the .DOT file
	 */
	public static Graph constructGraph(String inputFileName){
		Graph graph = new Graph();
        ReadWriteDOTFile reader = null;
		try {
				//reading the file using methods provided by the professor
	        	reader = new ReadWriteDOTFile(inputFileName );
	        } catch (FileNotFoundException e) {
			System.err.println("File not found.");
			System.err.println(e);
	        } catch (Exception e) {
			System.err.println(e);
		}
		
		//the graph needs an initial node. We won't be able to get that from the .DOT file but we know
		// that the .DOT file uses the first node to be at index 0 and that is why we will use the same
		//value as the value for the initial node. Of course no parent therefore we send null
		
		int numberOfEdges = reader.getNumberOfEdges(); 
		//TBD : the 4 is a constant that needs to get changed. cleaner code issue
		//read the nodes and edges from the file and populate a graph in memory using class <code>Graph</code>
		if (numberOfEdges > 0)
		{
			//initial node
			
			GraphNode initNode = new GraphNode(null, reader.getSourceNodeValueForEdgeAt(0));
			graph.setInitialNode(initNode);
			
			for (int i=0; i<numberOfEdges; i++) {
				String parent = reader.getSourceNodeValueForEdgeAt(i);
				String parentValue = parent;//parent.substring(4);
				String destinationNode = reader.getTargetNodeValueForEdgeAt(i);//.substring(4)+"";
				Edge<String> graphEdge = new Edge<String>(reader.getEdgeValueAt(i), destinationNode);
				graph.addEdgeToNode(parentValue,graphEdge);
			}
		}
		
		
		graph.pruneDuplicateNodes();
		return graph;
	}
	
	/**
	 * This method is to remove all leaf nodes out of the list of all duplicate nodes.
	 * Duplicate nodes that are leaves do not have an impact on the different combinations therefore
	 * they will be deleted to reduce the amount of processing
	 */
	private void pruneDuplicateNodes()
	{
		for(int i=getDuplicateNodes().size()-1; i>=0 ; i--) {
			if(getDuplicateNodes().get(i).isLeaf())
			{
				removeFromDuplicateNodes(i);
				
			}
		}
	}
	
	public String getSourceNode(Edge<String> edge)
	{
			boolean found = false;
			Node<String> node = null;
			Iterator<String> iterator = getAllNodes().keySet().iterator();
			while (iterator.hasNext()&& found==false) {
				node = getAllNodes().get(iterator.next());
				if(!node.isLeaf())
				for(int i=0; i<node.getOutgoingEdges().size();i++){
					if(node.getOutgoingEdges().get(i)==edge)
						found=true;
				}
			}
			return node.getNodeValue();
	
	}
	public void addLeadingPathToNode(GraphNode node, String path)
	{
		
		//The next code segment retrieves all nodes associated with the edges in the path
		// and marks those nodes as visited. WHY?
		ArrayList<GraphNode> visitedInThisPath = new ArrayList<GraphNode>();
		String[] edges = path.split(",");
		GraphNode currentNode = getNodebyValue(initialNode.getNodeValue());
		for(int j=0;j<edges.length;j++)
		{
			Edge<String> edge = currentNode.getEdgeByID(edges[j]);
			currentNode = getNodebyValue(edge.getDestinationNodeValue());
			visitedInThisPath.add(currentNode);
		}
		node.leadingPaths.add(path);
		for(int i=0; !node.isLeaf() && i<node.getOutgoingEdges().size();i++){
			Edge<String> edge = node.getOutgoingEdges().get(i);
			GraphNode childNode = this.getNodebyValue(edge.getDestinationNodeValue());
			if(!(path.contains(edge.getEdgeValue()+",") || visitedInThisPath.contains(childNode)) )
				addLeadingPathToNode(childNode,path+edge.getEdgeValue()+",");
		}
	}
	@Override
	public void addEdgeToNode(String parent, Edge<String> edge) {
		
		super.addEdgeToNode(parent, edge);
		GraphNode parentNode = this.getNodebyValue(parent);
		GraphNode childNode = this.getNodebyValue(edge.getDestinationNodeValue());
		if(parentNode.leadingPaths.size() != 0)
			for(int i=0; i<parentNode.leadingPaths.size(); i++){
				
				String leadingPath = parentNode.leadingPaths.get(i) + edge.getEdgeValue() + ",";
				boolean pathExists = false;
				//This is to make sure that all the stored paths are simple paths with no cycles
				for(int j=0; j<childNode.leadingPaths.size() && !pathExists; j++)
					if( leadingPath.contains(childNode.leadingPaths.get(j)))
						pathExists = true;
				if(!pathExists)
					this.addLeadingPathToNode(childNode,leadingPath);
			}
		else
			this.addLeadingPathToNode(childNode,edge.getEdgeValue() + ",");
	}
	@Override
	/**
	 * overriding the method in the parent so as to return a <code>GraphNode</code>
	 */
	public GraphNode getNodebyValue(String id) {
		GraphNode graphNode= (GraphNode)super.getNodebyValue(id);
	
		return graphNode;
	}
	
	@Override
	/**
	 * Overriding the parent method so that a node of type <code>GraphNode</code> is added to the graph
	 */
	public void addEdgeToRoot(Edge<String> edge) {

		addEdgeToNode(initialNode.getNodeValue(), edge);
	}
	
/*	@Override
	public StringBuilder getTreeAsString()
	{
		
		StringBuilder tree = new StringBuilder("Graph0"+" {\n");
		GraphNode parentNode = this.getNodebyValue(initialNode.getNodeValue());
		getNodeAsString(parentNode, tree);
		tree.append("\n}");
		return tree;
		
	}*/

	@Override
	/**
	 * Overriding the parent method to create a node of type <code>GraphNode</code>
	 */
	protected GraphNode createNode(String parent, String nodeValue) {
		
		/**
		 * Before creating the node, if another node has the same value (i.e. we are trying to do a loop)
		 * then return the existing one (no need to create a new one) otherwise create a brand new one
		 */
		if(getNodebyValue(nodeValue) == null)
		{
			
			return new GraphNode(parent, nodeValue);
		}
		else
		{
			//This node has been created before. i.e. since it has been visited before, it has more than
			//one ingoing edge.
			GraphNode node = getNodebyValue(nodeValue);
			//if the node has been already added to the list of nodes with more than one ingoing edge, don't 
			//add it again
			if( !getDuplicateNodes().contains(node) && !(node.getNodeValue()==initialNode.getNodeValue()))
				duplicateNodes.add(node);
			return node;
			
		}
	}
	
	public void removeFromDuplicateNodes(int currentIndex)
	{
		duplicateNodes.remove(currentIndex);
	}
}
