package me.ysobj.stone.model;

public class ASTNodeList extends ASTNode {
	ASTNode[] nodes;

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

}
