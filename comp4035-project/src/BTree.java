
public class BTree {
	int order = 2; // max number of node is order*2
	Nodes root = new Nodes(); // It should be LeafNode as root is the leaf at the first 5 keys
	Nodes start = root; // first leaf node array
	int height = 0; // height start from 0

	class Nodes {
		Integer[] entries = new Integer[order * 2 + 1]; // store searching key, first entry uses to store number of key in the
												// array
		Nodes[] indexPointer = new Nodes[entries.length]; // pointer for next level index, only use when it's
																	// not leaf node
		Nodes leafSilding; // pointer for locating leaf node, only use when it's
																// leaf node , It's only point to next leaf node
		boolean isLeafNode = true;

		Nodes(){
			this.entries[0] =0;
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


	public void Insert(int key) {
		Nodes currentNode = root;
		while(currentNode.isLeafNode==false){ // Find the nodes should store the key first, scan from root until the Nodes.isLeafNode = true
			for(int i=1; i<=currentNode.entries[0];i++){
				if(currentNode.entries[i]>key){
					currentNode = currentNode.indexPointer[i-1];
					return;
				}
			}
		}
		if(currentNode.entries[0]==4){
			//full
		}else{
			for(int i=1; i<=4;i++){
				if(currentNode.entries[i]==null){
					currentNode.entries[i] = key ; // Insert complete add entries[0] count
					currentNode.entries[0]++;
					return;
				}else{
					if(currentNode.entries[i]>key){
						int moveEntriesIndex = currentNode.entries[0]; // Move from last one 
						while(moveEntriesIndex>=i){
							currentNode.entries[moveEntriesIndex+1] = currentNode.entries[moveEntriesIndex];
							moveEntriesIndex--;
						}
						currentNode.entries[i] =key;
						currentNode.entries[0]++;
						return;
					}
				}
			}
		}
	}

	public void PushUp() {
	};

	public void CopyUp() {
	};

	public void printAllLeafNode(){
		Nodes currentNodes = start;
		while(currentNodes!=null){
			for(int i=1; i<=4;i++){
				System.out.print(currentNodes.entries[i]);
				System.out.print(", ");
			}
			currentNodes = currentNodes.leafSilding;
		}
		System.out.println("");
		System.out.println("End");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			BTree bTree = new BTree();
			bTree.Insert(8);
			bTree.Insert(3);
			bTree.Insert(5);
			bTree.Insert(1);
			bTree.printAllLeafNode();
	}

}
