package ar.edu.itba.pod.g3.client.csv;

import ar.edu.itba.pod.g3.api.models.TreeData;
import ar.edu.itba.pod.g3.client.exceptions.MalformedCSVException;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Parses the data from Buenos Aires Tree Data
 */
public class BUETreeCSVReader{

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


    protected static TreeData parseLine(String[] line) throws MalformedCSVException {
        if (line.length != 13)
            throw new MalformedCSVException();
        String neighbourhood  = line[2];
        String streetName     = line[4];
        String scientificName = line[7];
        double diameter       = Double.parseDouble(line[11]);
        return new TreeData(neighbourhood, streetName, scientificName, diameter, "BUE");
    }
}
