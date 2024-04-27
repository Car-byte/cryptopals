package org.carbyte.common;

import com.google.common.base.CharMatcher;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyValueParser {

    public static Map<String, String> parse(final String keyValuePairsSerialized) {
        return Arrays.stream(keyValuePairsSerialized.split(";"))
                .map(keyValuePair -> keyValuePair.split("="))
                .filter(keyValuePair -> keyValuePair.length == 2)
                .collect(Collectors.toMap(keyValuePair -> keyValuePair[0], keyValuePair -> keyValuePair[1]));
    }

    public static String sanatizeString(final String str) {
        return CharMatcher.anyOf(";=").removeFrom(str);
    }
}
