import java.lang.annotation.Target;

public class BTree {
	int order = 2; // max number of node is order*2
	Nodes root = new Nodes(); // It should be LeafNode as root is the leaf at the first 5 keys
	Nodes start = root; // first leaf node array
	int height = 0; // height start from 0

	class Nodes {
		Node[] entries = new Node[order * 2 + 1]; // store searching key, first entry uses to store number of key
													// in the
		// array
		Nodes[] indexPointer = new Nodes[entries.length]; // pointer for next level index, only use when it's
															// not leaf node
		Nodes leafSidling; // pointer for locating leaf node, only use when it's
							// leaf node , It's only point to next leaf node

		Nodes parentNode;
		boolean isLeafNode = true;

		Nodes() {
			this.entries[0] = new Node(0);
			this.isLeafNode = true;
		}
	}

	class Node {
		int key;
		int rid;

		Node(int key, int rid) {
			this.key = key;
			this.rid = rid;
		}

		Node(int key) {
			this.key = key;
			this.rid = 0;
		}
	}

	public void Insert(int key, int rid) {
		Nodes currentNode = root;
		while (currentNode.isLeafNode == false) { // Find the nodes should store the key first, scan from root until the
													// Nodes.isLeafNode = true
			for (int i = 1; i <= 5; i++) {
				if (i > 4) {
					currentNode = currentNode.indexPointer[4];
					break;
				}
				if (currentNode.entries[i] == null) {
					currentNode = currentNode.indexPointer[i - 1];
					break;
				} else {
					if (currentNode.entries[i].key > key) {
						currentNode = currentNode.indexPointer[i - 1];
						break;
					}
				}
			}
		}
		if (currentNode.entries[0].key == 4) {
			// full
			CopyUp(currentNode, key, rid);
		} else {
			for (int i = 1; i <= 4; i++) {
				if (currentNode.entries[i] == null) {
					currentNode.entries[i] = new Node(key, rid); // Insert complete add entries[0] count
					currentNode.entries[0].key++;
					break;
				} else {
					if (currentNode.entries[i].key > key) {
						int moveEntriesIndex = currentNode.entries[0].key; // Move from last one
						while (moveEntriesIndex >= i) {
							currentNode.entries[moveEntriesIndex + 1] = currentNode.entries[moveEntriesIndex];
							moveEntriesIndex--;
						}
						currentNode.entries[i] = new Node(key, rid);
						currentNode.entries[0].key++;
						break;
					}
				}
			}
		}
	}

