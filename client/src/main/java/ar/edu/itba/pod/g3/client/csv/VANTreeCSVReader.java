package ar.edu.itba.pod.g3.client.csv;

import ar.edu.itba.pod.g3.client.TreeData;
import ar.edu.itba.pod.g3.client.exceptions.MalformedCSVException;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Parses the data from Toronto Tree Data
 */
public class VANTreeCSVReader {
    public static void readCsv(Consumer<TreeData> dataConsumer, String path) throws IOException, MalformedCSVException {
        CSVReader reader = new CSVReader(new FileReader(path), ';');
        // discard header
        reader.readNext();
        String[] line;
        while ((line = reader.readNext()) != null) {
            Optional<TreeData> treeData = parseLine(line);
            treeData.ifPresent(dataConsumer);
        }

    }

    // Comuna: 2 calle: 4 cientif: 7 diam: 10 BUE
    // Calle: 2 cientif: 7 comuna 13 diam: 16 VAN
    private static Optional<TreeData> parseLine(String[] line) throws MalformedCSVException {
        if (line.length != 19)
            throw new MalformedCSVException();
        String neighbourhood  = line[12];
        String streetName     = line[2];
        String scientificName = line[6];
        double diameter       = Double.parseDouble(line[15]);
        TreeData treeData = new TreeData(neighbourhood, streetName, scientificName, diameter, "VAN");
        return Optional.of(treeData);
    }
}
