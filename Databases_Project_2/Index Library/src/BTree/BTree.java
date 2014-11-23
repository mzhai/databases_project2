package BTree;
import java.io.Serializable;
import java.util.ArrayList;
import Keys.*;
import java.util.LinkedList;
import java.util.Queue;
/**
 * This class is the implementation for a B+ Tree.<br>
 * Each node contains the data (the keys), a pointer to its parent, pointers to each of the children, 
 * and if the node is a leaf, a pointer to its right neighbor.<br>
 * The user can get the root node, which is the root of the tree. The user can get the start node, 
 * which is the start of the leftmost leaf (for in order traversal of the keys). The user can get the 
 * order of the tree.<br>
 * The user can insert a key, delete a key, search for a key, see if the tree contains a key, search 
 * for keys greater than an input key, search for keys less than an input key, or search for keys 
 * between two keys. The user can also print the tree in breadth-first order.<br>
 * The ordering of the keys depends on the Comparator of the Key object. Keys that are passed in as 
 * arguments must be of the type Key (Key is an abstract class). Key class implements Comparable. <br>
 * The methods in this class are synchronized to support concurrency.<br>
 * This class is serializable.
 * @author May Zhai
 * @author Bill Ge
 * 
 */
public class BTree implements Serializable {
	private int order;
	private BNode root;
	private BNode start;

	/* Constructors */
	/**
	 * Constructs a B+ Tree of order 1
	 */
	public BTree (){
		this(1);
	}

	/**
	 * Constructs a B+ Tree of order <tt>ord</tt>
	 * @param ord order of the B+ Tree
	 */
	public BTree(int ord){
		order = ord;
		root = new BNode(ord);
		start = root;
	}

	/**
	 * 
	 * @return root of the tree
	 */
	public synchronized BNode getRoot(){
		return root;
	}

	/**
	 * 
	 * @return start of linked list of leaf nodes, or the leftmost leaf node
	 */
	public synchronized BNode getStart(){
		return start;
	}

	/**
	 * 
	 * @return order of the tree
	 */
	public synchronized int getOrder(){
		return order;
	}
	
	/**
	 * This method is different from search in that it returns a boolean, whether the key is in 
	 * the tree or not.
	 * @param k key to be searched
	 * @return <tt>true</tt> if tree contains the specified key, <tt>false</tt> otherwise
	 */
	public synchronized boolean contains(Key k){
		if (search(k).containsKey(k))
			return true;
		else
			return false;
	}

	/**
	 * This method is different from contains in that it returns a leaf BNode regardless of whether the 
	 * key is in the tree or not. If the key is not in the tree, it returns the leaf BNode where the key 
	 * is suposed to be.
	 * @param k key to be searched
	 * @return a leaf BNode that contains the key or is supposed to contain the key if the key doesn't exist in the tree
	 */
	public synchronized BNode search(Key k){
		BNode node = root;

		while (!node.isLeaf()){
//			System.out.println(node);
			node = node.getChild(node.getChildPosition((k)));
		}
//		System.out.println(node.getChildSize());
//		System.out.println(node.getChildArray().size());
		return node;
	}
	/**
	 * @param key key to be deleted
	 * @return boolean, whether or not the Node was deleted successfully
	 */
	public synchronized boolean deleteKey(Key key){
		if (key == null)
			return false;

		//if the tree contains the key, attempt to delete the key
		BNode temp = search(key);
		if(temp.removeKey(key)){
			return true;
		}

		//otherwise key doesn't exist within tree	
		return false;
	}
	/**
	 * @param k key to be inserted
	 * @return whether or not the object was inserted successfully
	 */
	public synchronized boolean insertKey(Key k){
		if (k == null)
			return false;

		if (contains(k))
			return false;

		BNode child1 = search(k);

		// Put key in Node
		
		// Puts key in the BNode in correct place, shifts all keys down
		if (!child1.isFullNode()){
			child1.addKeyInOrder(k);
			return true;
		}
		
		//child1 is a full node
		
		//Split
		BNode child2 = new BNode(order);

		//child overflows
		child1.addKeyInOrder(k);

		//add key to new child in order, move half to new node including mid
		for (int i = 0; i < child1.getMaxKeySize()/2+1; i++){
			child2.addKeyInOrder(child1.getKey((child1.getMaxKeySize()/2)));
			child1.removeKey((child1.getMaxKeySize()/2));
		}

		//key that's pushed out
		Key currentKey = child2.getKey(0);
		BNode parent1 = child1.getParent();

		while (true){
			//Split node was root
			if (parent1 == null){
				parent1 = new BNode(order);
				parent1.addKeyInOrder(currentKey);
				parent1.addChild(child1);
				parent1.addChild(child2);
				child1.setParent(parent1);
				child2.setParent(parent1);
				root = parent1;
				//code to set the link from child1 to child2
				child1.setRight(child2);
				child2.setLeft(child1);
				return true;
			}
			//Parent isn't full
			else if (!(parent1.isFullNode())){
				int keyPosition = parent1.addKeyInOrder(currentKey);//put pushed key in parent node
				parent1.addChild(child2, keyPosition+1);//add child2 to the parent node
				child2.setParent(parent1);//child2's parent is child1's parent
//				root = parent1;
				
				child1.setRight(child2);
				child2.setLeft(child1);
				
				return true;
			}
			//Parent is full, split again
			else{
				int parentInsertion = parent1.addKeyInOrder(currentKey);//overflows parent

				//Add half of parent's keys to 2nd parent, including the middle one
				BNode parent2 = new BNode(order);
				for (int i = 0; i < (parent1.getMaxKeySize()/2)+1; i++){
					parent2.addKeyInOrder(parent1.getKey((parent1.getMaxKeySize()/2)));
					parent1.removeKey((parent1.getMaxKeySize()/2));
				}

				//add child2 to parent1 to overflow it
				parent1.addChild(child2, parentInsertion+1);
				child2.setParent(parent1);

				//move half of children from parent1 to parent2
				for (int i = 0; i < parent1.getMaxChildSize()/2+1; i++){
					(parent1.getChild((parent1.getMaxChildSize()/2)+1)).setParent(parent2);
					parent2.addChild(parent1.getChild((parent1.getMaxChildSize()/2)+1));
					parent1.removeChild((parent1.getMaxChildSize()/2)+1);
				}
				//remove the middle key from parent2
				currentKey = parent2.removeKey(0);

				//child1 is connected to child2, but that's it
				child1.setRight(child2);
				child2.setLeft(child1);
				
				//update variables
				child1 = parent1;
				child2 = parent2;
				parent1 = child1.getParent(); //child2's parent is null
				/*recurses*/
			}
		}
	}

