package com.vackosar.ytcapsdowner;

import android.content.res.AssetManager;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Punctuator {

    private static final String ASSET_PREFIX = "file:///android_asset/";
    public static final String WORD_INDEX_FILENAME = "punctuator_word_index.txt";
    public static final String MODEL_FILENAME = ASSET_PREFIX + "punctuator.pb";

    private static final String INPUT_NAME = "embedding_1_input";
    private static final String OUTPUT_NAME = "dense_1/Softmax";

    private static final int INPUT_SIZE = 30;
    public static final int OUTPUT_SIZE = 2;
    private static final int[] OUTPUTS = new int[OUTPUT_SIZE];

    private final AssetManager assetManager;
    private final TensorFlowInferenceInterface inferenceInterface;

    static {
        System.loadLibrary("tensorflow_inference");
    }

    public Punctuator(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.inferenceInterface = new TensorFlowInferenceInterface(assetManager, MODEL_FILENAME);
    }

    public boolean punctuate(int[] sample) {
        inferenceInterface.feed(INPUT_NAME, sample, 1, INPUT_SIZE);
        inferenceInterface.run(new String[]{ OUTPUT_NAME }, true);
        inferenceInterface.fetch(OUTPUT_NAME, OUTPUTS);
        Map<String, Integer> wordIndex = loadWordIndex();
        return OUTPUTS[0] > OUTPUTS[1];
    }

    private Map<String, Integer> loadWordIndex() {
        HashMap<String, Integer> wordIndex = new HashMap<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(assetManager.open(WORD_INDEX_FILENAME), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(" ");
                wordIndex.put(split[0], Integer.valueOf(split[1]));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load word index", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new IllegalArgumentException("Could not load word index", e);
                }
            }
        }
        return wordIndex;
    }

    public void close() {
        inferenceInterface.close();
    }
}
