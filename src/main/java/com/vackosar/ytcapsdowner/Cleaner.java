package com.vackosar.ytcapsdowner;

public class Cleaner {

    public static String clean(String text) {
        return text
                .toLowerCase()
                .replaceAll("[^a-z0-9\\' ]", "")
                .replaceAll("n't", " n`t")
                .replaceAll("([a-z])'s", "$1 `s")
                .replaceAll("([a-z])'m", "$1 `m")
                .replaceAll("([a-z])'re", "$1 `re")
                .replaceAll("([a-z])'ve", "$1 `ve")
                .replaceAll("([a-z])'d", "$1 `d")
                .replaceAll("([a-z])'ll", "$1 `ll")
                .replaceAll("([a-z])'em", "$1 `em")
                .replaceAll("'", "")
                .replaceAll("`", "'");
    }

}
