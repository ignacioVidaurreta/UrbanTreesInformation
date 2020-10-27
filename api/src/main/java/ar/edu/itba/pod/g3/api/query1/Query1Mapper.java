package ar.edu.itba.pod.g3.api.query1;

import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.pod.g3.api.models.TreeData;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class Query1Mapper implements Mapper<String, TreeData, String, Integer> {

    private static final Integer ONE = 1;
    private final Map<String, Integer> neighbourhoodData = new HashMap<>();

    public Query1Mapper(IMap<String, Integer>neighbourhoodData) {
        neighbourhoodData.forEach(this.neighbourhoodData::put);
    }

    @Override
    public void map(String key, TreeData treeData, Context<String, Integer> context) {
        if(neighbourhoodData.containsKey(treeData.getNeighbourhood()))
            context.emit(treeData.getNeighbourhood(), ONE);
    }
}
