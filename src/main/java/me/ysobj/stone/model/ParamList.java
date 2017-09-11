package me.ysobj.stone.model;

import java.util.Arrays;

public class ParamList extends ASTNode {
	Identifier[] nodes;
	
	public ParamList(int size) {
		this.nodes = new Identifier[size];
	}

	public ParamList(Identifier... nodes) {
		this.nodes = nodes;
	}

	public Identifier[] getNodes() {
		return nodes;
	}

	@Override
	public String toString() {
		return Arrays.toString(nodes);
	}

	public void add(Identifier node) {
		for (int i = 0; i < nodes.length; i++) {
			if(nodes[i] == null) {
				nodes[i] = node;
				return;
			}
		}
		Identifier[] tmp = new Identifier[nodes.length + 1];
		System.arraycopy(nodes, 0, tmp, 0, nodes.length);
		tmp[nodes.length] = node;
		this.nodes = tmp;
	}
	
	public int size() {
		return this.nodes.length;
	}
}
