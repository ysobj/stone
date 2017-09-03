package me.ysobj.stone.model;

public interface Operator {
	Object evaluate(Context context, ASTNode... astnode);
	int order();
}
