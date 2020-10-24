package ar.edu.itba.pod.g3.api.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Tuple<T, U> implements DataSerializable {

    private T t;
    private U u;

    public Tuple() {}

    public Tuple(T t, U u) {
        this.t = t;
        this.u = u;
    }

    public T getFirst() {
        return t;
    }

    public U getSecond() {
        return u;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(t);
        out.writeObject(u);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.t = in.readObject();
        this.u = in.readObject();
    }
}
