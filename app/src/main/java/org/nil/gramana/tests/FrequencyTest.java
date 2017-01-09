package org.nil.gramana.tests;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import org.nil.gramana.tools.Dictionary;

public class FrequencyTest {

    @Test
    public void anagramsTest () {
        // Read words from file and put into a simulated multimap
        Map<String, List<String>> m = new HashMap<>();
        List<String> l;

        final Scanner s;
        final String fileName = "src/main/assets/dictionaries/Italian dictionary.txt";
        int minGroupSize = 6;
        String word, alpha;
        Matcher wordMatcher;

        try {
            s = new Scanner(new File(fileName));

            while (s.hasNextLine()) {
                wordMatcher = Dictionary.PATTERN_DICTIONARY_WORD.matcher(s.nextLine());

                if (wordMatcher.lookingAt()) {
                    word = wordMatcher.group(0);
                } else {
                    continue;
                }

                alpha = alphabetize(word);
                l = m.get(alpha);

                if (l == null)
                    m.put(alpha, l = new ArrayList<>());

                l.add(word);
            }
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }

        // Print all permutation groups above size threshold
        for (List<String> list : m.values())
            if (list.size() >= minGroupSize)
                System.out.println(list.size() + ": " + list);
    }

    private static String alphabetize (String s) {
        char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }

}
