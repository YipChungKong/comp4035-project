
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
			for (int i = 1; i <= 4; i++) {
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

	public void PushUp() {
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
					for(int i = 1; i<=4; i++){
						targetNodes.entries[i] = overFlowKeys[i-1];
					}
					// Rearrange sidling node, all the node forward 1 index, and input the largest one overflow key to the first one
					for(int i = 4; i>1; i--){
						targetNodes.leafSidling.entries[i] = targetNodes.leafSidling.entries[i-1];
					}
					targetNodes.leafSidling.entries[1] = overFlowKeys[4];
					// The parent Index should change to overFlowKey
					targetNodes.parentNode.entries[FindParentIndex(targetNodes.parentNode,targetNodes.leafSidling.entries[targetNodes.leafSidling.entries[0].key].key)] = overFlowKeys[4];
				}
			}
			if(targetNodes.leafSidling==null || targetNodes.leafSidling.entries[0].key==4){
				// No sidling or sidling is full, we use copy up, check parent full or not
				if(targetNodes.parentNode.entries[0].key<4){
					
				}
			}
		}
	};

	public int FindParentIndex(Nodes parent, int largestKey){
		for(int i=1; i<=4;i++){
			if(parent.entries[i]==null){
				return i-1;
			}else{
				if(parent.entries[i].key>largestKey){
					return i-1;
				}
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
			currentNodes = currentNodes.leafSidling;
		}
		System.out.println("");
		System.out.println("End");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BTree bTree = new BTree();
		bTree.Insert(8, 0);
		bTree.Insert(6, 0);
		bTree.Insert(7, 0);
		bTree.Insert(1, 0);
		bTree.Insert(2, 0);
		bTree.Insert(5, 0);
		bTree.Insert(3, 0);
		bTree.Insert(4, 0);

		bTree.PrintAllLeafNode();
	}

}
