package ar.edu.itba.pod.g3.api.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

/**
 * Represents a single row of the neighbourhood's trees data csv
 * */
public class TreeData implements DataSerializable {

    private String neighbourhood;
    private String streetName;
    private String scientificName;
    private double diameter;
    private String city;


    public TreeData(String neighbourhood, String streetName, String scientificName, double diameter, String city) {
        this.neighbourhood  = neighbourhood;
        this.streetName     = streetName;
        this.scientificName = scientificName;
        this.diameter       = diameter;
        this.city           = city;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public double getDiameter() {
        return diameter;
    }

    @Override
    public String toString() {
        return String.format(
                "Neighbourhood: %s, Street name: %s, Scientific name: %s, diameter: %f",
                    neighbourhood, streetName, scientificName, diameter
                );
    }

    public String getCity() {
        return city;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(neighbourhood);
        objectDataOutput.writeUTF(streetName);
        objectDataOutput.writeUTF(scientificName);
        objectDataOutput.writeUTF(String.format("%f",diameter));
        objectDataOutput.writeUTF(city);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        this.neighbourhood  = objectDataInput.readUTF();
        this.streetName     = objectDataInput.readUTF();
        this.scientificName = objectDataInput.readUTF();
        this.diameter       = Double.parseDouble(objectDataInput.readUTF());
        this.city           = objectDataInput.readUTF();
    }
}
