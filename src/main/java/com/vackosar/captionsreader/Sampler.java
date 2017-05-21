package com.vackosar.captionsreader;

import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Sampler {

    static final String SPACE = " ";
    public static final String FILLER = "however";

    public List<String[]> sample(String text) {
        List<String[]> samples = new ArrayList<>();
        List<String> firstList = new ArrayList<>();
        for (int i = 0; i < GraphExecutor.DETECTECTION_INDEX + 1; i++) {
            firstList.add(FILLER);
        }
        Deque<String> deque = new ArrayDeque<>(firstList);
        for(String word: Cleaner.clean(text).split(SPACE)) {
            if (word.isEmpty()) {
                continue;
            }
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
