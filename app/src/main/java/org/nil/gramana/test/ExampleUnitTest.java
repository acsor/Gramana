import org.nil.gramana.activity.PermutationsActivity;
import org.nil.gramana.model.InputValidator;
import org.nil.gramana.model.Scrambler;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void testPermutations () {
        final String word = "Tri co lo re";
        final Set<String> permutations = Scrambler.permute(word, "\\s", "");

        System.out.format("Extracted %d permutation(s)\n", permutations.size());

        for (String s: permutations) {
            System.out.println(s);
        }
    }

    @Test
    public void testDictionaryFeature () {
        final String permutationWord = "r e m a r e";
        final String inSep = "\\s";
        final Set<String> permutations = Scrambler.permute(permutationWord, inSep, "");

        final String dictionaryFileName = "src/main/assets/Italian dictionary.txt";
        final Set<String> dictionary = new TreeSet<>();
        Scanner dictionaryReader = null;

        final Pattern wordPattern = Pattern.compile(
                String.format("^[%s]+", permutationWord.replaceAll(inSep, "")),
                Pattern.UNICODE_CASE);
        Matcher wordMatcher;

        try {
            dictionaryReader = new Scanner(new File(dictionaryFileName), "UTF-8");

            while (dictionaryReader.hasNextLine()) {
                wordMatcher = wordPattern.matcher(dictionaryReader.nextLine());

                if (wordMatcher.lookingAt()) {
                    dictionary.add(wordMatcher.group().toLowerCase(Locale.getDefault()));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.format("\"%s\" not found\n", dictionaryFileName);
            return;
        } finally {
            if (dictionaryReader != null) {
                dictionaryReader.close();
            }
        }

        permutations.retainAll(dictionary);

        System.out.format("Found %d matches in \"%s\"\n", permutations.size(),
                dictionaryFileName);

        if (!permutations.isEmpty()) {
            System.out.println(permutations);
        }
    }

    @Test
    public void testPermutationsActivityInputValidator () {
        final String syllables = "a-b-c-d-e-f-g-h-j-k-l-m";
        final InputValidator<String> v = new PermutationsActivity.InputStringValidator(null, "-");

        System.out.println(v.isInputValid(syllables));
    }

}