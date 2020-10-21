package ar.edu.itba.pod.g3.client;

/**
 * Represents a single row of the neighbourhood's trees data csv
 * */
public class TreeData {

    private final String neighbourhood;
    private final String streetName;
    private final String scientificName;
    private final double diameter;


    public TreeData(String neighbourhood, String streetName, String scientificName, double diameter) {
        this.neighbourhood  = neighbourhood;
        this.streetName     = streetName;
        this.scientificName = scientificName;
        this.diameter       = diameter;
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
}
