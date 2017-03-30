package edu.carleton.thesis.io;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;


public class ReadWriteDOTFile {
    private Graph readGraph;
    private int[][] arcsArray;
    private int[][] newArcsArray;
    private int numArcs;

    public ReadWriteDOTFile(String filePath) throws Exception, FileNotFoundException, ParseException {
        FileReader in=null;
        File f = new File( filePath );
        Parser p=null;

        in = new FileReader(f);
        p = new Parser();
        p.parse(in);

        ArrayList<Graph> gl = p.getGraphs();

	if (gl.size() != 1) {
		throw new Exception("The DOT file must only contain one graph!");
        }

	readGraph = gl.get(0);

	System.out.println(getNumberOfNodes()+" "+getNumberOfEdges());
	printGraph();
    }


    protected void printGraph() {
	ArrayList<Node> theNodes = readGraph.getNodes(true);
	for (int i=0; i<theNodes.size(); i++) {
		System.out.print("node "+theNodes.get(i).getId().getId());
		System.out.println(" ("+theNodes.indexOf(theNodes.get(i))+")");
	}
	ArrayList<Edge> theEdges = readGraph.getEdges();
	for (int j=0; j<theEdges.size(); j++) {
		System.out.print("edge ");
		System.out.print(theEdges.get(j).getSource().getNode().getId().getId());
		System.out.print(" -> ");
		System.out.print(theEdges.get(j).getTarget().getNode().getId().getId());
		System.out.print(", ");
		System.out.print(theEdges.get(j).getAttribute("label"));
		System.out.println(" ("+theEdges.indexOf(theEdges.get(j))+")");
	}
    }

    public int getNumberOfNodes() {
	return readGraph.getNodes(true).size();
    }
    public int getNumberOfEdges() {
	return readGraph.getEdges().size();
    }

    public String getEdgeAt(int index)
    {
    	Edge e = readGraph.getEdges().get(index);
    	String edgeValue = e.toString();
    	return edgeValue;
    }
    public String getEdgeValueAt(int index)
    {
    	Edge e=readGraph.getEdges().get(index);
    	String edgeValue = e.getAttribute("label");
    	return edgeValue;
    	
    }
    
    public int getSourceNodeForEdgeAt(int index) {
	Edge e = readGraph.getEdges().get(index);
	int ret = readGraph.getNodes(true).indexOf(e.getSource().getNode());
	return ret;
    }
    public int getTargetNodeForEdgeAt(int index) {
	Edge e = readGraph.getEdges().get(index);
	int ret = readGraph.getNodes(true).indexOf(e.getTarget().getNode());
	return ret;
    }
    
    public String getSourceNodeValueForEdgeAt(int index) {
    	Edge e = readGraph.getEdges().get(index);
    	String ret = e.getSource().getNode().getId().getId();
    	return ret;
        }
    
    public String getTargetNodeValueForEdgeAt(int index) {
    	Edge e = readGraph.getEdges().get(index);
    	String ret = e.getTarget().getNode().getId().getId();
    	return ret;
        }


    private int lastSeen(int row, int col) {
        int ret = -1;
        for (int i=0; i<row; i++) 
            for (int j=0; j<2; j++) {
                if (arcsArray[i][j] == arcsArray[row][col]) {
                    ret = newArcsArray[i][j];
                }
            }
        for (int j=0; j<col; j++)
            if (arcsArray[row][j] == arcsArray[row][col])
                ret = newArcsArray[row][j];
        return ret;
    }

    protected void showArcsArrays() {
        System.out.println("arcsArray");
        for (int i=0; i<numArcs; i++) {
            System.out.println(arcsArray[i][0]+" "+arcsArray[i][1]+" "+arcsArray[i][2]);
        }
        System.out.println("newArcsArray");
        for (int i=0; i<numArcs; i++) {
            System.out.println(newArcsArray[i][0]+" "+newArcsArray[i][1]);
        }
    }

