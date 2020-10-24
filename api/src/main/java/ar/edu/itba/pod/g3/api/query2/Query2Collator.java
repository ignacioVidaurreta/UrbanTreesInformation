package ar.edu.itba.pod.g3.api.query2;

import ar.edu.itba.pod.g3.api.models.Tuple;
import com.hazelcast.mapreduce.Collator;

import java.util.HashMap;
import java.util.Map;

public class Query2Collator implements Collator<Map.Entry<Tuple<String, String>, Integer>, Map<String, Tuple<String, Integer>>> {
    @Override
    public Map<String, Tuple<String, Integer>> collate(Iterable<Map.Entry<Tuple<String, String>, Integer>> values ) {
        Map<String, Tuple<String, Integer>> map = new HashMap<>();
        for (Map.Entry<Tuple<String, String>, Integer> entry : values) {
            if(!map.containsKey(entry.getKey().getFirst())) {
                map.put(entry.getKey().getFirst(), new Tuple<>(entry.getKey().getSecond(), entry.getValue()));
            }
            else if(map.get(entry.getKey().getFirst()).getSecond() < entry.getValue()) {
                map.put(entry.getKey().getFirst(), new Tuple<>(entry.getKey().getSecond(), entry.getValue()));
            }
        }
        return map;
    }
}
