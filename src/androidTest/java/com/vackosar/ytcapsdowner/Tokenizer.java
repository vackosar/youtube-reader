package com.vackosar.ytcapsdowner;

import java.util.List;

public class Tokenizer {

    private static final String DOT_LIKE = ",;.!?";

    public List<String> tokenize(String text) {
        String clean = text
                .replaceAll("([^" + DOT_LIKE + "']*)", " \1")
                .replaceAll("[^a-z0-9A-Z\\' ]", "")
                .replaceAll("'", " '")
                .replaceAll("'([^t])", " '\1");


    }
}
