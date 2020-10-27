package ar.edu.itba.pod.g3.api.models;

<<<<<<< HEAD
import java.io.Serializable;

public class NeighbourhoodData implements Serializable {
=======
import java.io.IOException;
>>>>>>> 6dae775... Add initial version

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

public class NeighbourhoodData implements DataSerializable {

    private String    neighbourhood;
    private int       population;

    public NeighbourhoodData(String neighbourhood, int population) {
        this.neighbourhood  = neighbourhood;
        this.population     = population;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public int getPopulation() {
        return population;
    }

    @Override
    public String toString() {
        return String.format("Neighbourhood %s with population of %d", neighbourhood, population);
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(neighbourhood);
        objectDataOutput.writeInt(population);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        this.neighbourhood = objectDataInput.readUTF();
        this.population = objectDataInput.readInt();
    }
}
