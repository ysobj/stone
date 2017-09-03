package me.ysobj.stone.model;

import java.util.Arrays;

public class ASTNodeList extends ASTNode {
	ASTNode[] nodes;
	
	public ASTNodeList(int size) {
		this.nodes = new ASTNode[size];
	}

	public ASTNodeList(ASTNode... nodes) {
		this.nodes = nodes;
	}

	public ASTNode[] getNodes() {
		return nodes;
	}

	@Override
	public Object evaluate(Context context) {
		Object result = null;
		for (ASTNode astNode : nodes) {
			if (astNode != null) {
				result = astNode.evaluate(context);
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return Arrays.toString(nodes);
	}

	public void add(ASTNode node) {
		for (int i = 0; i < nodes.length; i++) {
			if(nodes[i] == null) {
				nodes[i] = node;
				return;
			}
		}
		ASTNode[] tmp = new ASTNode[nodes.length + 1];
		System.arraycopy(nodes, 0, tmp, 0, nodes.length);
		tmp[nodes.length] = node;
		this.nodes = tmp;
	}
	
	public int size() {
		return this.nodes.length;
	}
}