	/**
	 * prints out the tree in breadth-first order
	 */
	public synchronized void printTree(){
		Queue <BNode> q = new LinkedList <BNode> ();
		BNode n = root;
		q.offer(n);

		while ((n = q.poll()) != null){

			for (int i = 0; i<n.getKeySize(); i++){
				System.out.print("["+n.getKey(i)+"]");
			}
			System.out.println();
			for (int i = 0; i < n.getChildSize(); i++){
				q.offer(n.getChild(i));
			}
		}
	}
	
	/**
	 * Prints out a list of the the BNode leaves in ascending order
	 */
	public synchronized void printList(){
		BNode temp = getStart();
		while(temp!=null){
			for (int i = 0; i < temp.getKeySize(); i ++){
				System.out.print("["+temp.getKey(i)+"]");
			}
			temp = temp.getRight();
		}
		System.out.println();
	}	

	/**
	 * @param key key to base the search on
	 * @param inclusive if inclusive, also includes the given key in the list
	 * @return an ArrayList of the primary keys greater than the given key
	 */
	public synchronized ArrayList<Key> searchGreaterThan(Key key, boolean inclusive){
		BNode temp = this.start;
		ArrayList<Key> values = new ArrayList<Key>();
		boolean afterwards = false;
		while(temp!=null){
			ArrayList<Key> tempList =temp.getKeyArray();
			
			if(afterwards){
				values.addAll(tempList);
			}
			for(int i = 0; i <tempList.size();i++){
				if(afterwards){
					values.add(tempList.get(i));
				}
				else if(tempList.get(i).compareTo(key)==0){
					if(inclusive){
						values.add(tempList.get(i));
					}
					afterwards = true;
				}
				
			}
			temp = temp.getRight();			
		}
		
		if(values.size()>0){
			return values;
		}else{
			return null;
		}
	}
	
	/**
	 * @param key key to base the search on
	 * @param inclusive if inclusive, also includes the given key in the list
	 * @return an ArrayList of the primary keys less than the given key
	 */
	public synchronized ArrayList<Key> searchLessThan(Key key, boolean inclusive){
		BNode temp = this.start;
		ArrayList<Key> values = new ArrayList<Key>();
		while(temp!=null){
			ArrayList<Key> tempList =temp.getKeyArray();
			for(int i = 0; i <tempList.size();i++){
				if(tempList.get(i).compareTo(key)<0){
					values.add(tempList.get(i));
				}
				else if(inclusive && tempList.get(i).compareTo(key)==0){
					values.add(tempList.get(i));
				}
			}
			temp = temp.getRight();			
		}
		
		if(values.size()>0){
			return values;
		}else{
			return null;
		}
	}
	
	/**
	 * @param lower lower key to base the search on
	 * @param upper upper key to base the search on
	 * @param inclusive if inclusive, also includes the given key in the list
	 * @return a list of the primary keys within the range of the given keys
	 */
	public synchronized ArrayList<Key> searchInBetween(Key lower, Key upper, boolean inclusive){
		if (lower.compareTo(upper)> 0)
			return null;
		
		
		BNode temp = this.start;
		ArrayList<Key> values = new ArrayList<Key>();
		
		
		while(temp!=null){
			ArrayList<Key> tempList =temp.getKeyArray();
			for(int i = 0; i <tempList.size();i++){
				if(tempList.get(i).compareTo(lower)==0 && inclusive){					
					values.add(lower);
				}else if(tempList.get(i).compareTo(lower)>0 && tempList.get(i).compareTo(upper)<0){
					values.add(tempList.get(i));
					
				}else if(tempList.get(i).compareTo(upper)==0 && inclusive){
					values.add(tempList.get(i));
				}
			}
			temp = temp.getRight();			
		}
		
		if(values.size()>0){
			return values;
		}else{
			return null;
		}
	}

	
	/**
	 * 
	 * @return number of BNodes in BTree
	 */
    public int countNodes(){
        BNode temp = getStart();
        int counter = 0;
        while(temp!=null){
             counter++;
             temp = temp.getRight();
        }
        return counter;
   }
    
    /**
     * 
     * @return number of Keys in BTree
     */
   public int countEntries(){
        BNode temp = getStart();
        int counter = 0;
        while(temp!=null){
             counter+=temp.getKeySize();
             temp = temp.getRight();
        }
        return counter;
   }



}