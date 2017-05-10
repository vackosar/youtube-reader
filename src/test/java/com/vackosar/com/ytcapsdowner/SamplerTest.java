package com.vackosar.com.ytcapsdowner;

import com.vackosar.ytcapsdowner.Sampler;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SamplerTest {

    @Test
    public void clean() throws Exception {
        String text = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31";
        List<String[]> samples = new Sampler().sample(text);
        Assert.assertEquals(
                Arrays.asList("however however however however however however however however however however however however however however however 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15".split(" ")),
                Arrays.asList(samples.get(0)));
        Assert.assertEquals(
                Arrays.asList("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30".split(" ")),
                Arrays.asList(samples.get(15)));
        Assert.assertEquals(
                Arrays.asList("6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 however however however however".split(" ")),
                Arrays.asList(samples.get(20)));
        Assert.assertEquals(
                Arrays.asList("16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 however however however however however however however however however however however however however however".split(" ")),
                Arrays.asList(samples.get(30)));
    }

}