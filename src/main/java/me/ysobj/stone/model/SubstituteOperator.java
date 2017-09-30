package me.ysobj.stone.model;

import me.ysobj.stone.exception.VariableAlreadyDefinedException;
import me.ysobj.stone.exception.VariableNotFoundException;

public class SubstituteOperator implements Operator {
	private boolean inVarContext;

	public SubstituteOperator(boolean inVarContext) {
		this.inVarContext = inVarContext;
	}

	@Override
	public Object evaluate(Context context, ASTNode... astnode) {
		Identifier identifier = extract(astnode[0]);
		if (inVarContext) {
			if (context.has(identifier.getName())) {
				throw new VariableAlreadyDefinedException(identifier.getName());
			}
			if (context instanceof NestedContext) {
				((NestedContext) context).putNew(identifier.getName(), astnode[1].evaluate(context));
			} else {
				context.put(identifier.getName(), astnode[1].evaluate(context));
			}
		} else {
			if (!context.has(identifier.getName())) {
				throw new VariableNotFoundException(identifier.getName());
			}
			context.put(identifier.getName(), astnode[1].evaluate(context));
		}
		return Void.VOID;

	}

	protected Identifier extract(ASTNode node) {
		if (node instanceof Identifier) {
			return (Identifier) node;
		} else if (node instanceof ASTNodeList) {
			return extract(((ASTNodeList) node).getNodes()[0]);
		}
		throw new RuntimeException();
	}

	@Override
	public String toString() {
		return "=";
	}

	@Override
	public int order() {
		return 0;
	}

}
