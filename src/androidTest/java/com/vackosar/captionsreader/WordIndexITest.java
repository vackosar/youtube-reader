package com.vackosar.captionsreader;

import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;

import org.junit.Test;

public class WordIndexITest {

    @Test
    public void load() {
        WordIndex wordIndex = new WordIndex(InstrumentationRegistry.getTargetContext().getAssets());
        Assert.assertEquals(Integer.valueOf(1), wordIndex.get("the"));
        Assert.assertEquals(Integer.valueOf(2), wordIndex.get("of"));
    }
}

