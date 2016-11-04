package edu.carleton.thesis.baseobj;

import java.util.ArrayList;

/**
 * A Node is the vertex of a graph or a tree. It has its own value(identifier). It also
 * stores its parent and the edges(branches; for a tree) coming out of it.
 * @author HOda
 *
 * @param <T>
 */
public class Node<T> {
	T nodeValue;
	T parent;

	ArrayList<Edge<T>> outgoingEdges = null;

	public Node(T _nodeValue, ArrayList<Edge<T>> edges) {
		this.nodeValue = _nodeValue;
		this.outgoingEdges = edges;
	}

	
	public Node(T _parent, T _nodeValue, ArrayList<Edge<T>> edges) {
		this.parent = _parent;
		this.nodeValue = _nodeValue;
		this.outgoingEdges = edges;
	}
	
	public Node(T _parent, T _nodeValue) {
		this.parent = _parent;
		this.nodeValue = _nodeValue;
	}
	
	public Node<T> clone() {
		Node<T> theClone = new Node<T>(getParent(), getNodeValue());
		for( int i =0; getOutgoingEdges() != null  && i < getOutgoingEdges().size(); i++){
			Edge<T> clonedEdge = getOutgoingEdges().get(i).clone();
			theClone.addEdge(clonedEdge);
		}
		return theClone;
	}

	
	
	public ArrayList<Edge<T>> getOutgoingEdges() {
		return outgoingEdges;
	}

	public void setOutgoingEdges(ArrayList<Edge<T>> outgoingEdges) {
		this.outgoingEdges = outgoingEdges;
	}


	public void addEdge(Edge<T> edge) {
		if(outgoingEdges == null)
			outgoingEdges = new ArrayList<Edge<T>>();
		outgoingEdges.add(edge);
	}
	
	public Edge<T> deleteChildNode(T id)
	{
		Edge<T> deletedEdge = null;
		boolean notFound = true;
		for( int i =0; notFound && getOutgoingEdges() != null  && i < getOutgoingEdges().size(); i++){
				T destinationNode = getOutgoingEdges().get(i).getDestinationNodeValue();
				if( id.equals(destinationNode))
				{
					deletedEdge = getOutgoingEdges().get(i);
					getOutgoingEdges().remove(i);
					notFound=false;
				}
			}
		return deletedEdge;
		
	}
	
	public Edge<T> getLeadingEdge(T id)
	{
		Edge<T> deletedEdge = null;
		boolean notFound = true;
		for( int i =0; notFound && getOutgoingEdges() != null  && i < getOutgoingEdges().size(); i++){
				T destinationNode = getOutgoingEdges().get(i).getDestinationNodeValue();
				if( id.equals(destinationNode))
				{
					deletedEdge = getOutgoingEdges().get(i);
					//getOutgoingEdges().remove(i);
					notFound=false;
				}
			}
		return deletedEdge;
		
	}
	public boolean isLeaf() {
		if(outgoingEdges == null || outgoingEdges.isEmpty()) 
			return true;
		else
			return false;
	}

	public T getNodeValue() {
		return nodeValue;
	}

	public void setNodeValue(T nodeValue) {
		this.nodeValue = nodeValue;
	}

	public T getParent() {
		return parent;
	}

	public void setParent(T parent) {
		this.parent = parent;
	}
	
	public Edge<T> getEdgeByID(T id) {
		ArrayList<Edge<T>> edges = getOutgoingEdges();
		for(int i=0; edges != null && i<edges.size(); i++) {
			Edge<T> edge =edges.get(i);
			if(edge.getEdgeValue().equals(id)){
				return edge;
			}
		}
		return null;
	}
}
