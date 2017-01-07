package org.nil.gramana.tools;

import java.io.*;
import java.util.Locale;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by n0ne on 14/12/16.
 */
public class Dictionary implements Closeable {

    private Scanner dictionaryReader;

    //TO-DO Improve this regex. Digits are allowed in the following pattern.
    private static final Pattern dictionaryWordPattern =
            Pattern.compile("^\\P{Punct}+", Pattern.UNICODE_CASE);

    private Dictionary () {
    }

    public Dictionary (File file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }

    public Dictionary (InputStream dictionary) throws FileNotFoundException {
        this();
        dictionaryReader = new Scanner(dictionary, "UTF-8");
    }

    public SortedSet<String> getAllWords () {
        final SortedSet<String> words = new TreeSet<>();
        Matcher wordMatcher;

        while (dictionaryReader.hasNextLine()) {
            wordMatcher = dictionaryWordPattern.matcher(dictionaryReader.nextLine());

            if (wordMatcher.lookingAt()) {
                words.add(wordMatcher.group().toLowerCase(Locale.getDefault()));
            }
        }

        return words;
    }

    @Override
    public void close () throws IOException {
        if (dictionaryReader != null) {
            dictionaryReader.close();
        }
    }

}
