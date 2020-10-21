package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.client.exceptions.InvalidPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static ar.edu.itba.pod.g3.client.util.PropertyParser.parseArguments;
import static ar.edu.itba.pod.g3.client.util.PropertyParser.validateFile;
import static ar.edu.itba.pod.g3.client.util.PropertyParser.validateCity;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private final List<String> ipAddresses;
    private final String city;
    private final String inputFile;
    private final String outputFile;

    public Client(String city, List<String> ipAddresses, String inputFile, String outputFile) throws InvalidPropertyException {
        this.city = validateCity(city);
        this.ipAddresses = ipAddresses;
        this.inputFile = validateFile(inputFile, "inPath");
        this.outputFile = validateFile(outputFile, "outPath");
    }

    public static void main(String[] args) {
        logger.info("UrbanTreesInformation Client Starting ...");
        Properties arguments = System.getProperties();

        Optional<Client> maybeClient = parseArguments(arguments);
        if (!maybeClient.isPresent())
            return;

        Client client = maybeClient.get();

        logger.info(String.format("Created client with City: %s and IP Addresses: %s\n Input File: %s, Output File: %s",
                client.getCity(),
                client.getIpAddresses().toString(),
                client.getInputFile(),
                client.getOutputFile()
        ));
    }


    public String getInputFile() {
        return inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public List<String> getIpAddresses() {
        return ipAddresses;
    }

    public String getCity() {
        return city;
    }
}
