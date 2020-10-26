package ar.edu.itba.pod.g3.api.query3;

import ar.edu.itba.pod.g3.api.models.Tuple;
import com.hazelcast.mapreduce.Collator;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Query3Collator implements Collator<Map.Entry<String, Double>, Map<String, Double>> {

    private int n;

    public Query3Collator(int n) {
        this.n = n;
    }

    @Override
    public Map<String, Double> collate(Iterable<Map.Entry<String, Double>> values) {
        Map<String, Double> map = new HashMap<>();
        for (Map.Entry<String, Double> entry : values) {
            map.put(entry.getKey(), entry.getValue());
        }

        List<Map.Entry<String, Double>> list = map.entrySet().stream().sorted(new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        }).collect(Collectors.toList());

        return list.subList(0, n).stream().collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }
}
