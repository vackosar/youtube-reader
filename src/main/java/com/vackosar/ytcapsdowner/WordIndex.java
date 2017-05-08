package com.vackosar.ytcapsdowner;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class WordIndex {

    private static final String WORD_INDEX_FILENAME = "punctuator_word_index.txt";
    private final Map<String, Integer> index;

    public WordIndex(AssetManager assetManager) {
        index = load(assetManager);
    }

    private Map<String, Integer> load(AssetManager assetManager) {
        HashMap<String, Integer> wordIndex = new HashMap<>();
        BufferedReader reader = null;
        try {
            reader = createReader(assetManager);
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(wordIndex, line);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load word index", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {}
            }
        }
        return wordIndex;
    }

    @NonNull
    private BufferedReader createReader(AssetManager assetManager) throws IOException {
        return new BufferedReader(new InputStreamReader(assetManager.open(WORD_INDEX_FILENAME), "UTF-8"));
    }

    private void parseLine(HashMap<String, Integer> wordIndex, String line) {
        String[] split = line.split(" ");
        wordIndex.put(split[0], Integer.valueOf(split[1]));
    }

    public Integer get(String word) {
        Integer i = index.get(word);
        if (i == null) {
            return unknownWordIndex();
        } else {
            return i;
        }
    }

    private Integer unknownWordIndex() {
        return index.size();
    }

}
