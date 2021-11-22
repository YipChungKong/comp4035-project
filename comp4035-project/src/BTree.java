
public class BTree {
	int order = 2; // max number of node is order*2
	IndexNode root;
	LeafNode start; // first leaf node array
	int height = 0; // height start from 0

	class IndexNode {
		int[] entries = new int[order * 2 + 1]; // store searching key, first entry uses to store number of key in the
												// array
		IndexNode[] indexPointer = new IndexNode[entries.length]; // pointer for next level index, only use when it's
																	// not leaf node
		LeafNode[] leafPointer = new LeafNode[entries.length]; // pointer for locating leaf node, only use when it's
																// leaf node

		IndexNode(int key) {
			entries[0] = 1;
			entries[1] = key;
		}

		public void InsertIndex(int key) {
			if (entries[0] < order * 2) { // if index node have space
				for (int i = entries[0]; i > 0; i--) { // scan from right to left
					if (key >= entries[i]) {
						entries[i + 1] = key;
					} else {
						entries[i + 1] = entries[i];
					}
				}
				entries[0]++;
			} else { // if index node have no space
				PushUp();
			}
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

	class LeafNode {
		LeafNode next; // pointer for next leaf node array
		LeafNode previous; // pointer for previous leaf node array
		Node[] entries = new Node[order * 2 + 1]; // array to store nodes

		LeafNode() {
			Node record = new Node(0);
			entries[0] = record;
		}

		public void InsertLeaf(int key, int rid) {
			if (entries[0].key < order * 2) { // if leaf node have space
				for (int i = entries[0].key; i > 0; i--) { // scan from right to left
					if (key >= entries[i].key) {
						entries[i + 1] = new Node(key, rid);
					} else {
						entries[i + 1] = entries[i];
					}
				}
				entries[0].key++;
			} else { // if leaf node have no space
				CopyUp();
			}
		}
	}

	public void Insert(int key, int rid) {
		if (root == null) {
			if (start == null) {
				LeafNode leaf = new LeafNode();
				leaf.InsertLeaf(key, rid);
				start = leaf;
			} else {
				start.InsertLeaf(key, rid);
			}
		} else {
			// search place
			LeafNode demo = new LeafNode();
			demo.InsertLeaf(key, rid);

		}
	}

	public void PushUp() {
	};

	public void CopyUp() {
	};

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