	public void PushUp(Nodes targetNodes, int overFlowKey, int key, int rid) { // get key and rid for re insert after
																				// PushUp, get overFlowKey to calculate
																				// the PushUp of parent(which one be the
																				// new parent)
		Node[] overFlowParent = new Node[5];
		int sortedOverFlowKeyPosition = 0; // Remember the position of the overflow number for assign the indexPointer
		for (int i = 0; i < 4; i++) {
			// Put all the key of targetNodes to the overFlow one first
			overFlowParent[i] = targetNodes.parentNode.entries[i + 1];
		}
		// compare Key and overFlow one by one
		if (overFlowParent[3].key < overFlowKey) {
			overFlowParent[4] = new Node(overFlowKey, rid);
			sortedOverFlowKeyPosition = 4;
		} else {
			int sortPointer = 0;
			while (overFlowParent[sortPointer].key < overFlowKey) {
				sortPointer++;
			}
			int moveEntriesIndex = 3; // Move from last one
			while (moveEntriesIndex >= sortPointer) {
				overFlowParent[moveEntriesIndex + 1] = overFlowParent[moveEntriesIndex];
				moveEntriesIndex--;
			}
			overFlowParent[sortPointer] = new Node(overFlowKey, rid);
			sortedOverFlowKeyPosition = sortPointer;
		}
		if (targetNodes.parentNode == root) {
			// Open two indexNode, store the data of root
			Nodes leftIndexNode = new Nodes();
			Nodes rightIndexNode = new Nodes();
			leftIndexNode.isLeafNode = false;
			rightIndexNode.isLeafNode = false;
			if (sortedOverFlowKeyPosition <= 2) {
				// Put parent[2] to be new parent
				leftIndexNode.entries[0].key = 1;
				rightIndexNode.entries[0].key = 2;
				leftIndexNode.entries[1] = targetNodes.parentNode.entries[1];
				leftIndexNode.indexPointer[0] = targetNodes.parentNode.indexPointer[0];
				leftIndexNode.indexPointer[1] = targetNodes.parentNode.indexPointer[1];
				rightIndexNode.entries[1] = targetNodes.parentNode.entries[3];
				rightIndexNode.entries[2] = targetNodes.parentNode.entries[4];
				rightIndexNode.indexPointer[0] = targetNodes.parentNode.indexPointer[2];
				rightIndexNode.indexPointer[1] = targetNodes.parentNode.indexPointer[3];
				rightIndexNode.indexPointer[2] = targetNodes.parentNode.indexPointer[4];
				targetNodes.parentNode.entries[1] = targetNodes.parentNode.entries[2];
			} else {
				// Put parent[3] to be new parent
				leftIndexNode.entries[0].key = 2;
				rightIndexNode.entries[0].key = 1;
				leftIndexNode.entries[1] = targetNodes.parentNode.entries[1];
				leftIndexNode.entries[2] = targetNodes.parentNode.entries[2];
				leftIndexNode.indexPointer[0] = targetNodes.parentNode.indexPointer[0];
				leftIndexNode.indexPointer[1] = targetNodes.parentNode.indexPointer[1];
				leftIndexNode.indexPointer[2] = targetNodes.parentNode.indexPointer[2];
				rightIndexNode.entries[1] = targetNodes.parentNode.entries[4];
				rightIndexNode.indexPointer[0] = targetNodes.parentNode.indexPointer[3];
				rightIndexNode.indexPointer[1] = targetNodes.parentNode.indexPointer[4];
				targetNodes.parentNode.entries[1] = targetNodes.parentNode.entries[3];
			}
			// Reset root
			targetNodes.parentNode.entries[0].key = 1;
			targetNodes.parentNode.entries[1] = targetNodes.parentNode.entries[2];
			targetNodes.parentNode.entries[2] = null;
			targetNodes.parentNode.entries[3] = null;
			targetNodes.parentNode.entries[4] = null;
			targetNodes.parentNode.indexPointer[0] = leftIndexNode;
			targetNodes.parentNode.indexPointer[1] = rightIndexNode;
			targetNodes.parentNode.indexPointer[2] = null;
			targetNodes.parentNode.indexPointer[3] = null;
			targetNodes.parentNode.indexPointer[4] = null;
			// Set leafNode parent
			leftIndexNode.parentNode = root;
			rightIndexNode.parentNode = root;
			leftIndexNode.indexPointer[0].parentNode = leftIndexNode;
			leftIndexNode.indexPointer[1].parentNode = leftIndexNode;
			rightIndexNode.indexPointer[0].parentNode = rightIndexNode;
			rightIndexNode.indexPointer[1].parentNode = rightIndexNode;
			if (sortedOverFlowKeyPosition > 2) {
				leftIndexNode.indexPointer[2].parentNode = leftIndexNode;
			} else {
				rightIndexNode.indexPointer[2].parentNode = rightIndexNode;
			}
		} else {
			// Splite Node, add one empty node first
			Nodes rightIndexNode = new Nodes();
			rightIndexNode.isLeafNode = false;
			rightIndexNode.parentNode = targetNodes.parentNode.parentNode;
			if (sortedOverFlowKeyPosition <= 2) {
				// Put parent[2] to be new parent
				rightIndexNode.entries[0].key = 2;
				rightIndexNode.entries[1] = targetNodes.parentNode.entries[3];
				rightIndexNode.entries[2] = targetNodes.parentNode.entries[4];
				rightIndexNode.indexPointer[0] = targetNodes.parentNode.indexPointer[2];
				rightIndexNode.indexPointer[1] = targetNodes.parentNode.indexPointer[3];
				rightIndexNode.indexPointer[2] = targetNodes.parentNode.indexPointer[4];

				targetNodes.parentNode.entries[0].key = 1;
				targetNodes.parentNode.entries[2] = null;
				targetNodes.parentNode.entries[3] = null;
				targetNodes.parentNode.entries[4] = null;
				targetNodes.parentNode.indexPointer[2] = null;
				targetNodes.parentNode.indexPointer[3] = null;
				targetNodes.parentNode.indexPointer[4] = null;
			} else {
				// Put parent[3] to be new parent
				rightIndexNode.entries[0].key = 1;
				rightIndexNode.entries[1] = targetNodes.parentNode.entries[4];
				rightIndexNode.indexPointer[0] = targetNodes.parentNode.indexPointer[3];
				rightIndexNode.indexPointer[1] = targetNodes.parentNode.indexPointer[4];
				targetNodes.parentNode.entries[0].key = 2;
				targetNodes.parentNode.entries[3] = null;
				targetNodes.parentNode.entries[4] = null;
				targetNodes.parentNode.indexPointer[3] = null;
				targetNodes.parentNode.indexPointer[4] = null;
			}
			int parentIndex = FindParentIndex(targetNodes.parentNode.parentNode,
					targetNodes.parentNode.entries[2].key);
			for (int i = 4; i > parentIndex + 1; i--) { // do 4-parentIndex-1 time, because, if the
				// parentNode not full, we need to move key3 to 4...
				// 2 to 3 ... until i+1 can be replace
				targetNodes.parentNode.parentNode.entries[i] = targetNodes.parentNode.parentNode.entries[i - 1];
				targetNodes.parentNode.parentNode.indexPointer[i] = targetNodes.parentNode.parentNode.indexPointer[i
						- 1];
			}
			// After rearrange, we assign pointer to new node, and the key to parent index
			targetNodes.parentNode.parentNode.entries[parentIndex + 1] = targetNodes.parentNode.entries[2];
			targetNodes.parentNode.parentNode.indexPointer[parentIndex + 1] = rightIndexNode;
			targetNodes.parentNode.parentNode.entries[0].key++;
			// Set leafNode parent
			rightIndexNode.indexPointer[0].parentNode = rightIndexNode;
			rightIndexNode.indexPointer[1].parentNode = rightIndexNode;
			if (sortedOverFlowKeyPosition <= 2) {
				rightIndexNode.indexPointer[2].parentNode = rightIndexNode;
			}
		}
		Insert(key, rid);
	};

