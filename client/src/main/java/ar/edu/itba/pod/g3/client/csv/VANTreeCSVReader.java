package ar.edu.itba.pod.g3.client.csv;

import ar.edu.itba.pod.g3.client.TreeData;
import ar.edu.itba.pod.g3.client.exceptions.MalformedCSVException;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Parses the data from Vancouver Tree Data
 */
public class VANTreeCSVReader {
    public static void readCsv(Consumer<TreeData> dataConsumer, String path) throws IOException, MalformedCSVException {
        CSVReader reader = new CSVReader(new FileReader(path), ';');
        // discard header
        reader.readNext();
        String[] line;
        while ((line = reader.readNext()) != null) {
            TreeData treeData = parseLine(line);
            dataConsumer.accept(treeData);
        }

    }

    public static void readCsv(Consumer<TreeData> dataConsumer, String path, Predicate<TreeData> condition) throws IOException, MalformedCSVException {
        CSVReader reader = new CSVReader(new FileReader(path), ';');
        // discard header
        reader.readNext();
        String[] line;
        while ((line = reader.readNext()) != null) {
            TreeData treeData = parseLine(line);
            if(condition.test(treeData))
                dataConsumer.accept(treeData);
        }

    }

    private static TreeData parseLine(String[] line) throws MalformedCSVException {
        if (line.length != 19)
            throw new MalformedCSVException();
        String neighbourhood  = line[12];
        String streetName     = line[2];
        String scientificName = line[6];
        double diameter       = Double.parseDouble(line[15]);
        return new TreeData(neighbourhood, streetName, scientificName, diameter, "VAN");

    }
}
