package org.nil.gramana.tests;

import org.junit.Assert;
import org.junit.Test;
import org.nil.gramana.utils.DictionaryManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * Created by n0ne on 27/02/17.
 */
public class DictionaryTest {

	private static String PATH_DICT_ROOT = "src/main/assets/dictionaries/";

	@Test
	public void testCreateDictionary () throws FileNotFoundException, UnsupportedEncodingException {
		final String
				in = PATH_DICT_ROOT + "Italian dictionary.txt",
				out = PATH_DICT_ROOT + "Filtered italian dictionary.txt";

		Assert.assertTrue(
				DictionaryManager.createDictionary(
						new File(in),
						out
				)
		);
	}

	@Test(expected = DictionaryManager.FileFoundException.class)
	public void testCreateDictionaryIfNotExists () throws FileNotFoundException, UnsupportedEncodingException {
		final String
				in = PATH_DICT_ROOT + "Italian dictionary.txt",
				out = PATH_DICT_ROOT + "Filtered italian dictionary.txt";

		Assert.assertTrue(
				DictionaryManager.createDictionaryIfNotExists(
						new File(in),
						out
				)
		);
	}

}
