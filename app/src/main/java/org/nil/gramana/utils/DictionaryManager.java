package org.nil.gramana.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
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
	 * Reads {@code source} line by line writing filtered content to {@code out} whenever a line matches against
	 * {@code PATTERN_DICTIONARY_WORD}.
	 * @param source raw dictionary file to vocabulary read terms from.
	 * @param out filename where to write filtered vocabulary terms.
	 * @return false if the method encounters errors, true otherwise.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static boolean createDictionary (File source, String out) throws FileNotFoundException, UnsupportedEncodingException {
		final Scanner reader = new Scanner(source, "UTF-8");
		final PrintStream writer = new PrintStream(out, "UTF-8");
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


}
