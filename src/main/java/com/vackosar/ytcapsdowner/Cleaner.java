package com.vackosar.ytcapsdowner;

public class Cleaner {

    private static final String DOT_LIKE = ",;.!?";

    public String clean(String text) {
        return text
                .replaceAll(" '([^" + DOT_LIKE + "']*)'", " $1")
                .replaceAll("[^a-z0-9A-Z\\' ]", "")
                .replaceAll("n't", " n't")
                .replaceAll("'([^t])", " '$1");
    }
}