	public void CopyUp(Nodes targetNodes, int key, int rid) {
		// find the copy up key first, we new a int array to store 5 key and sort it
		Node[] overFlowKeys = new Node[5];
		for (int i = 0; i < 4; i++) {
			// Put all the key of targetNodes to the overFlow one first
			overFlowKeys[i] = targetNodes.entries[i + 1];
		}
		// compare Key and overFlow one by one
		if (overFlowKeys[3].key < key) {
			overFlowKeys[4] = new Node(key, rid);
		} else {
			int sortPointer = 0;
			while (overFlowKeys[sortPointer].key < key) {
				sortPointer++;
			}
			int moveEntriesIndex = 3; // Move from last one
			while (moveEntriesIndex >= sortPointer) {
				overFlowKeys[moveEntriesIndex + 1] = overFlowKeys[moveEntriesIndex];
				moveEntriesIndex--;
			}
			overFlowKeys[sortPointer] = new Node(key, rid);
		}
		// check if root
		if (targetNodes == root) {
			// new two leafNode, and change root to indexNode
			targetNodes.isLeafNode = false;
			Nodes leftLeafNode = new Nodes();
			Nodes rightLeafNode = new Nodes();
			leftLeafNode.entries[0].key = 2;
			rightLeafNode.entries[0].key = 3;
			leftLeafNode.entries[1] = overFlowKeys[0];
			leftLeafNode.entries[2] = overFlowKeys[1];
			leftLeafNode.leafSidling = rightLeafNode;
			leftLeafNode.parentNode = targetNodes;
			rightLeafNode.entries[1] = overFlowKeys[2];
			rightLeafNode.entries[2] = overFlowKeys[3];
			rightLeafNode.entries[3] = overFlowKeys[4];
			rightLeafNode.parentNode = targetNodes;
			targetNodes.entries[0].key = 1;
			targetNodes.entries[1] = overFlowKeys[2];
			targetNodes.entries[2] = null;
			targetNodes.entries[3] = null;
			targetNodes.entries[4] = null;
			targetNodes.indexPointer[0] = leftLeafNode;
			targetNodes.indexPointer[1] = rightLeafNode;
			start = leftLeafNode;
		} else {
			// If not a root, we have to check the sidling of targetNodes
			if (targetNodes.leafSidling != null) {
				if (targetNodes.leafSidling.entries[0].key < 4) {
					// sidling not full, we using re-distribution first
					// Assign the first 4 sorted overFlowkey to targetNodes
					for (int i = 1; i <= 4; i++) {
						targetNodes.entries[i] = overFlowKeys[i - 1];
					}
					// Rearrange sidling node, all the node forward 1 index, and input the largest
					// one overflow key to the first one
					for (int i = 4; i > 1; i--) {
						targetNodes.leafSidling.entries[i] = targetNodes.leafSidling.entries[i - 1];
					}
					targetNodes.leafSidling.entries[1] = overFlowKeys[4];
					targetNodes.leafSidling.entries[0].key++;
					// The parent Index should change to overFlowKey
					targetNodes.parentNode.entries[FindParentIndex(targetNodes.parentNode,
							targetNodes.leafSidling.entries[targetNodes.leafSidling.entries[0].key].key)] = overFlowKeys[4];
				} else {
					// No sidling or sidling is full, we use copy up, check parent full or not
					if (targetNodes.parentNode.entries[0].key < 4) {
						// Open new nodes
						Nodes rightLeafNodes = new Nodes();
						rightLeafNodes.entries[0].key = 3;
						rightLeafNodes.entries[1] = overFlowKeys[2];
						rightLeafNodes.entries[2] = overFlowKeys[3];
						rightLeafNodes.entries[3] = overFlowKeys[4];
						rightLeafNodes.leafSidling = targetNodes.leafSidling;
						rightLeafNodes.parentNode = targetNodes.parentNode;
						targetNodes.leafSidling = rightLeafNodes;
						targetNodes.entries[0].key = 2;
						targetNodes.entries[1] = overFlowKeys[0];
						targetNodes.entries[2] = overFlowKeys[1];
						targetNodes.entries[3] = null;
						targetNodes.entries[4] = null;
						int parentIndex = FindParentIndex(targetNodes.parentNode, overFlowKeys[4].key);
						for (int i = 4; i > parentIndex + 1; i--) { // do 4-parentIndex-1 time, because, if the
																	// parentNode not full, we need to move key3 to 4...
																	// 2 to 3 ... until i+1 can be replace
							targetNodes.parentNode.entries[i] = targetNodes.parentNode.entries[i - 1];
							targetNodes.parentNode.indexPointer[i] = targetNodes.parentNode.indexPointer[i - 1];
						}
						// After rearrange, we assign pointer to new node, and the key to parent index
						targetNodes.parentNode.entries[parentIndex + 1] = overFlowKeys[2];
						targetNodes.parentNode.indexPointer[parentIndex + 1] = rightLeafNodes;
						targetNodes.parentNode.entries[0].key++;
					} else {
						PushUp(targetNodes, overFlowKeys[2].key, key, rid);
					}
				}
			} else {
				// No sidling or sidling is full, we use copy up, check parent full or not
				if (targetNodes.parentNode.entries[0].key < 4) {
					// Open new nodes
					Nodes rightLeafNodes = new Nodes();
					rightLeafNodes.entries[0].key = 3;
					rightLeafNodes.entries[1] = overFlowKeys[2];
					rightLeafNodes.entries[2] = overFlowKeys[3];
					rightLeafNodes.entries[3] = overFlowKeys[4];
					rightLeafNodes.leafSidling = targetNodes.leafSidling;
					rightLeafNodes.parentNode = targetNodes.parentNode;
					targetNodes.leafSidling = rightLeafNodes;
					targetNodes.entries[0].key = 2;
					targetNodes.entries[1] = overFlowKeys[0];
					targetNodes.entries[2] = overFlowKeys[1];
					targetNodes.entries[3] = null;
					targetNodes.entries[4] = null;
					int parentIndex = FindParentIndex(targetNodes.parentNode, overFlowKeys[4].key);
					for (int i = 4; i > parentIndex + 1; i--) { // do 4-parentIndex-1 time, because, if the parentNode
																// not full, we need to move key3 to 4... 2 to 3 ...
																// until i+1 can be replace
						targetNodes.parentNode.entries[i] = targetNodes.parentNode.entries[i - 1];
						targetNodes.parentNode.indexPointer[i] = targetNodes.parentNode.indexPointer[i - 1];
					}
					// After rearrange, we assign pointer to new node, and the key to parent index
					targetNodes.parentNode.entries[parentIndex + 1] = overFlowKeys[2];
					targetNodes.parentNode.indexPointer[parentIndex + 1] = rightLeafNodes;
					targetNodes.parentNode.entries[0].key++;
				} else {
					PushUp(targetNodes, overFlowKeys[2].key, key, rid);
				}
			}
		}
	};

