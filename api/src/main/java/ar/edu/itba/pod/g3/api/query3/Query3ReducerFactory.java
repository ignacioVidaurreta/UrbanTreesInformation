package ar.edu.itba.pod.g3.api.query3;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Query3ReducerFactory implements ReducerFactory<String, Double, Double> {

    @Override
    public Reducer<Double, Double> newReducer(String key) {
        return new Query3Reducer();
    }

    private class Query3Reducer extends Reducer<Double, Double> {
        private volatile double sum;
        private volatile double cant;
        
        @Override
        public void beginReduce () {
            sum = 0;
            cant = 0;
        }
        @Override
        public void reduce(Double value) {
            sum += value;
            cant++;
        }
        @Override
        public Double finalizeReduce() {
            return sum/cant;
        }
    }
}
