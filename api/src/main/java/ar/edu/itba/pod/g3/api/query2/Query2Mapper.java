package ar.edu.itba.pod.g3.api.query2;

import ar.edu.itba.pod.g3.api.models.TreeData;
import ar.edu.itba.pod.g3.api.models.Tuple;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class Query2Mapper implements Mapper<String, TreeData, Tuple<String, String>, Integer> {

    private static final Integer ONE = 1;

    @Override
    public void map(String key, TreeData treeData, Context<Tuple<String, String>, Integer> context) {
        context.emit(new Tuple<>(treeData.getNeighbourhood(),treeData.getStreetName()), ONE);
    }
}
