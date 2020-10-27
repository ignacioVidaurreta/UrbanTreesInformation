package ar.edu.itba.pod.g3.api.query4;

import ar.edu.itba.pod.g3.api.models.Tuple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Query4ReducerFactory implements ReducerFactory<String, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(String key) {
        return new ar.edu.itba.pod.g3.api.query4.Query4ReducerFactory.Query4Reducer();
    }

    private class Query4Reducer extends Reducer<Integer, Integer> {
        private volatile int sum;

        @Override
        public void beginReduce () {
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
