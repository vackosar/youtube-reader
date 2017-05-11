package com.vackosar.ytcapsdowner;

import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;

import org.junit.Test;

public class PunctuatorTest {


    private static final String TEXT = "Binding upon the member states as to the result to be achieved. But leave to the national authorities.";

    @Test
    public void skipDotted() {
        AssetManager assets = InstrumentationRegistry.getTargetContext().getAssets();
        Punctuator punctuator = new Punctuator(new Sampler(), new SamplePunctuator(new WordIndex(assets), new GraphExecutor(assets)));
        Assert.assertEquals(TEXT, punctuator.punctuate(TEXT));
    }

}
