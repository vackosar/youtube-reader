package com.vackosar.captionsreader;

import android.content.res.AssetManager;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class GraphExecutor {

    private static final String ASSET_PREFIX = "file:///android_asset/";
    private static final String MODEL_FILENAME = ASSET_PREFIX + "punctuator.pb";

    private static final String INPUT_NAME = "embedding_1_input";
    private static final String OUTPUT_NAME = "dense_1/Softmax";

    public static final int INPUT_SIZE = 30;
    public static final int DETECTECTION_INDEX = INPUT_SIZE / 2;
    private static final int OUTPUT_SIZE = 2;
    private static final float[] OUTPUTS = new float[OUTPUT_SIZE];

    private final TensorFlowInferenceInterface inferenceInterface;

    static {
        System.loadLibrary("tensorflow_inference");
    }

    public GraphExecutor(AssetManager assetManager) {
        this.inferenceInterface = new TensorFlowInferenceInterface(assetManager, MODEL_FILENAME);
    }

    public boolean punctuate(int[] sample) {
        inferenceInterface.feed(INPUT_NAME, sample, 1, INPUT_SIZE);
        inferenceInterface.run(new String[]{ OUTPUT_NAME }, true);
        inferenceInterface.fetch(OUTPUT_NAME, OUTPUTS);
        return OUTPUTS[0] < OUTPUTS[1];
    }

    public void close() {
        inferenceInterface.close();
    }
}