	public int FindParentIndex(Nodes parent, int largestKey) {
		for (int i = 1; i <= 4; i++) {
			if (parent.entries[i] == null) {
				return i - 1;
			} else {
				if (parent.entries[i].key > largestKey) {
					return i - 1;
				}
			}
			if (i == 4) {
				return 4;
			}
		}
		return 1;
	}

	public void PrintAllLeafNode() {
		Nodes currentNodes = start;
		while (currentNodes != null) {
			for (int i = 1; i <= 4; i++) {
				if (currentNodes.entries[i] != null) {
					System.out.print(currentNodes.entries[i].key);
				} else {
					System.out.print("none");
				}
				System.out.print(", ");
			}
			System.out.print("  ||  ");
			currentNodes = currentNodes.leafSidling;
		}
		System.out.println("");
	}

	public void PrintRootNode() {
		for (int i = 1; i <= root.entries[0].key; i++) {
			System.out.print(root.entries[i].key + " ,");
		}
		System.out.println("");
	}

	public void PrintIndexNode(int height) {
		Nodes currentNodes = root.indexPointer[0];
		int currentPointer = 0;
		while (currentNodes != null) {
			for (int i = 1; i <= 4; i++) {
				if (currentNodes.entries[i] != null) {
					System.out.print(currentNodes.entries[i].key);
				} else {
					System.out.print("none");
				}
				System.out.print(", ");
			}
			System.out.print("  ||  ");
			if (currentPointer == 4) {
				break;
			}
			currentPointer += 1;
			currentNodes = root.indexPointer[currentPointer];
		}
	}

