package me.ysobj.stone.model;

public class StoneObject {
	private ClassInfoNode clazz;
	private NestedContext context;

	public StoneObject(ClassInfoNode clazz, NestedContext context) {
		super();
		this.clazz = clazz;
		this.context = context;
		this.clazz.getClassBody().evaluate(context);
	}

	public Object get(String name) {
		return context.get(name);
	}

}
