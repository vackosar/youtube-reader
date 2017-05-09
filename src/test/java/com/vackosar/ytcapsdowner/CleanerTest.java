package com.vackosar.ytcapsdowner;

import junit.framework.Assert;

import org.junit.Test;

public class CleanerTest {

    @Test
    public void clean() throws Exception {
        Assert.assertEquals("I do n't", new Cleaner().clean("I don't!"));
    }

}