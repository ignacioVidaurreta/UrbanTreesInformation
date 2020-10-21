package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.client.csv.NeighbourhoodCSVReader;
import ar.edu.itba.pod.g3.client.exceptions.InvalidParametersException;
import ar.edu.itba.pod.g3.client.exceptions.MalformedCSVException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private final List<String> ipAddresses;
    private final String city;
    private final String inputDirectory;
    private final String outputDirectory;

    Client(String city, List<String> ipAddresses, String inputDirectory, String outputDirectory){
        this.city        = city;
        this.ipAddresses = ipAddresses;
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
    }

    public static void main(String[] args) throws IOException, MalformedCSVException, InvalidParametersException {
        logger.info("UrbanTreesInformation Client Starting ...");
        Properties arguments = System.getProperties();

        Optional<Client> maybeClient = parseArguments(arguments);
        if(! maybeClient.isPresent())
            return;

        Client client = maybeClient.get();

        logger.info(String.format("Created client with City: %s and IP Addresses: %s\n Input File: %s, Output File: %s",
                client.getCity(),
                client.getIpAddresses().toString(),
                client.getInputDirectory(),
                client.getOutputDirectory()
                ));

        // Parse the required data for the queries
        List<NeighbourhoodData> neighbourhoodData = new LinkedList<>();
        List<TreeData> neighbourhoodTreeData = new LinkedList<>();
        NeighbourhoodCSVReader.readCsv(neighbourhoodData::add, buildNeighbourhoodCSVPath(client));



    }

    private static String buildNeighbourhoodCSVPath(Client client) {
        return String.format( "%s/barrios%s.csv",client.getInputDirectory(), client.getCity());
    }

    private static Optional<Client> parseArguments(Properties arguments) throws InvalidParametersException {
        Set<String> validCities = new HashSet<>(Arrays.asList("BUE", "VAN"));
        if( ! (arguments.containsKey("city") && validCities.contains(arguments.getProperty("city"))) ) {
            logger.error("Invalid -Dcity argument");
            throw new InvalidParametersException("Invalid or missing -Dcity argument");
        }

        if (! (arguments.containsKey("addresses"))){
            logger.error("Invalid -Daddresses argument");
            throw new InvalidParametersException("must provide a -Daddress");
        }

        if (! (arguments.containsKey("inPath") && fileExists(arguments.getProperty("inPath")))){
            logger.error("Invalid -DinPath argument");
            throw new InvalidParametersException(String.format("directory %s does not exist", arguments.getProperty("inPath")));
        }

        if (! (arguments.containsKey("outPath") && fileExists(arguments.getProperty("outPath")))){
            logger.error("Invalid -DoutPath argument");
            throw new InvalidParametersException(String.format("directory %s does not exist", arguments.getProperty("outPath")));
        }

        return Optional.of(new Client(
                arguments.getProperty("city"),
                Arrays.asList(arguments.getProperty("addresses").split(";")),
                arguments.getProperty("inPath"),
                arguments.getProperty("outPath")
        ));
    }

    private static boolean fileExists(String path){
        Path filePath = Paths.get(path);

        return Files.exists(filePath);
    }

    public String getInputDirectory() {
        return inputDirectory;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public List<String> getIpAddresses() {
        return ipAddresses;
    }

    public String getCity() {
        return city;
    }
}
