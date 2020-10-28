package ar.edu.itba.pod.g3.api.query1;

import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.pod.g3.api.models.Tuple;
import com.hazelcast.mapreduce.Collator;

public class Query1Collator implements Collator<Map.Entry<String, Integer>, Map<String, Double>> {

    private final Map<String, Integer> neighbourhoodData = new HashMap<>();

    public Query1Collator(Map<String, Integer> neighbourhoodData) {
        neighbourhoodData.forEach(this.neighbourhoodData::put);
    }

    @Override
    public Map<String, Double> collate(Iterable<Map.Entry<String, Integer>> values) {
        Map<String, Double> map = new HashMap<>();
        for (Map.Entry<String, Integer> entry : values) {
            map.put(entry.getKey(), (double)entry.getValue()/neighbourhoodData.get(entry.getKey()));
        }

        return map;
    }
}
