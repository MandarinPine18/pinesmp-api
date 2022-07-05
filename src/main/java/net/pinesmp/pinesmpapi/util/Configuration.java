package net.pinesmp.pinesmpapi.util;

import net.pinesmp.pinesmpapi.exceptions.ConfigurationException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class Configuration extends HashMap <String, String> {
	private final File file;
	private final File folder;

	private static final HashMap<String, String> defaults;
	static {
		defaults = new HashMap<String, String>();

		defaults.put("PORT", "7070");

		defaults.put("SSL_ENCRYPTION", "false");
		defaults.put("SSL_keystoreFile", "null");
		defaults.put("SSL_keystorePassword", "null");
		defaults.put("SSL_truststoreFile", "null");
		defaults.put("SSL_truststorePassword", "null");
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

		for (String key: this.keySet()) {
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
		for (String key: this.keySet()) {
			lines.add(key + "=" + this.get(key));
		}
		lines.sort(String::compareToIgnoreCase);    // alphabetize real quick

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
