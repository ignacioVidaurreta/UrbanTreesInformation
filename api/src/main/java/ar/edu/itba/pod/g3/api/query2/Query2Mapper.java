package ar.edu.itba.pod.g3.api.query2;

import ar.edu.itba.pod.g3.api.models.TreeData;
import ar.edu.itba.pod.g3.api.models.Tuple;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Query2Mapper implements Mapper<String, TreeData, Tuple<String, String>, Integer> {

    private static final Integer ONE = 1;
    private final Set<String> neighbourhoods;

    public Query2Mapper(Set<String> neighbourhoods) {
        this.neighbourhoods = neighbourhoods;
    }

    @Override
    public void map(String key, TreeData treeData, Context<Tuple<String, String>, Integer> context) {
        if (neighbourhoods.contains(treeData.getNeighbourhood())) {
            context.emit(new Tuple<>(treeData.getNeighbourhood(), treeData.getStreetName()), ONE);
        }
    }
}
