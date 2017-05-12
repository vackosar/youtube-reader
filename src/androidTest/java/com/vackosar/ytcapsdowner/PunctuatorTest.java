package com.vackosar.ytcapsdowner;

import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class PunctuatorTest {


    private static final String TEXT_1 = "Binding upon the member states as to the result to be achieved. But leave to the national authorities.";
    private static final String TEXT_2 = "I think that's a good thing like it doesn't have to change the world like you know. If you make something that has high value to people and frankly even if it's something ";
    private static final String TEXT_3 = "We have elon musk eon thank you for joining us thanks having right so we we want to spend the time today talking about your view of the future and people should work on so to start off could you tell us you famously said. When you were younger there were five problems that you thought were most important for you to work on um.";
    private Punctuator punctuator;

    @Before
    public void before() {
        AssetManager assets = InstrumentationRegistry.getTargetContext().getAssets();
        punctuator = new Punctuator(new Sampler(), new SamplePunctuator(new WordIndex(assets), new GraphExecutor(assets)));
    }

    @Test
    public void punctuate1() {
        Assert.assertEquals(TEXT_1, punctuator.punctuate(TEXT_1));
    }

    @Test
    public void punctuate2() {
        Assert.assertEquals(TEXT_2, punctuator.punctuate(TEXT_2));
    }

    @Test
    public void punctuate3() {
        String punctuated = punctuator.punctuate(TEXT_3);
        System.out.println(punctuated);
        Assert.assertEquals(TEXT_3, punctuated);
    }


}
