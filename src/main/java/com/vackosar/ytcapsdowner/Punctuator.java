package com.vackosar.ytcapsdowner;

import android.content.res.AssetManager;
import android.util.Log;

import org.tensorflow.Operation;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class Punctuator {

    int[] sample = {1981, 12531, 12, 209, 42, 360, 7212, 96, 19999, 796, 3, 10, 8841, 7481, 7228, 464, 42, 177, 19999, 362, 425, 3, 2191, 206, 3, 19, 42, 132, 17094, 60};

    static {
        System.loadLibrary("tensorflow_inference");
    }

    private static final String TAG = "Punctuator";

    protected void punctuate(AssetManager assetManager) {

        int inputSize = 30;
        String inputName = "embedding_1_input";
        String outputName = "dense_1/Softmax";
        String modelFilename = "file:///android_asset/punctuator.pb";

        TensorFlowInferenceInterface inferenceInterface = new TensorFlowInferenceInterface(assetManager, modelFilename);

        final Operation operation = inferenceInterface.graphOperation(outputName);
        final int numClasses = (int) operation.output(0).shape().size(1);
        Log.i(TAG, "Output layer size is " + numClasses);

        String[] outputNames = new String[] {outputName};
        int[] intValues = sample;
        float[] outputs = new float[numClasses];


        inferenceInterface.feed(inputName, intValues, 1, inputSize);
        inferenceInterface.run(outputNames, true);
        inferenceInterface.fetch(outputName, outputs);

        for (float output: outputs) {
            Log.i(TAG, "output:" + output);
        }

        inferenceInterface.close();

    }


}