    public void generateDOTfile(String edges, String threadName, String sourceFileName) {
      //  String sourceNode, targetNode, label;
	StringTokenizer st1, st2;

	// Prepare graph name for body of DOT file: concatenation of input file name (containing the graph) and the thread name
        // The '-' character is changed into a '_' because graphic does not accept a '-' in the graph name !!!
	String graphName = sourceFileName.substring(sourceFileName.lastIndexOf("/")+1, sourceFileName.lastIndexOf(".")).concat("_").concat(threadName.replace('-','_'));
        // Prepare name of output file: concatenation of the input file (containing the graph), fully qualified, and the thread name
	String outputFileName = sourceFileName.substring(0, sourceFileName.lastIndexOf("/")+1).concat(graphName).concat(".dot");

        // If we use node names corresponding to indexes in the readGraph.getNodes array list for labels in the output dot file,
        // graphviz will create a graph, not a tree. Indeed, the same node name will appear in several arcs of the "tree". 
        // Graphviz recognizes a node name instance in several arcs as one node to display.
        // Instead, we want each occurrence of a graph node name in the tree to be considered a separate node.
        // We need to trick graphviz. We need to name nodes according to their occurrences and give them a label that comes from the graph.
        // This way, Graphviz will display different nodes with the same label.
        // So, first, we need to count occurrences of each node number in the edge specification of String edge.
        // This is the purpose of the next couple of lines. 
        // arcsArray is a 2-dimension array recording all the node and edge numbers in the "edges" parameter.
        // newArcsArray records occurrences of node numbers that appear in arcsArray.

        st1 = new StringTokenizer(edges, "Node:");
	numArcs = st1.countTokens();
	arcsArray = new int[numArcs][3];
	int arcsArrayIndex = 0;
        while (st1.hasMoreTokens()) {
            st2 = new StringTokenizer(st1.nextToken());
            arcsArray[arcsArrayIndex][0] = Integer.parseInt(st2.nextToken());
            arcsArray[arcsArrayIndex][1] = Integer.parseInt(st2.nextToken());
            arcsArray[arcsArrayIndex][2] = Integer.parseInt(st2.nextToken());
            arcsArrayIndex++;
        }

        newArcsArray = new int[numArcs][2];
        for (int i=0; i<numArcs; i++)
            for (int j=0; j<2; j++)
                newArcsArray[i][j] = 0;

        int val;
        for (int i=0; i<numArcs; i++) {
            val = lastSeen(i, 0);
            if (val == -1) newArcsArray[i][0] = 1;
            else newArcsArray[i][0] = val;
            val = lastSeen(i, 1);
            if (val == -1) newArcsArray[i][1] = 1;
            else newArcsArray[i][1] = val+1;
        }

        // Now we are ready to generate the dot file.
        // Each node of the tree is given a node name which is a concatenation of the graph node name and a count (from newArcsArray).
        // Each node of the tree is given a label which is the corresponding graph node name.

	ArrayList<String> listOfNodeSpecs = new ArrayList<String>();
        StringBuilder strB;
        for (int i=0; i<numArcs; i++) {
            strB = new StringBuilder(readGraph.getNodes(true).get(arcsArray[i][0]).getId().getId()).append("_").append(newArcsArray[i][0]);
            strB.append(" [label="+readGraph.getNodes(true).get(arcsArray[i][0]).getId().getId()+"]\n");
            if (listOfNodeSpecs.contains(strB.toString()) == false)
                listOfNodeSpecs.add(strB.toString());
            strB = new StringBuilder(readGraph.getNodes(true).get(arcsArray[i][1]).getId().getId()).append("_").append(newArcsArray[i][1]);
            strB.append(" [label="+readGraph.getNodes(true).get(arcsArray[i][1]).getId().getId()+"]\n");
            if (listOfNodeSpecs.contains(strB.toString()) == false)
                listOfNodeSpecs.add(strB.toString());
        }

        StringBuilder dotFileBody = new StringBuilder("digraph ");
	dotFileBody.append(graphName).append(" {\n");
        Iterator<String> itr = listOfNodeSpecs.iterator();
        while (itr.hasNext())
            dotFileBody.append(itr.next());

        for (int i=0; i<numArcs; i++) {
            dotFileBody.append(readGraph.getNodes(true).get(arcsArray[i][0]).getId().getId());
            dotFileBody.append("_").append(newArcsArray[i][0]);
            dotFileBody.append(" -> ");
            dotFileBody.append(readGraph.getNodes(true).get(arcsArray[i][1]).getId().getId());
            dotFileBody.append("_").append(newArcsArray[i][1]);
            dotFileBody.append(" [label="+readGraph.getEdges().get(arcsArray[i][2]-1).getAttribute("label")+"]\n");
        }
        dotFileBody.append("}\n");

System.out.println(dotFileBody.toString());

        try {
            FileWriter fw = new FileWriter(outputFileName);
	    fw.write(dotFileBody.toString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}
