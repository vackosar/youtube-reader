package com.vackosar.ytcapsdowner;

import java.util.List;

public class Punctuator {

    private static final String PUNCTION = ".";
    public static final String DOT_LIKE = "[.!?,]";
    private final Sampler sampler;
    private final SamplePunctuator samplePunctuator;

    public Punctuator(Sampler sampler, SamplePunctuator samplePunctuator) {
        this.sampler = sampler;
        this.samplePunctuator = samplePunctuator;
    }

    public String punctuate(String text) {
        List<String[]> samples = sampler.sample(text);
        String output = "";
        boolean capitalize = true;
        for (String[] sample: samples) {
            boolean punctuate = samplePunctuator.punctuate(sample);
            String word = sample[GraphExecutor.DETECTECTION_INDEX];
            if (capitalize) {
                output = output + capitalize(word);
            } else {
                output = output + word;
            }
            if (punctuate) {
                output = output + PUNCTION;
            }
            output = output + Sampler.SPACE;
        }
        return output;
    }

    private String capitalize(String word) {
        return word.substring(0,1).toUpperCase() + word.substring(1);
    }
}
