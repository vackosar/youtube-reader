package com.vackosar.captionsreader;

import java.util.List;

public class Punctuator {

    private static final String PUNCTION = ".";
    private static final String EMPTY = "";
    private static final String APOSTROPHE = "'";
    private static final String N_T = "n't";
    private final Sampler sampler;
    private final SamplePunctuator samplePunctuator;

    Punctuator(Sampler sampler, SamplePunctuator samplePunctuator) {
        this.sampler = sampler;
        this.samplePunctuator = samplePunctuator;
    }

    public String punctuate(String text) {
        List<String[]> samples = sampler.sample(text);
        StringBuilder builder = new StringBuilder();
        boolean capitalize = true;
        for (String[] sample: samples) {
            String word = readCurrentWord(sample);
            appendSpace(builder, word);
            appendCapitalized(builder, capitalize, word);
            capitalize = appendPunctuated(builder, sample);
        }
        return builder.toString();
    }

    private String readCurrentWord(String[] sample) {
        return sample[GraphExecutor.DETECTECTION_INDEX];
    }

    private void appendSpace(StringBuilder output, String word) {
        if (output.length() > 0) {
            output.append(space(word));
        }
    }

    private boolean appendPunctuated(StringBuilder output, String[] sample) {
        boolean punctuate = samplePunctuator.punctuate(sample);
        if (punctuate) {
            output.append(PUNCTION);
        }
        return punctuate;
    }

    private void appendCapitalized(StringBuilder output, boolean capitalize, String word) {
        if (capitalize) {
            output.append(capitalize(word));
        } else {
            output.append(word);
        }
    }

    private String capitalize(String word) {
        return word.substring(0,1).toUpperCase() + word.substring(1);
    }

    private String space(String word) {
        if (word.startsWith(APOSTROPHE) || N_T.equals(word)) {
            return EMPTY;
        } else {
            return Sampler.SPACE;
        }
    }

}
