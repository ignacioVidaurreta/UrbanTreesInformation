package ar.edu.itba.pod.g3.client.csv;

import java.io.FileReader;

import ar.edu.itba.pod.g3.client.exceptions.MalformedCSVException;
import com.opencsv.CSVReader ;

import java.io.IOException;
import java.util.function.Consumer;

import ar.edu.itba.pod.g3.api.models.NeighbourhoodData;

public class NeighbourhoodCSVReader {

    public static void readCsv(Consumer<NeighbourhoodData> dataConsumer, String path) throws IOException, MalformedCSVException {
        CSVReader reader = new CSVReader(new FileReader(path), ';');
        // discard header
        reader.readNext();
        String[] line;
        while ((line = reader.readNext()) != null) {
            NeighbourhoodData neighbourhoodData = parseLine(line);
            dataConsumer.accept(neighbourhoodData);
        }

    }

    private static NeighbourhoodData parseLine(String[] line) throws MalformedCSVException {
        if (line.length != 2)
            throw new MalformedCSVException();
        String neighbourhood = line[0];
        int population = Integer.parseInt(line[1]);
        return new NeighbourhoodData(neighbourhood, population);

    }
}
