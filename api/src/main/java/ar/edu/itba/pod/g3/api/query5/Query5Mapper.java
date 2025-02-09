package ar.edu.itba.pod.g3.api.query5;

import ar.edu.itba.pod.g3.api.models.TreeData;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class Query5Mapper implements Mapper<String, TreeData, String, Integer> {
    private static final Integer ONE = 1;

    @Override
    public void map(String s, TreeData treeData, Context<String, Integer> context) {
        context.emit(treeData.getNeighbourhood(), ONE);
    }
}