	public void PrintTree() {
		System.out.println("----------Print Tree----------");
		System.out.println("----------   Root   ----------");
		PrintRootNode();
		for (int i = 0; i < GetHeight() - 1; i++) {
			System.out.println("----------   Height " + (i + 1) + "  ----------");
			PrintIndexNode(i + 1);
			System.out.println("");
		}
		System.out.println("----------Leaf Node----------");
		PrintAllLeafNode();
		System.out.println("----------End print ----------");
	}

	public int GetHeight() {
		Nodes currentNodes = root;
		int height = 0;
		while (currentNodes.isLeafNode == false) {
			height++;
			currentNodes = currentNodes.indexPointer[0];
		}
		return height;
	}

	public void DeleteKey(int key, int rid) {
		Nodes currentNodes = start;
		while (currentNodes != null) {
			for (int i = 1; i <= 4; i++) {
				if (currentNodes.entries[i] != null) {
					if (currentNodes.entries[i].key == key) {
						// Have key, delete it
						currentNodes.entries[i] = null;
						// Rearrange the Nodes
						for (int j = i; j < 4; j++) {
							currentNodes.entries[j] = currentNodes.entries[j + 1];
						}
						// After rearrange, the last node before delete should delete(As it rearrange to
						// previous one)
						currentNodes.entries[currentNodes.entries[0].key] = null;
						currentNodes.entries[0].key--;
						if (currentNodes.entries[0].key == 1) {
							HandleUnderFlow(currentNodes);
						}
						System.out.println("Have key");
						return;
					} else if (currentNodes.entries[i].key > key) {
						System.out.println("Not have key");
						return;
					}
				}
			}
			currentNodes = currentNodes.leafSidling;
		}
		System.out.println("Not have key");
		return;
	}

	public void HandleUnderFlow(Nodes targetNodes) {
		if (targetNodes.isLeafNode == true) {
			if (targetNodes.leafSidling.entries[0].key > 2) {
				// Sibling can share key, re-distribution
				targetNodes.entries[2] = targetNodes.leafSidling.entries[1];
				targetNodes.entries[0].key++;
				// ReArrange sibling
				for (int i = 1; i < 4; i++) {
					targetNodes.leafSidling.entries[i] = targetNodes.leafSidling.entries[i + 1];
				}
				// After rearrange, the last node before delete should delete(As it rearrange to
				// previous one)
				targetNodes.leafSidling.entries[targetNodes.leafSidling.entries[0].key] = null;
				targetNodes.leafSidling.entries[0].key--;
				targetNodes.leafSidling.parentNode.entries[FindParentIndex(targetNodes.leafSidling.parentNode,
						targetNodes.leafSidling.entries[targetNodes.leafSidling.entries[0].key].key)] = targetNodes.leafSidling.entries[1];
			} else {
				MergeNodes(targetNodes);
			}
		} else {

		}
	}

