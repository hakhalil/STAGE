package edu.carleton.thesis.structs;

import java.util.ArrayList;
import java.util.List;

import edu.carleton.thesis.baseobj.Edge;

/**
 * For the purposes of this simulation, this a special type of class <code>Node</code>
 * that is used in a graph (not a tree) and its identifier is an <code>Integer</code>
 * @author Hoda
 *
 */
public class GraphNode extends TreeNode {

	//member variable that states if the node is branched or not (i.e. has outgoing edges were traversed)
	boolean isBranched = false;
	public ArrayList<String> leadingPaths;

	
	//an array that holds all the possible sequences of edges coming out of a this node
	ArrayList<ArrayList<String>> allPermutations = new ArrayList<ArrayList<String>>();
	

	public GraphNode(String parent, String _nodeNumber) {
		super(parent, _nodeNumber);
		leadingPaths = new ArrayList<String>();

	}
	
	public boolean isBranched() {
		return isBranched;
	}

	public void setBranched(boolean isBranched) {
		this.isBranched = isBranched;
	}
	
	/**
	 * extract all the values of the edges and get all possible permutations of those values
	 */
	public ArrayList<ArrayList<String>> generateAllPermutations(Graph theGraph) {
		
		//this method is usually called after adding new edges, so clear up all previously stored info before starting
		allPermutations.clear();

		ArrayList<Edge<String>> edges = getOutgoingEdges();
		
		//normally leaf nodes have no baring on the structure of the tree so we don't want to add them in the permutations
		//this list will hold all edges that end in a node that is leaf
		ArrayList<String> edgesWithLeafNodes = new ArrayList<String>();
		
		//extract all values of the edges
		ArrayList<String> valuesOfEdges = new ArrayList<String>();
		for(int i=0; edges != null && i<edges.size(); i++) {
			
			Edge<String> edge = edges.get(i);
			
			//if the node is leaf add it to list of leaf nodes so that it is excluded from the permutations. 
			//it can be added later to each sequence.
			GraphNode destinationNodeOfEdge = theGraph.getNodebyValue(edge.getDestinationNodeValue());
			if(destinationNodeOfEdge.isLeaf()) {
				edgesWithLeafNodes.add(edge.getEdgeValue());
			} else {
				valuesOfEdges.add(edge.getEdgeValue());
			}
		}

		ArrayList<ArrayList<String>> lists = permute(valuesOfEdges);
		
		
			for(int i=0;i<lists.size();i++) {
				ArrayList<String> list = lists.get(i);
				list.addAll(edgesWithLeafNodes);
				allPermutations.add(list);
			}
		
		//allPermutations.addAll(lists);
		
		return allPermutations;
	}

	/**
	 * Method that does the calculation (in a recursive manner) to identify each possible 
	 * sequence of edges. 
	 * This method was found by searching the Internet
	 * @param input
	 * @return
	 */
	private ArrayList<ArrayList<String>> permute(ArrayList<String> input) {
		ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();
        if (input.isEmpty()) {
            output.add(new ArrayList<String>());
            return output;
        }
        List<String> list = new ArrayList<String>(input);
        String head = list.get(0);
        ArrayList<String> rest = new ArrayList<String>();
        rest.addAll(list.subList(1, list.size()));
        for (List<String> permutations : permute(rest)) {
            List<ArrayList<String>> subLists = new ArrayList<ArrayList<String>>();
            for (int i = 0; i <= permutations.size(); i++) {
            	ArrayList<String> subList = new ArrayList<String>();
                subList.addAll(permutations);
                subList.add(i, head);
                subLists.add(subList);
            }
            output.addAll(subLists);
        }
        return output;
    }
	
	public ArrayList<ArrayList<String>> getAllPermutations() {
		return allPermutations;
	}
	
	public void printAllPermutations(Graph theGraph) {
		generateAllPermutations(theGraph);
		for(int i=0; i<getAllPermutations().size();i++) {
			ArrayList<String> sequence = getAllPermutations().get(i);
			for(int j =0; j<sequence.size(); j++) {
				System.out.print(sequence.get(j).toString() + " ");
			}
			System.out.println("");
		}
	}

}
