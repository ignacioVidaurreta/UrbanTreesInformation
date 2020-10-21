package ar.edu.itba.pod.g3.client.csv;

import java.io.FileNotFoundException;
import java.io.FileReader;

import ar.edu.itba.pod.g3.client.exceptions.MalformedCSVException;
import com.opencsv.CSVReader ;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

import ar.edu.itba.pod.g3.client.NeighbourhoodData;

public class NeighbourhoodCSVReader {

    public static void readCsv(Consumer<NeighbourhoodData> dataConsumer, String path) throws IOException, MalformedCSVException {
        CSVReader reader = new CSVReader(new FileReader(path), ';');
        // discard header
        reader.readNext();
        String[] line;
        while ((line = reader.readNext()) != null) {
            Optional<NeighbourhoodData> neighbourhoodData = parseLine(line);
            neighbourhoodData.ifPresent(dataConsumer);
        }

    }

    private static Optional<NeighbourhoodData> parseLine(String[] line) throws MalformedCSVException {
        if (line.length != 2)
            throw new MalformedCSVException();
        String neighbourhood = line[0];
        int population = Integer.parseInt(line[1]);
        NeighbourhoodData neighbourhoodData = new NeighbourhoodData(neighbourhood, population);
        return Optional.of(neighbourhoodData);
    }
}
