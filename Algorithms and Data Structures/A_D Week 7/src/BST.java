/*************************************************************************
 *  Binary Search Tree class.
 *  Adapted from Sedgewick and Wayne.
 *
 *  @version 3.0 1/11/15 16:49:42
 *
 *  @author Gregory Partridge
 *
 *************************************************************************/

import java.util.NoSuchElementException;

public class BST<Key extends Comparable<Key>, Value> {
    private Node root;             // root of BST

    /**
     * Private node class.
     */
    private class Node {
        private Key key;           // sorted by key
        private Value val;         // associated data
        private Node left, right;  // left and right subtrees
        private int N;             // number of nodes in subtree

        public Node(Key key, Value val, int N) {
            this.key = key;
            this.val = val;
            this.N = N;
        }
    }

    // is the symbol table empty?
    public boolean isEmpty() 
    {
    	return size() == 0;
    }

    // return number of key-value pairs in BST
    public int size() 
    {
    	return size(root);
    }

    // return number of key-value pairs in BST rooted at x
    private int size(Node x)
    {
        if (x == null) return 0;
        else return x.N;
    }

    /**
     *  Search BST for given key.
     *  Does there exist a key-value pair with given key?
     *
     *  @param key the search key
     *  @return true if key is found and false otherwise
     */
    public boolean contains(Key key) 
    {
        return get(key) != null;
    }

    /**
     *  Search BST for given key.
     *  What is the value associated with given key?
     *
     *  @param key the search key
     *  @return value associated with the given key if found, or null if no such key exists.
     */
    public Value get(Key key) 
    {
    	return get(root, key);
    }

    private Value get(Node x, Key key) 
    {
        if (x == null)
        {
        	return null;
        }
        
        int cmp = key.compareTo(x.key);
        
        if(cmp < 0)
        {
        	return get(x.left, key);
        }
        else if(cmp > 0)
        {
        	return get(x.right, key);
        }
        else
        {
        	return x.val;
        }
    }

    /**
     *  Insert key-value pair into BST.
     *  If key already exists, update with new value.
     *
     *  @param key the key to insert
     *  @param val the value associated with key
     */
    public void put(Key key, Value val) 
    {
        if (val == null) 
        {
        	delete(key);
        	return;
        }
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val)
    {
        if (x == null)
        	{
        		return new Node(key, val, 1);
        	}
        int cmp = key.compareTo(x.key);
        if(cmp < 0)
        {
        	x.left  = put(x.left,  key, val);
        }
        else if(cmp > 0)
        {
        	x.right = put(x.right, key, val);
        }
        else
        {
        	x.val   = val;
        }
        x.N = 1 + size(x.left) + size(x.right);
        return x;
    }

    /**
     * Tree height.
     *
     * Asymptotic worst-case running time using Theta notation: TODO
     *
     * @return the number of links from the root to the deepest leaf.
     *
     * Example 1: for an empty tree this should return -1.
     * Example 2: for a tree with only one node it should return 0.
     * Example 3: for the following tree it should return 2.
     *   B
     *  / \
     * A   C
     *      \
     *       D
     */
    public int height() 
    {
    	if (isEmpty()) return -1;
    	else return height(root);
    }
    	
    private int height(Node x)
    {
    	if(x != null)
    	{
    		int leftOfNode = height(x.left);
        	int rightOfNode = height(x.right);        	
        	if(leftOfNode > rightOfNode) return ++leftOfNode;
        	else return ++rightOfNode;
    	}
    	return -1;
    }

    /**
     * Median key.
     * If the tree has N keys k1 < k2 < k3 < ... < kN, then their median key 
     * is the element at position (N+1)/2 (where "/" here is integer division)
     *
     * @return the median key, or null if the tree is empty.
     */
    public Key median() {
      if (isEmpty()) return null;
      int offset = 0;
      return median(root, offset);
    }
    
    private Key median(Node x, int offset)
    {
    	Key returnKey;
    	int cmp = new Integer (size(x.left)+offset).compareTo((size()-1)/2);
    	if(cmp>0) returnKey = median(x.left, offset);
    	else if(cmp<0) returnKey = median(x.right, offset+size(x.left)+1);
    	else returnKey = x.key;
    	return returnKey;
    }
    
