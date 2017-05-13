package com.vackosar.com.ytcapsdowner;

import com.vackosar.ytcapsdowner.Cleaner;

import junit.framework.Assert;

import org.junit.Test;

public class CleanerTest {

    @Test
    public void clean() {
        Assert.assertEquals("he does n't", Cleaner.clean("He doesn't."));
        Assert.assertEquals("he does n't", Cleaner.clean("'He doesn't.'"));
        Assert.assertEquals("he 's", Cleaner.clean("He's"));
    }

}
