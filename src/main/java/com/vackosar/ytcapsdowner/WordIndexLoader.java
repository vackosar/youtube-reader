package com.vackosar.ytcapsdowner;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class WordIndexLoader {

    private static final String WORD_INDEX_FILENAME = "punctuator_word_index.txt";

    private final AssetManager assetManager;

    public WordIndexLoader(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Map<String, Integer> load() {
        HashMap<String, Integer> wordIndex = new HashMap<>();
        BufferedReader reader = null;
        try {
            reader = createReader();
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
    private BufferedReader createReader() throws IOException {
        return new BufferedReader(new InputStreamReader(assetManager.open(WORD_INDEX_FILENAME), "UTF-8"));
    }

    private void parseLine(HashMap<String, Integer> wordIndex, String line) {
        String[] split = line.split(" ");
        wordIndex.put(split[0], Integer.valueOf(split[1]));
    }

}
