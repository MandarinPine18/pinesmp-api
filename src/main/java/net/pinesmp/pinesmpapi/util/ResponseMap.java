package net.pinesmp.pinesmpapi.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ResponseMap extends LinkedHashMap<String, Object> {
	private boolean success;
	public final LinkedList<String> errors;
	public final LinkedList<String> messages;
	public final LinkedHashMap<String, Object> result;

	public ResponseMap() {
		super();

		this.success = true;
		this.errors = new LinkedList<String>();
		this.messages = new LinkedList<String>();
		this.result = new LinkedHashMap<String, Object>();

		this.put("success", this.success);
		this.put("errors", this.errors);
		this.put("messages", this.messages);
		this.put("result", this.result);
	}

	public boolean success() {
		return this.success;
	}

	public void success(boolean success) {
		this.success = success;
	}

	/*  // WIP
	public String serialize() {
		return new Gson().toJson(this);
	}

	public ResponseMap deserialize(String serialized) {
		Type type = new TypeToken<ResponseMap>(){}.getType();
		ResponseMap responseMap = new Gson().fromJson(serialized, type);

		this.success
	}*/
}
