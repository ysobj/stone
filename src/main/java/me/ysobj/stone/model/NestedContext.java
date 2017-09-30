package me.ysobj.stone.model;

import java.util.HashMap;

public class NestedContext extends Context {
	protected Context context;

	public NestedContext() {
		env = new HashMap<>();
	}

	public NestedContext(Context context) {
		this.context = context;
		env = new HashMap<>();
	}

	@Override
	public boolean has(String name) {
		if (env.containsKey(name)) {
			return true;
		}
		if (this.context == null) {
			return false;
		}
		return this.context.has(name);
	}

	@Override
	public Object get(String name) {
		if (env.containsKey(name)) {
			return env.get(name);
		}
		if (this.context == null) {
			return null;
		}
		return this.context.get(name);
	}

	@Override
	public void put(String name, Object value) {
		if (env.containsKey(name)) {
			env.put(name, value);
		} else if (this.context == null) {
			return;
		} else if (context.has(name)) {
			context.put(name, value);
		}
	}

	public void putNew(String name, Object value) {
		env.put(name, value);
	}

	@Override
	public String toString() {
		return "NestedContext [env=" + env + ", context=" + context + "]";
	}

	public void setOuter(Context context) {
		if (this != context) {
			this.context = context;
		}
	}
}
