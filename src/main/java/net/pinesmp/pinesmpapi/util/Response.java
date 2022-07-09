package net.pinesmp.pinesmpapi.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Response {

	private boolean success;
	private final LinkedList<String> errors;
	private final LinkedList<String> messages;
	private final LinkedHashMap<String, Object> result;

	public Response() {
		this.success = true;
		this.errors = new LinkedList<String>();
		this.messages = new LinkedList<String>();
		this.result = new LinkedHashMap<String, Object>();
	}

	public Response(boolean success, LinkedList<String> errors, LinkedList<String> messages, LinkedHashMap<String, Object> result) {
		this.success = success;
		this.errors = errors;
		this.messages = messages;
		this.result = result;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
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
