package me.ysobj.stone.model;

public class ASTNodeList extends ASTNode{
	ASTNode[] nodes;
	public ASTNodeList(ASTNode... nodes){
		this.nodes = nodes;
	}
	
	public ASTNode[] getNodes() {
		return nodes;
	}
}
