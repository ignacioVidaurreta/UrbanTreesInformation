package ar.edu.itba.pod.g3.api.query4;

import ar.edu.itba.pod.g3.api.models.Tuple;
import com.hazelcast.mapreduce.Collator;

import java.util.HashMap;
import java.util.Map;

public class Query4Collator implements Collator<Map.Entry<String, Integer>, Map<String, Integer>> {

    private int min;

    public Query4Collator(int min) {
        this.min = min;
    }

    @Override
    public Map<String, Integer> collate(Iterable<Map.Entry<String, Integer>> iterable) {
        Map<String, Integer> map = new HashMap<>();
        for (Map.Entry< String, Integer> entry : iterable){
            if(entry.getValue() > this.min){
                if (!map.containsKey(entry.getKey())) {
                    map.put( entry.getKey(), entry.getValue());
                } else if (map.get(entry.getKey()) < entry.getValue()) {
                    map.put( entry.getKey(), entry.getValue());
                }
            }
        }
        return map;
    }
}