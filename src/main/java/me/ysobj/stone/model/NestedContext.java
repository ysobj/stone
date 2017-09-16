package me.ysobj.stone.model;

import java.util.HashMap;
import java.util.Map;

public class NestedContext extends Context {
	private Map<String, Object> env;
	private Context context;

	public NestedContext(Context context) {
		this.context = context;
		env = new HashMap<>();
	}

	@Override
	public boolean has(String name) {
		return env.containsKey(name) ? true : this.context.has(name);
	}

	@Override
	public Object get(String name) {
		if (env.containsKey(name)) {
			return env.get(name);
		}
		return this.context.get(name);
	}

	@Override
	public void put(String name, Object value) {
		if (env.containsKey(name)) {
			env.put(name, value);
		} else if (context.has(name)) {
			context.put(name, value);
		}
	}

	public void putNew(String name, Object value) {
		env.put(name, value);
	}
}
