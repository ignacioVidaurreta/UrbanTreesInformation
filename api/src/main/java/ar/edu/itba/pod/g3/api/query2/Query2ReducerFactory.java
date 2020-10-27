package ar.edu.itba.pod.g3.api.query2;

import ar.edu.itba.pod.g3.api.models.Tuple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashMap;
import java.util.Map;

public class Query2ReducerFactory implements ReducerFactory<Tuple<String, String>, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(Tuple<String, String> key) {
        return new Query2Reducer();
    }

    private class Query2Reducer extends Reducer<Integer, Integer> {
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