    /**
     * Print all keys of the tree in a sequence, in-order.
     * That is, for each node, the keys in the left subtree should appear before the key in the node.
     * Also, for each node, the keys in the right subtree should appear before the key in the node.
     * For each subtree, its keys should appear within a parenthesis.
     *
     * Example 1: Empty tree -- output: "()"
     * Example 2: Tree containing only "A" -- output: "(()A())"
     * Example 3: Tree:
     *   B
     *  / \
     * A   C
     *      \
     *       D
     *
     * output: "((()A())B(()C(()D())))"
     *
     * output of example in the assignment: (((()A(()C()))E((()H(()M()))R()))S(()X()))
     *
     * @return a String with all keys in the tree, in order, parenthesized.
     */
    public String printKeysInOrder() {
      if (isEmpty()) return "()";
      String returnString = "(";      
      return returnString = printKeysInOrder(root, returnString) + ")";
    }
    
    private String printKeysInOrder(Node x, String returnString)
    {
    	if(x.left == null)returnString += "()";
    	else
    	{
    		returnString += "(";
    		returnString = printKeysInOrder(x.left, returnString);
    		returnString += ")";
    	}
    	returnString += x.val;
    	if(x.right == null)returnString += "()";
    	else
    	{
    		returnString += "(";
    		returnString = printKeysInOrder(x.right, returnString);
    		returnString += ")";
    	}
    	return returnString;
    }
    
    /**
     * Pretty Printing the tree. Each node is on one line -- see assignment for details.
     *
     * @return a multi-line string with the pretty ascii picture of the tree.
     */
    public String prettyPrintKeys() 
    {
    	String prefix = "";
    	String returnString = "";
    	return prettyPrintKeys(root, prefix, returnString);
    }
    
    private String prettyPrintKeys(Node node, String prefix, String returnString)
    {
    	if(node == null)returnString+=prefix+"-null\n";    		
    	else
    	{
    		returnString+=prefix+"-"+node.val+"\n";
    		String prefixForLeft = prefix + " |";
    		String prefixForRight = prefix + "  ";
    		returnString = prettyPrintKeys(node.left, prefixForLeft, returnString);
    		returnString = prettyPrintKeys(node.right, prefixForRight, returnString);
    	}
    	return returnString;
    }

    /**
     * Deletes a key from a tree (if the key is in the tree).
     * Note that this method works symmetrically from the Hibbard deletion:
     * If the node to be deleted has two child nodes, then it needs to be
     * replaced with its predecessor (not its successor) node.
     *
     * @param key the key to delete
     */
    public void delete(Key key) 
    {
    	Node nodeReplacing;
    	
    	if(size() == 0) return;
    	if(!contains(key)) return;
    	Node parentNode = findParentNode(root, key);
    	Node nodeToDelete = findNodeToDelete(parentNode, key);
    	
    	if(nodeToDelete.left == null && nodeToDelete.right == null) 
    	{
    		if(parentNode.left == nodeToDelete) parentNode.left = null;
    		else parentNode.right = null;
    	}
    	else if(nodeToDelete.left == null)
    	{
    		if(parentNode.left == nodeToDelete) parentNode.left = nodeToDelete.right;
    		else parentNode.right = nodeToDelete.right;
    	}
    	else if(nodeToDelete.right == null)
    	{
    		if(parentNode.left == nodeToDelete) parentNode.left = nodeToDelete.left;
    		else parentNode.right = nodeToDelete.left;
    	}
    	else
    	{
    		if(parentNode.left == nodeToDelete)
    		{
    			nodeReplacing = findMax(nodeToDelete.left);
    			
    			delete(findMax(nodeToDelete.left).key);
    			
    			nodeReplacing.left = nodeToDelete.left;
    			nodeReplacing.right = nodeToDelete.right;
    			
    			parentNode.left = nodeReplacing;    			
    		}
    		else 
    		{
    			nodeReplacing = findMax(nodeToDelete.left);
    			
    			delete(findMax(nodeToDelete.left).key);
    			
    			nodeReplacing.left = nodeToDelete.left;
    			nodeReplacing.right = nodeToDelete.right;
    			
    			parentNode.right = nodeReplacing;  
    		}
    	}
    }
    
    
    
    private Node findMax(Node currentNode)
    {
    	if(currentNode.right != null) return findMax(currentNode.right);
		return currentNode;
    }
    
    private Node findParentNode(Node x, Key key)
    {
    	if(x == null) return null;
    	int cmp = key.compareTo(x.key);
    	if(x.left != null)
    	{
    		if(x.left.key == key) return x;
    	}
    	if(x.right != null)
    	{
    		if(x.right.key == key) return x;
    	}
    	if (cmp < 0) return findParentNode(x.left, key);
    	else if(cmp > 0) return findParentNode(x.right, key);
    	return null;
    }
    
    private Node findNodeToDelete(Node parentNode, Key key)
    {
    	if(parentNode.left != null)
    	{
    			if(parentNode.left.key == key) return parentNode.left;
    	}
    	return parentNode.right;
    }
    
}