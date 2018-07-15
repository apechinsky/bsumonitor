package com.anton.bgu.parser;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.srplib.contract.Assert;

import com.anton.bgu.model.Range;

/**
 * @author Q-APE
 */
public class RequestsDistribution {

    private Map<Range, Integer> requests = new TreeMap<>(Comparator.reverseOrder());

    public void set(Range range, int value) {
        Assert.checkFalse(requests.containsKey(range), "Can't set %s to %d. Range already has value %d.",
            range, value, requests.get(range));

        requests.put(range, value);
    }



}
