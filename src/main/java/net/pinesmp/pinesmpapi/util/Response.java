package net.pinesmp.pinesmpapi.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Response {
	private final boolean success;
	private final LinkedList<String> errors;
	private final LinkedList<String> messages;
	private final LinkedHashMap<String, Object> result;

	public Response(boolean success, LinkedList<String> errors, LinkedList<String> messages, LinkedHashMap<String, Object> result) {
		this.success = success;
		this.errors = errors;
		this.messages = messages;
		this.result = result;
	}

	public boolean isSuccess() {
		return success;
	}

	public LinkedList<String> getErrors() {
		return errors;
	}

	public LinkedList<String> getMessages() {
		return messages;
	}

	public LinkedHashMap<String, Object> getResult() {
		return result;
	}
}