	public void MergeNodes(Nodes targetNodes) {
		// TargetNodes store remaining 2 key from sibling, and rearrange the parent
		// Sibling must only if 2 key as, if 1 keys, it underFlow, we will solve it
		// before, and if it more than 3 key, we can use re-distribution
		targetNodes.entries[0].key += 2;
		targetNodes.entries[2] = targetNodes.leafSidling.entries[1];
		targetNodes.entries[3] = targetNodes.leafSidling.entries[2];
		int parentIndex = FindParentIndex(targetNodes.leafSidling.parentNode, targetNodes.leafSidling.entries[2].key);
		for (int i = parentIndex; i < 4; i++) {
			targetNodes.leafSidling.parentNode.entries[i] = targetNodes.leafSidling.parentNode.entries[i + 1];
			targetNodes.leafSidling.parentNode.indexPointer[i] = targetNodes.leafSidling.parentNode.indexPointer[i + 1];
		}
		targetNodes.leafSidling.parentNode.entries[targetNodes.leafSidling.parentNode.entries[0].key] = null;
		targetNodes.leafSidling.parentNode.indexPointer[targetNodes.leafSidling.parentNode.entries[0].key] = null;
		targetNodes.leafSidling.parentNode.entries[0].key--;
		if (targetNodes.leafSidling.parentNode.entries[0].key < 2) {
			PullDown(targetNodes.parentNode);
		}
		targetNodes.leafSidling = targetNodes.leafSidling.leafSidling;
	}

