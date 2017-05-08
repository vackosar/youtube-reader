package com.vackosar.ytcapsdowner;

import android.content.res.AssetManager;

import org.tensorflow.Operation;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Punctuator {

    private static final String ASSET_PREFIX = "file:///android_asset/";
    private static final String INPUT_NAME = "embedding_1_input";
    private static final String OUTPUT_NAME = "dense_1/Softmax";
    private static final int INPUT_SIZE = 30;
    private static final String WORD_INDEX_FILENAME = "punctuator_word_index.txt";
    private final AssetManager assetManager;
    private final TensorFlowInferenceInterface inferenceInterface;
    private final float[] outputs;
    private final int[] inputs;
    
    int[] SAMPLE = {1981, 12531, 12, 209, 42, 360, 7212, 96, 19999, 796, 3, 10, 8841, 7481, 7228, 464, 42, 177, 19999, 362, 425, 3, 2191, 206, 3, 19, 42, 132, 17094, 60};

    static {
        System.loadLibrary("tensorflow_inference");
    }

    private static final String GRAPH_FILENAME = "punctuator.pb";
    private static final String MODEL_FILENAME = ASSET_PREFIX + GRAPH_FILENAME;

    public Punctuator(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.inferenceInterface = new TensorFlowInferenceInterface(assetManager, MODEL_FILENAME);
        final Operation operation = inferenceInterface.graphOperation(OUTPUT_NAME);
        final int numClasses = (int) operation.output(0).shape().size(1);
        this.outputs = new float[numClasses];
        this.inputs = new int[INPUT_SIZE];
    }

    public boolean punctuate(int[] sample) {
        inferenceInterface.feed(INPUT_NAME, sample, 1, INPUT_SIZE);
        inferenceInterface.run(new String[]{ OUTPUT_NAME }, true);
        inferenceInterface.fetch(OUTPUT_NAME, outputs);
//        Map<String, Integer> wordIndex = loadWordIndex();
        return outputs[0] > outputs[1];
    }

    private Map<String, Integer> loadWordIndex() {
        HashMap<String, Integer> wordIndex = new HashMap<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(assetManager.open(ASSET_PREFIX + WORD_INDEX_FILENAME), "UTF-8"));
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
