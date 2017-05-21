package com.vackosar.captionsreader;

import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;

import org.junit.Test;

public class SamplePunctuatorITest {
    
    private String[] SAMPLE = new String[] { "you", "know", "hey", "welcome", "back", "to", "test", "great", "to", "have", "you", "here", "alright", "so", "in", "the", "next", "half", "hour", "or", "so", "we", "'re", "gonna", "spend", "some", "time", "exploring", "your", "for" };

    @Test
    public void sample() {
        GraphExecutor graphExecutor = new GraphExecutor(InstrumentationRegistry.getTargetContext().getAssets());
        WordIndex wordIndex = new WordIndex(InstrumentationRegistry.getTargetContext().getAssets());
        SamplePunctuator samplePunctuator = new SamplePunctuator(wordIndex, graphExecutor);
        Assert.assertFalse(samplePunctuator.punctuate(SAMPLE));
    }
}
