package ar.edu.itba.pod.g3.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.Option;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private final List<String> ipAddresses;
    private final String city;
    private final String inputFile;
    private final String outputFile;

    Client(String city, List<String> ipAddresses, String inputFile, String outputFile){
        this.city        = city;
        this.ipAddresses = ipAddresses;
        this.inputFile   = inputFile;
        this.outputFile  = outputFile;
    }

    public static void main(String[] args) {
        logger.info("UrbanTreesInformation Client Starting ...");
        Properties arguments = System.getProperties();

        Optional<Client> maybeClient = parseArguments(arguments);
        if(maybeClient.isEmpty())
            return;

        Client client = maybeClient.get();

        logger.info(String.format("Created client with City: %s and IP Addresses: %s\n Input File: %s, Output File: %s",
                client.getCity(),
                client.getIpAddresses().toString(),
                client.getInputFile(),
                client.getOutputFile()
                ));
    }

    private static Optional<Client> parseArguments(Properties arguments){
        Set<String> validCities = new HashSet<>(List.of("BUE", "VAN"));
        if( ! (arguments.containsKey("city") && validCities.contains(arguments.getProperty("city"))) ) {
            logger.error("Invalid -Dcity argument");
            return Optional.empty();
        }

        if (! (arguments.containsKey("addresses"))){
            logger.error("Invalid -Daddresses argument");
            return Optional.empty();
        }

        if (! (arguments.containsKey("inPath") && fileExists(arguments.getProperty("inPath")))){
            logger.error("Invalid -DinPath argument");
            return Optional.empty();
        }

        if (! (arguments.containsKey("outPath") && fileExists(arguments.getProperty("outPath")))){
            logger.error("Invalid -DoutPath argument");
            return Optional.empty();
        }

        return Optional.of(new Client(
                arguments.getProperty("city"),
                List.of(arguments.getProperty("addresses").split(";")),
                arguments.getProperty("inPath"),
                arguments.getProperty("outPath")
        ));
    }

    private static boolean fileExists(String path){
        Path filePath = Paths.get(path);

        return Files.exists(filePath);
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