	public void PullDown(Nodes targetNodes) {
		int parentIndex = FindParentIndex(targetNodes.parentNode, targetNodes.entries[1].key);
		if (parentIndex != 0) { // !=0 means that it is not the first child node
			if (targetNodes.parentNode.indexPointer[parentIndex - 1].entries[0].key == 2) {// If the left index nodes,
																							// only have 2 key, we merge
																							// two index nodes, else we
																							// re-disturibtion
				Nodes leftIndexNode = targetNodes.parentNode.indexPointer[parentIndex - 1];
				leftIndexNode.entries[0].key += 2;
				leftIndexNode.entries[3] = targetNodes.parentNode.entries[parentIndex];
				leftIndexNode.entries[4] = targetNodes.entries[1];
				leftIndexNode.indexPointer[3] = targetNodes.indexPointer[0];
				leftIndexNode.indexPointer[4] = targetNodes.indexPointer[1];
				leftIndexNode.indexPointer[3].parentNode = leftIndexNode;
				leftIndexNode.indexPointer[4].parentNode = leftIndexNode;
				// Re arrange parent
				for (int i = parentIndex; i < 4; i++) {
					targetNodes.parentNode.entries[i] = targetNodes.parentNode.entries[i + 1];
					targetNodes.parentNode.indexPointer[i] = targetNodes.parentNode.indexPointer[i
							+ 1];
				}
				targetNodes.parentNode.entries[targetNodes.parentNode.entries[0].key] = null;
				targetNodes.parentNode.indexPointer[targetNodes.parentNode.entries[0].key] = null;
				targetNodes.parentNode.entries[0].key--;
				if(targetNodes.parentNode==root){
					if(root.entries[0].key==0){
						root = leftIndexNode;
						height--;
						return;
					}else{
						return;
					}
				}
				if (targetNodes.parentNode.entries[0].key < 2) {
					PullDown(targetNodes.parentNode);
				}
			} else {
				// Put leftIndexNode last index be the parent,
				Nodes leftIndexNode = targetNodes.parentNode.indexPointer[parentIndex - 1];
				targetNodes.entries[2] = targetNodes.entries[1];
				targetNodes.indexPointer[2] = targetNodes.indexPointer[1];
				targetNodes.indexPointer[1] = targetNodes.indexPointer[0];
				targetNodes.indexPointer[0] = leftIndexNode.indexPointer[leftIndexNode.entries[0].key];
				targetNodes.indexPointer[0].parentNode = targetNodes;
				targetNodes.entries[1] = targetNodes.parentNode.entries[parentIndex];
				targetNodes.parentNode.entries[parentIndex] = leftIndexNode.entries[leftIndexNode.entries[0].key];
				leftIndexNode.entries[leftIndexNode.entries[0].key] = null;
				leftIndexNode.indexPointer[leftIndexNode.entries[0].key] = null;
				leftIndexNode.entries[0].key--;
				targetNodes.entries[0].key++;
			}
		} else {
			if (targetNodes.parentNode.indexPointer[parentIndex + 1].entries[0].key == 2) {// If the left index nodes,
																							// only have 2 key, we merge
																							// two index nodes, else we
																							// re-disturibtion
				Nodes rightIndexNode = targetNodes.parentNode.indexPointer[parentIndex + 1];
				targetNodes.entries[2] = targetNodes.parentNode.entries[parentIndex + 1];
				targetNodes.entries[3] = rightIndexNode.entries[1];
				targetNodes.entries[4] = rightIndexNode.entries[2];
				targetNodes.indexPointer[2] = rightIndexNode.indexPointer[0];
				targetNodes.indexPointer[3] = rightIndexNode.indexPointer[1];
				targetNodes.indexPointer[4] = rightIndexNode.indexPointer[2];
				targetNodes.indexPointer[2].parentNode = targetNodes;
				targetNodes.indexPointer[3].parentNode = targetNodes;
				targetNodes.indexPointer[4].parentNode = targetNodes;
				targetNodes.entries[0].key += 3;
				// Re arrange parent
				for (int i = parentIndex + 1; i < 4; i++) {
					targetNodes.parentNode.entries[i] = targetNodes.parentNode.entries[i + 1];
					targetNodes.parentNode.indexPointer[i] = targetNodes.parentNode.indexPointer[i
							+ 1];
				}
				targetNodes.parentNode.entries[targetNodes.parentNode.entries[0].key] = null;
				targetNodes.parentNode.indexPointer[targetNodes.parentNode.entries[0].key] = null;
				targetNodes.parentNode.entries[0].key--;
				if(targetNodes.parentNode==root){
					if(root.entries[0].key==0){
						root =targetNodes;
						height--;
						return;
					}else{
						return;
					}
				}
				if (targetNodes.parentNode.entries[0].key < 2) {
					PullDown(targetNodes.parentNode);
				}
			} else {
				// Put rightIndexNode last index be the parent,
				Nodes rightIndexNode = targetNodes.parentNode.indexPointer[parentIndex - 1];
				targetNodes.entries[0].key++;
				targetNodes.entries[2] = targetNodes.parentNode.entries[parentIndex + 1];
				targetNodes.indexPointer[2] = rightIndexNode.indexPointer[0];
				targetNodes.indexPointer[2].parentNode = targetNodes;
				targetNodes.parentNode.entries[parentIndex + 1] = rightIndexNode.entries[1];
				for (int i = 0; i < 4; i++) {
					rightIndexNode.entries[i] = rightIndexNode.entries[i + 1];
					rightIndexNode.indexPointer[i] = rightIndexNode.indexPointer[i
							+ 1];
				}
				rightIndexNode.entries[rightIndexNode.entries[0].key] = null;
				rightIndexNode.indexPointer[rightIndexNode.entries[0].key] = null;
				rightIndexNode.entries[0].key--;
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BTree bTree = new BTree();
		bTree.Insert(50, 0);
		bTree.Insert(20, 0);
		bTree.Insert(30, 0);
		bTree.Insert(8, 0);
		bTree.Insert(1, 0);
		bTree.Insert(22, 0);
		bTree.Insert(46, 0);
		bTree.Insert(23, 0);
		bTree.Insert(28, 0);
		bTree.Insert(21, 0);
		bTree.Insert(27, 0);
		bTree.Insert(15, 0);
		bTree.Insert(10, 0);
		bTree.Insert(13, 0);
		bTree.Insert(3, 0);
		bTree.Insert(7, 0);
		bTree.Insert(-80, 0);
		bTree.Insert(-20, 0);
		bTree.Insert(-10, 0);
		bTree.Insert(2, 0);
		bTree.DeleteKey(22, 0);
		bTree.DeleteKey(13, 0);
		bTree.DeleteKey(20, 0);
		bTree.PrintTree();
	}

}
