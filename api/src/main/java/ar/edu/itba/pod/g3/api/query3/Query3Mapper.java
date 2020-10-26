package ar.edu.itba.pod.g3.api.query3;

import ar.edu.itba.pod.g3.api.models.TreeData;
import ar.edu.itba.pod.g3.api.models.Tuple;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class Query3Mapper implements Mapper<String, TreeData, String, Double> {

    private static final Integer ONE = 1;

    @Override
    public void map(String key, TreeData treeData, Context<String, Double> context) {
        context.emit(treeData.getScientificName(), treeData.getDiameter());
    }
}
