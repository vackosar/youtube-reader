package com.vackosar.ytcapsdowner;

import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Map;

public class WordIndexLoaderIT {

    @Test
    public void load() {
        WordIndexLoader loader = new WordIndexLoader(InstrumentationRegistry.getTargetContext().getAssets());
        Map<String, Integer> index = loader.load();
        Assert.assertEquals(Integer.valueOf(1), index.get("the"));
        Assert.assertEquals(Integer.valueOf(2), index.get("of"));
    }
}

