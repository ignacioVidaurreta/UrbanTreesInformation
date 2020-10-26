package ar.edu.itba.pod.g3.api.query5;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Query5ReducerFactory implements ReducerFactory<String, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(String s) {
        return new Query5Reducer();
    }

    private class Query5Reducer extends Reducer<Integer, Integer> {
        private volatile int amountOfTrees;

        @Override
        public void beginReduce() {
            amountOfTrees = 0;
        }

        @Override
        public void reduce(Integer amountToAdd) {
            amountOfTrees += amountToAdd;
        }

        @Override
        public Integer finalizeReduce() {
            return amountOfTrees / 1000;
        }
    }
}
