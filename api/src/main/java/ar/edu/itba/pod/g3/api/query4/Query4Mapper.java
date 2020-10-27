package ar.edu.itba.pod.g3.api.query4;

import ar.edu.itba.pod.g3.api.models.TreeData;
import ar.edu.itba.pod.g3.api.models.Tuple;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;


public class Query4Mapper implements Mapper<String, TreeData, String, Integer> {

    private String name;
    public Query4Mapper(String name){
        this.name = name;
    }
    private static final Integer ONE = 1;

    @Override
    public void map(String key, TreeData treeData, Context<String, Integer> context) {
        if(treeData.getScientificName().toUpperCase().equals(name.toUpperCase()))
            context.emit(treeData.getNeighbourhood(), ONE);
    }
}
