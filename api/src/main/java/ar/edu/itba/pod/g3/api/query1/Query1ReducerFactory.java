package ar.edu.itba.pod.g3.api.query1;

import ar.edu.itba.pod.g3.api.models.Tuple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Query1ReducerFactory implements ReducerFactory<String, Integer, Integer> {
    @Override
    public Reducer<Integer, Integer> newReducer(String key) {
        return new Query1Reducer();
    }

    private static class Query1Reducer extends Reducer<Integer, Integer> {

        public Query1Reducer() {
        }

        private volatile int sum;
        
        @Override
        public void beginReduce() {
            sum = 0;
        }

        @Override
        public void reduce(Integer value) {
            sum += value;
        }
        @Override
        public Integer finalizeReduce() {
            return sum;
        }
    }
}
