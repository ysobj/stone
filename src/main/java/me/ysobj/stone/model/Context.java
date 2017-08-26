package me.ysobj.stone.model;

import java.util.HashMap;
import java.util.Map;

public class Context {
	Map<String, Object> env;
	public Context() {
		env = new HashMap<>();
	}

	public boolean has(String name) {
		return env.containsKey(name);
	}

	public Object get(String name) {
		return env.get(name);
	}

	public void put(String name, Object value) {
		env.put(name, value);
	}

}
