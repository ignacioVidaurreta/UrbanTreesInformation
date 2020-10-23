package ar.edu.itba.pod.g3.client;

import java.io.Serializable;

public class NeighbourhoodData implements Serializable {

    private final String    neighbourhood;
    private final int       population;

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
}
