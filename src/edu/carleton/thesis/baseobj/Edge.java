package edu.carleton.thesis.baseobj;


/**
 * * Generic class to signify a connection between two nodes. The edge has an identification value
 * @author Hoda
 *
 * @param <T>
 */
public class Edge<T> {
	T edgeValue;
	T destinationNodeValue;

	public Edge(T _edgeValue, T _node) {
		this.edgeValue = _edgeValue;
		this.destinationNodeValue = _node;
	}

	public T getEdgeValue() {
		return edgeValue;
	}

	public void setEdgeValue(T edgeValue) {
		this.edgeValue = edgeValue;
	}

	public T getDestinationNodeValue() {
		return destinationNodeValue;
	}

	public void setDestinationNodeValue(T destinationNodeValue) {
		this.destinationNodeValue = destinationNodeValue;
	}
	
	public Edge<T> clone() {
		Edge<T> theClone = new Edge<T>(getEdgeValue(), getDestinationNodeValue());
		return theClone;
	}

}
