package ar.edu.itba.pod.g3.api.query5;

import com.hazelcast.mapreduce.Collator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Query5Collator implements Collator<Map.Entry<String, Integer>, Map<Integer, List<String>>> {
    @Override
    public Map<Integer, List<String>> collate(Iterable<Map.Entry<String, Integer>> iterable) {
        return StreamSupport.stream(iterable.spliterator(), true)
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())
                    )
                );
    }
}
