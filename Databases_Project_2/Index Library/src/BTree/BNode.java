package BTree;

import Keys.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is for a BNode. BNodes are used in the BTree class. A BNode has an ArrayList 
 * of the keys, and Arraylist of child BNodes, a parent BNode, the order of the node, the BNode 
 * that's to the right of this node, and the maximum number of children and the maximum number of keys
 * @author May, Bill
 *
 */
public class BNode implements Serializable {
	private ArrayList <Key> key;
	private ArrayList <BNode> child;
	private BNode parent;
	private int order;
	private final int MAX_CHILD_SIZE;

	private final int MAX_KEY_SIZE;
	private BNode left = null;
	private BNode right = null;
	
	/* Constructors */

	/**
	 * Creates BNode of Order 1, with no parent
	 */
	public BNode(){
		this(1, null);
	}

	/**
	 * Creates BNode of Order 1, parent par	
	 * @param par parent BNode
	 */
	public BNode(BNode par){
		this(1, par);
	}
 
	/**
	 * Creates BNode of Order ord, with no parent
	 * @param ord order
	 */
	public BNode(int ord){
		this(ord, null);
	}
	
	/**
	 * Creates BNode of Order ord, parent par
	 * @param ord order
	 * @param par parent BNode
	 */
	public BNode(int ord, BNode par){
		order = ord;
		key = new ArrayList <Key> ();
		child = new ArrayList <BNode> ();
		parent = par;
		MAX_CHILD_SIZE = 2*ord+1;
		MAX_KEY_SIZE = 2*ord;
	}

	/**
	 * 
	 * @param k value of key
	 * @return position of key in keys array
	 */
	public int getKeyPosition(Key k){
		if (!containsKey(k))
			return -1;
		return key.indexOf(k);
	}

	/**
	 * 
	 * @param k value of key
	 * @return does this keys array contain the key? true/false
	 */
	boolean containsKey(Key k){
		for(int i = 0; i <key.size();i++){
			if(key.get(i).compareTo(k)==0){
				return true;
			}
		}
		return false;
	}
	
	int getOrder(){
		return order;
	}
	
	int getChildSize(){
		return child.size();
	}
	
	int getKeySize(){
		return key.size();
	}
	
	int getMaxKeySize(){
		return MAX_KEY_SIZE;
	}
	
	int getMaxChildSize(){
		return MAX_CHILD_SIZE;
	}
	
	boolean isLeaf(){
		if (child.size() == 0)
			return true;
		return false;
	}
	
	BNode removeChild(int pos){
		return child.remove(pos);
	}
	
	int addChild(BNode node, int pos){
		child.add(pos, node);
		return 0;
	}
	
	int addChild(BNode node){
		child.add(node);
		return 0;
	}
	
	
	ArrayList <BNode> getChildArray(){
		return child;
	}

	BNode getLeft(){
		return left;
	}
	BNode getRight(){
		return right;
	}
	
	
	int setRight(BNode rt){
		if (rt == null)
			return -1;
		right = rt;
		return 0;
	}
	int setLeft(BNode lt){
		if (lt == null)
			return -1;
		left = lt;
		return 0;
	}
	
	
	BNode getChild (int pos){
		return child.get(pos);
	}
	
	int setParent(BNode par){
		if (par == null)
			return -1;
		parent = par;
		return 0;
	}
	
	BNode getParent(){
		return parent;
	}
	
	/**
	 * Takes in value of key and adds key in the correct place<br>
	 * Example: [3][5]:[0,1,2](3)[3,4](5)[5,6,7,8,...]	
	 * @param k value of key
	 * @return position of added key
	 */
	
	int addKeyInOrder(Key k){
		for (int i = 0; i < key.size(); i++){
			if (k.compareTo(key.get(i)) <0){//< strictly less than
				key.add(i, k);
				return i;
			}
		}
		key.add(k);
		return key.size()-1;
	}
	
	boolean removeKey(Key toBeRemoved){
		for(int i = 0; i <key.size(); i++){
			if(key.get(i).compareTo(toBeRemoved)==0){
				key.remove(i);
				return true;
			}
		}
		return false;
	}
	Key removeKey(int pos){
		return key.remove(pos);
	}
	
	/**
	 * Takes in a key and returns the position of child that the key would go to <br>
	 * Ex: [0,1,2](3)[3,4](5)[5,6,7,8,...]
	 * @param k key
	 * @return position of child that the key would go to
	 */
	int getChildPosition (Key k){
		for (int i = 0; i < key.size(); i++){
			if (k.compareTo(key.get(i)) <0){//< strictly less than
				return i;
			}
		}
		return key.size();
	}

	
	public ArrayList <Key> getKeyArray(){
		return key;
	}
	
	Key getKey (int pos){
		return key.get(pos);
	}
	
	boolean isFullNode(){
		if (key.size() == MAX_KEY_SIZE)
			return true;
		return false;
	}
	
	public String toString(){
		String output = "";
		output = "[";
		for(int i = 0; i <key.size(); i++){
			output = output + " " + key.get(i);
		}
		output = output + "]";
		return output;
	}
}