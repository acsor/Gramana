package org.nil.gramana.utils;

import java.io.*;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to provide CRUD operations on dictionary files.
 */
public class DictionaryManager {

	//TO-DO Improve this regex. Digits are allowed in the following pattern.
	public static final Pattern PATTERN_DICTIONARY_WORD =
			Pattern.compile("^\\P{Punct}+", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

	/**
	 * Invokes createDictionary(File, OutputStream) if there is no such file at {@code out}.
	 * @param source
	 * @param out
	 * @return result value of createDictionary(File, OutputStream).
	 * @throws FileFoundException, if a file located at {@code out} already exists.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static boolean createDictionaryIfNotExists (File source, String out)
			throws FileFoundException, FileNotFoundException, UnsupportedEncodingException {
		final File outFile = new File(out);

		if (outFile.exists() && outFile.isFile()) {
			throw new FileFoundException(
					String.format("\"%s\" already exists", out)
			);
		} else {
			return createDictionary(source, out);
		}
	}

	public static boolean createDictionary (File source, String out) throws FileNotFoundException, UnsupportedEncodingException {
		return createDictionary(source, new FileOutputStream(out));
	}

	public static boolean createDictionary (File source, OutputStream out) throws FileNotFoundException, UnsupportedEncodingException {
		return createDictionary(new FileInputStream(source), out);
	}

	/**
	 * Reads {@code source} line by line writing filtered content to {@code out} whenever a line matches against
	 * {@code PATTERN_DICTIONARY_WORD}.
	 * @param source raw dictionary InputStream to read vocabulary terms from.
	 * @param out OutputStream where to write filtered vocabulary terms.
	 * @return false if the method encounters errors, true otherwise.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static boolean createDictionary (InputStream source, OutputStream out) throws FileNotFoundException, UnsupportedEncodingException {
		final Scanner reader = new Scanner(source, "UTF-8");
		final PrintStream writer = new PrintStream(out, false, "UTF-8");
		Matcher wordMatcher;

		while (reader.hasNextLine()) {
			wordMatcher = PATTERN_DICTIONARY_WORD.matcher(reader.nextLine());

			if (wordMatcher.lookingAt()) {
				writer.println(wordMatcher.group());

				if (writer.checkError()) {
					reader.close();
					writer.close();

					return false;
				}
			}
		}

		reader.close();
		writer.close();

		return true;
	}

	public static SortedSet<String> readDictionary (final File source) throws FileNotFoundException {
		final Scanner reader = new Scanner(source, "UTF-8");
		final SortedSet<String> words = new TreeSet<>();

		while (reader.hasNextLine()) {
			words.add(reader.nextLine());
		}

		reader.close();

		return words;
	}

	/**
	 * Deletes dictionary file referred to by {@code dictionary}.
	 * @param dictionary dictionary file to be deleted. Must not be null and have isFile()
	 *                   return true.
	 */
	public static void deleteDictionary (final File dictionary) {
		if (dictionary != null && dictionary.isFile()) {
			dictionary.delete();
		}
	}

	/**
	 * Thrown to notify a user that an operation is being attempted on an already existing file.
	 */
	public static class FileFoundException extends RuntimeException {

		public FileFoundException () {
			super();
		}

		public FileFoundException (String message) {
			super(message);
		}

	}

}
