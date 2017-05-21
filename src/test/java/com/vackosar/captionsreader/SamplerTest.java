package com.vackosar.captionsreader;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.vackosar.captionsreader.Sampler.FILLER;

public class SamplerTest {

    @Test
    public void clean() throws Exception {
        String text = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31";
        List<String[]> samples = new Sampler().sample(text);
        Assert.assertEquals(
                join(filler(15), range(1, 15)),
                Arrays.asList(samples.get(0)));
        Assert.assertEquals(
                range(1, 30),
                Arrays.asList(samples.get(15)));
        Assert.assertEquals(
                join(range(6, 31), filler(4)),
                Arrays.asList(samples.get(20)));
        Assert.assertEquals(
                join(range(16, 31), filler(14)),
                Arrays.asList(samples.get(30)));
    }
    
    private List<String> range(int from, int to) {
        List<String> values = new ArrayList<>(to - from + 1);
        for (int i = 0; i < to - from + 1; i++) {
            values.add(String.valueOf(from + i));
        }
        return values;
    }

    private List<String> join(List<String> list1, List<String> list2) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(list1);
        list.addAll(list2);
        return list;
    }

    private List<String> filler(int num) {
        List<String> values = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            values.add(FILLER);
        }
        return values;
    }

}