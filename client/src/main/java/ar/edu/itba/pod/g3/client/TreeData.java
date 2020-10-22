package ar.edu.itba.pod.g3.client;

/**
 * Represents a single row of the neighbourhood's trees data csv
 * */
public class TreeData {

    private final String neighbourhood;
    private final String streetName;
    private final String scientificName;
    private final double diameter;
    private final String city;


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
}
