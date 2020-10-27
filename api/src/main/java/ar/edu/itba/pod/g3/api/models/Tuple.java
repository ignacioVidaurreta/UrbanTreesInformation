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

    @Override
    public String toString() {
        return "<" + t.toString() + "," + u.toString() + ">";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tuple other = (Tuple) obj;
        if (u == null) {
            if (other.u != null)
                return false;
        }
        if (t == null) {
            return other.t == null;
        }
        else return t.equals(other.t) && u.equals(other.u);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((t == null) ? 0 : t.hashCode());
        result = prime * result
                + ((u == null) ? 0 : u.hashCode());
        return result;
    }
}
