package com.vackosar.ytcapsdowner;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class GraphExecutorIT {

    private GraphExecutor graphExecutor;

    @Before
    public void before() {
        graphExecutor = new GraphExecutor(InstrumentationRegistry.getTargetContext().getAssets());
    }

    @Test
    public void predictInts() throws Exception {
        int[] sample = new int[]{1981, 12531, 12, 209, 42, 360, 7212, 96, 19999, 796, 3, 10, 8841, 7481, 7228, 464, 42, 177, 19999, 362, 425, 3, 2191, 206, 3, 19, 42, 132, 17094, 60};
        Assert.assertTrue(graphExecutor.punctuate(sample));
    }
}
