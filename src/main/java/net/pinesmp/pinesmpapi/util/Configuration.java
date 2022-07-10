package net.pinesmp.pinesmpapi.util;

import net.pinesmp.pinesmpapi.exceptions.ConfigurationException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class Configuration extends HashMap <String, String> {
	private final File file;
	private final File folder;

	private static final HashMap<String, String> defaults;
	private static final List<String> keys;
	static {
		defaults = new HashMap<>();
		keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		// default key-value definitions to be mapped in defaults
		keys.add("PORT");                       values.add("8080");
		keys.add("SSL_ENCRYPTION");             values.add("false");
		keys.add("SSL_client-auth");            values.add("want");
		keys.add("SSL_key-alias");              values.add("null");
		keys.add("SSL_key-store");              values.add("null");
		keys.add("SSL_key-store-password");     values.add("null");
		keys.add("SSL_trust-store");            values.add("null");
		keys.add("SSL_trust-store-password");   values.add("null");

		assert keys.size() == values.size();
		for(int i = 0; i < keys.size(); i++) {
			defaults.put(keys.get(i), values.get(i));
		}
	}

	public static String getDefault(String key) {
		return defaults.get(key);
	}

	public Configuration (File file, File folder) {
		super();

		// sets config to defaults
		this.putAll(defaults);

		// initialize file if it's not there
		this.file = file;
		try {
			if (!file.exists()) {
				if (!file.createNewFile()) {
					throw new IOException();
				}
			}
			assert file.isFile();
		} catch (IOException e) {
			throw new ConfigurationException("Configuration file initialization failed, for some reason.");
		}

		// initialize folder if it's not there
		this.folder = folder;
		try {
			if (!file.exists()) {
				if (!file.mkdirs()) {
					throw new IOException();
				}
			}
			assert file.isDirectory();
		} catch (IOException e) {
			throw new ConfigurationException("Configuration folder initialization failed, for some reason.");
		}

		this.fileImport();
	}

	@Override
	public String get(Object key) {
		// if it's not in the defaults, the key is unrecognized
		if (!defaults.containsKey((String) key)) {
			throw new ConfigurationException("Key " + key + " not in configuration.");
		}

		return super.getOrDefault(key, getDefault((String) key));
	}

	public File getFile(Object key) {
		File referredFile = new File(folder, get(key));

		if (!referredFile.exists()) {
			try {
				if (!referredFile.createNewFile()) {
					throw new IOException();
				}
			} catch (IOException e) {
				throw new ConfigurationException("getFile() failed to create new file");
			}
		}

		return referredFile;
	}

	public void fileImport() {
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(file));
		} catch (IOException e) {
			throw new ConfigurationException("File import failed unexpectedly.");
		}

		ArrayList<String> lines = new ArrayList<String>(in.lines().toList());

		for (String key: keys) {
			String line;
			try {
				line = lines.stream().filter((x) -> (x.startsWith(key + "="))).findFirst().get();    // get a line matching the key
			} catch (NoSuchElementException ignored) {
				continue;       // if a key isn't there, it'll just default - no need to panic
			}

			line = line.substring((key + "=").length());    // stripping off the assignment part of the string

			this.put(key, line);
		}
	}

	public void fileExport() {
		FileWriter out;
		try {
			out = new FileWriter(file, false);  // overwrite mode is on
		} catch (IOException e) {
			throw new ConfigurationException("File export failed unexpectedly.");
		}

		ArrayList<String> lines = new ArrayList<String>();
		for (String key: keys) {
			lines.add(key + "=" + this.get(key));
		}

		for (String line: lines) {
			try {
				out.write(line + "\n");
			} catch (IOException e) {
				throw new ConfigurationException("File writing in export failed unexpectedly.");
			}
		}

		try {
			out.flush();
		} catch (IOException ignored) {
			throw new ConfigurationException("File writing in export failed unexpectedly.");
		}
	}
}
