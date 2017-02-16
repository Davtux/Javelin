package fr.unilim.tree;

import java.util.Map;

/**
 * Represent a Binary Tree.
 */
public interface IBinaryTree {

	/**
	 * @return left child.
	 */
	IBinaryTree getLeft();
	
	/**
	 * @return right child.
	 */
	IBinaryTree getRight();
	
	/**
	 * @return node's values.
	 */
	Map<String, String> getValues();
	
	/**
	 * @param key
	 * @return If values contains key.
	 */
	boolean containsKey(String key);
}
