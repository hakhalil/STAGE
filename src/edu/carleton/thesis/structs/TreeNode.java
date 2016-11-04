
package edu.carleton.thesis.structs;

import edu.carleton.thesis.baseobj.Node;

/**
 * 
 * special <code>Node</code> whose identifier is an <code>String</code>
 * This object will be used as part of the <code>Tree</code> object
 * @author Hoda
 *
 */
public class TreeNode extends Node<String> {


	/**
	 * Overriding the constructor of the parent
	 * @param parent
	 * @param _nodeNumber
	 */
	public TreeNode(Integer parent, Integer _nodeNumber){
		super(""+parent, ""+_nodeNumber);
	}
	
	/**
	 * Overriding the constructor of the parent
	 * @param parent
	 * @param _nodeNumber
	 */
	public TreeNode(String parent, String _nodeNumber){
		super(parent, _nodeNumber);
	}

	
}
