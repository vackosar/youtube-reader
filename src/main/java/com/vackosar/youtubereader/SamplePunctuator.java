package com.vackosar.youtubereader;

public class SamplePunctuator {

    private final GraphExecutor graphExecutor;
    private final WordIndex wordIndex;

    public SamplePunctuator(WordIndex wordIndex, GraphExecutor graphExecutor) {
        this.wordIndex = wordIndex;
        this.graphExecutor = graphExecutor;
    }

    public boolean punctuate(String[] sample) {
        if (sample.length != GraphExecutor.INPUT_SIZE) {
            throw new IllegalArgumentException("Length " + sample.length + " does not match " + GraphExecutor.INPUT_SIZE);
        }
        int[] inputs = new int[GraphExecutor.INPUT_SIZE];
        for (int i = 0; i < GraphExecutor.INPUT_SIZE; i++) {
            inputs[i] = wordIndex.get(sample[i]);
        }
        return graphExecutor.punctuate(inputs);
    }
}
