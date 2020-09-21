package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		TrieNode start = new TrieNode(null,null,null);
		
		for(int i = 0;  i< allWords.length; i++) {
			start = build(start,allWords,i,false);
		}
		
		return start;
	}
	
	
	public static TrieNode build(TrieNode first, String [] words,int i,boolean internal) {
		
		Indexes index = new Indexes(i,(short) 0,(short)(words[i].length()-1));
		
		TrieNode build = new TrieNode(index,null,null);
		
		if(first.substr == null && first.firstChild == null && first.sibling == null) {
			// adding first node
			first.firstChild = build;
			return first;
		}

		
		TrieNode head = first, ptr = head.firstChild, prev = null;

 		
		while(!internal && ptr != null && !(similarPrefix(words[ptr.substr.wordIndex].substring(ptr.substr.startIndex),words[i]))){//Case #1 new letter
				prev = ptr;
				ptr = ptr.sibling;
 		}
 		
		while(internal && ptr != null && !(similarPrefix(words[ptr.substr.wordIndex].substring(ptr.substr.startIndex),words[i].substring(head.substr.endIndex+1)))) {
 			prev = ptr;
			ptr = ptr.sibling;
 		}
		
		if(ptr == null) {		
			// To add new prefix
			if(internal) {
				build.substr.startIndex = (short) (head.substr.endIndex + 1);
			}
			prev.sibling = build;
			return head;
		}
		
		else {
			
			if(ptr.firstChild == null) {
				String prefix = getPrefix(words[i],words[ptr.substr.wordIndex]);
				Indexes parentIndex = new Indexes(ptr.substr.wordIndex,(short)0,(short)(prefix.length()-1));
				
				if(head.substr != null) {
					parentIndex.startIndex = (short) (head.substr.endIndex + 1);
				}
				
				TrieNode parent = new TrieNode(parentIndex,ptr,ptr.sibling);
				ptr.substr.startIndex = (short) prefix.length();
				build.substr.startIndex = (short) prefix.length();
				ptr.sibling = build;
				
				if(prev != null) {
					prev.sibling = parent;
				}
				
				else {
					head.firstChild = parent;
				}
				
				return head;
			}
			
			else {
				String prefix = words[ptr.substr.wordIndex].substring(0, ptr.substr.endIndex+1);
				//to rotate
				if(!(words[i].substring(0,prefix.length()).contains(prefix))) {
					// for rotation
					String newPre = getPrefix(words[i],prefix);
					
					Indexes parentInd = new Indexes(ptr.substr.wordIndex,(short)0, (short)(newPre.length()-1));
					
					ptr.substr.startIndex = (short) newPre.length();
					
					build.substr.startIndex = (short) newPre.length();
					
					ptr.sibling = build;
					
					TrieNode newParent = new TrieNode(parentInd,ptr,null);
					
					if(prev != null) {
						prev.sibling = newParent;
					}
					
					else {
						head.firstChild = newParent;
					}
					
					return head;
				}
				ptr = build(ptr,words,i,true);
				return head;
			}

		}
	}
	
	private static boolean similarPrefix(String word1,String word2) {
		//If the prefix is the same
		return(word1.charAt(0) == word2.charAt(0));
	}
	
	private static String getPrefix(String x,String y) {
		String pre = "";
		int min = x.length() < y.length() ? x.length():y.length();
		
		for(int i = 0;i < min; i++) {
			
			if(x.charAt(i) != y.charAt(i)) {
				break;
			}
			pre += x.substring(i,i+1);
		}
		return pre;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		ArrayList<TrieNode> list = new ArrayList<TrieNode>();
		return  completionRecursive(root,allWords,prefix, list);
	}
	
	public static ArrayList<TrieNode> completionRecursive(TrieNode head,
			String[] allWords, String prefix, ArrayList<TrieNode> list) {

		if(head == null) {
			return list;
		}
	
		if(head.substr == null) {
			//This is for the Root
			list = completionRecursive(head.firstChild , allWords,prefix,list);
			
			if(list.size() == 0) {
				return null;
			}
			return list;
		}
		
		if(similarPrefix(prefix,allWords[head.substr.wordIndex])) {
			//If they start with the same letter
			if(prefix.equals(allWords[head.substr.wordIndex].substring(0,head.substr.endIndex+1))) {
				if(allWords[head.substr.wordIndex].length() == allWords[head.substr.wordIndex].substring(0,head.substr.endIndex+1).length()) {
					list.add(head);
					return list;
				}
				return completionRecursive(head.firstChild , allWords,prefix,list);
			}
			
			else if(allWords[head.substr.wordIndex].contains(prefix)) {
				
				if((head.substr.endIndex + 1) == (allWords[head.substr.wordIndex].length())) {
					list.add(head);
					return completionRecursive(head.sibling , allWords,prefix,list);
				}
				list = completionRecursive(head.firstChild , allWords,prefix,list);
				list = completionRecursive(head.sibling , allWords,prefix,list);
				return list;
			}
			list = completionRecursive(head.firstChild , allWords,prefix,list);
			list = completionRecursive(head.sibling , allWords,prefix,list);
			return list;
		}
		return completionRecursive(head.sibling , allWords,prefix,list);
		// if they are not the same
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
