package com.vackosar.ytcapsdowner;

import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Sampler {

    private static final String DOT_LIKE = ",;.!?";
    public static final String SPACE = " ";
    public static final String FILLER = "however";

    public Sampler() {

    }

    private String clean(String text) {
        return text
                .toLowerCase()
                .replaceAll(" '([^" + DOT_LIKE + "']*)'", " $1")
                .replaceAll("[^a-z0-9\\' ]", "")
                .replaceAll("n't", " n't")
                .replaceAll("'([^t])", " '$1");
    }

    public List<String[]> sample(String text) {
        List<String[]> samples = new ArrayList<>();
        List<String> firstList = new ArrayList<>();
        for (int i = 0; i < GraphExecutor.DETECTECTION_INDEX + 1; i++) {
            firstList.add(FILLER);
        }
        Deque<String> deque = new ArrayDeque<>(firstList);
        for(String word: clean(text).split(SPACE)) {
            deque.add(word);
            if (deque.size() == GraphExecutor.INPUT_SIZE + 1) {
                deque.pollFirst();
                samples.add(createSample(deque));
            }
        }
        for (int i = 0; i < GraphExecutor.DETECTECTION_INDEX - 1; i++) {
            deque.add(FILLER);
            deque.pollFirst();
            samples.add(createSample(deque));
        }
        return samples;
    }

    @NonNull
    private String[] createSample(Deque<String> deque) {
        return new ArrayList<>(deque).toArray(createSampleArray());
    }

    @NonNull
    private String[] createSampleArray() {
        return new String[GraphExecutor.INPUT_SIZE];
    }
}
