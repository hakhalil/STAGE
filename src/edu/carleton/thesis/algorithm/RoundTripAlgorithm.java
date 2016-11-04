package edu.carleton.thesis.algorithm;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import edu.carleton.thesis.baseobj.Edge;
import edu.carleton.thesis.baseobj.Node;
import edu.carleton.thesis.structs.Graph;

public class RoundTripAlgorithm {

	ArrayList<List<Edge<String>>> rtPaths = new ArrayList<List<Edge<String>>>();
	public void generateRoundTripPaths(Graph graph){

		HashMap<String, Node<String>> allNodes = graph.getAllNodes();
		Iterator<String> iterator = allNodes.keySet().iterator();
		//Iterate on all nodes to get all round trips for each starting node
		List<Edge<String>>  path = null;
		while (iterator.hasNext()) {
			Node<String> node = allNodes.get(iterator.next());
			//List<Edge<String>> prefix = new ArrayList<Edge<String>>();
			if( node != graph.getInitialNode()){
				//prefix = getDirections(graph, graph.getInitialNode(), node);
				System.out.println(" " + node.getNodeValue() + " \r\n");
				compute(graph, node, null, new LinkedHashSet<Edge<String>>());
				//			ArrayList<Edge<String>>
			}

		}
	}

	private void compute(Graph g, Node<String> startVertex, 
			Node<String> currentVertex, Set<Edge<String>> currentPath)
	{
		if (currentVertex!=null && startVertex.getNodeValue() == currentVertex.getNodeValue())
		{
			//get the prefix
			List<Edge<String>> path = new ArrayList<Edge<String>>();
			//path.add(startVertex);
			if( currentPath.size()>0)
			{
				path.addAll(currentPath);
				List<Edge<String>> prefix = getDirections(g, g.getInitialNode(), startVertex);
				path.addAll(0,prefix);
				//prefix.addAll(path);
				for( Edge<String> e : path)
				{
					System.out.print("-"+ e.getEdgeValue() + '-'+e.getDestinationNodeValue());	
				}
				System.out.println(" \r\n");
				rtPaths.add(path);
			}

		}
		if (currentVertex == null)
		{ 
			currentVertex = startVertex;
		}
		List<Edge<String>> neighbors = currentVertex.getOutgoingEdges();//getOutNeighbors(g, currentVertex);
		if(neighbors != null)
		{
			for (Edge<String> neighbor : neighbors)
			{
				boolean found = false;
				for (Edge<String> e :currentPath){
					if (e.getDestinationNodeValue() == neighbor.getDestinationNodeValue())
						found = true;
				}
				if (!found)
				{
					currentPath.add(neighbor);
					compute(g, startVertex, g.getNodebyValue((neighbor.getDestinationNodeValue())), currentPath);
					currentPath.remove(neighbor);
				}
			}
		}

	}

	static List<Node<String>> getOutNeighbors(Graph g, Node<String> v)
	{
		List<Node<String>> result = new ArrayList<Node<String>>();
		for (Edge<String> e : v.getOutgoingEdges())
		{
			Node<String> node = g.getNodebyValue(e.getDestinationNodeValue());
			result.add(node);

		}
		return result;
	}

	private static Map<Edge<String>, Boolean> vis = new HashMap<Edge<String>, Boolean>();

	private static Map<Edge<String>, Edge<String>> prev = new HashMap<Edge<String>, Edge<String>>();

	public static List<Edge<String>> getDirections(Graph g, Node<String> start, Node<String> finish){
		vis.clear();
		prev.clear();
		List<Edge<String>> directions = new LinkedList<Edge<String>>();
		Queue<Edge<String>> q = new LinkedList<Edge<String>>();
		Edge<String> current = null;
		//current = start;
		// q.add(current);
		//vis.put(current, true);
		q.addAll(start.getOutgoingEdges());
		Edge<String> finishEdge = null;
		while(!q.isEmpty()){
			current =q.remove();
			if (current.getDestinationNodeValue() == finish.getNodeValue()){
				finishEdge = current;
				break;
			}else{
				Node<String> destNode = g.getNodebyValue(current.getDestinationNodeValue());
				ArrayList<Edge<String>> edges = destNode.getOutgoingEdges();
				if(edges!=null){
					for(Edge<String> e : edges){
						Node<String> node = g.getNodebyValue(e.getDestinationNodeValue());
						if(!containsLeadingEdge(e)){
							q.add(e);
							vis.put(e, true);
							prev.put(e, current);
						}
					}
				}else{

				}
			}
		}
		if (current.getDestinationNodeValue()!=finish.getNodeValue()){
			System.out.println("can't reach destination");
		}
		for(Edge<String> e = finishEdge; e != null; e = prev.get(e)) {
			directions.add(e);
		}
		Collections.reverse(directions);
		//  directions.reverse();
		return directions;
	}

	public static Boolean containsLeadingEdge(Edge<String> e)
	{
		Boolean found = false;

		if(vis.containsValue(e))
			found = true;
		else{
			Iterator<Edge<String>> iterator = vis.keySet().iterator();
			while (iterator.hasNext()&&!found) {
				Edge<String> visitedEdge  = iterator.next();
				if(visitedEdge.getDestinationNodeValue() == e.getDestinationNodeValue())
					found = true;
			}

		}

		return found;
	}
	public void printPaths(Graph g, String pathsFileName)
	{
		StringBuilder pathsFile = new StringBuilder("");

		Node<String> root = g.getInitialNode();
		for (int i = 0; i < rtPaths.size(); i++) {
			pathsFile.append("[" + i + "] " + root.getNodeValue());
			for (int j = 0; j < rtPaths.get(i).size(); j++) {
				Edge<String> edge = rtPaths.get(i).get(j);
				pathsFile.append("-" + edge.getEdgeValue() + "-"
						+ edge.getDestinationNodeValue());

			}
			pathsFile.append(System.lineSeparator());
		}

		FileWriter fw = null;
		try {
			fw = new FileWriter(pathsFileName + "Path.txt");
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
}
