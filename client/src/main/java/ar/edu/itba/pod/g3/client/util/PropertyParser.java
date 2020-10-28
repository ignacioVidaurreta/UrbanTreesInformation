package ar.edu.itba.pod.g3.client.util;

import ar.edu.itba.pod.g3.client.Client;
import ar.edu.itba.pod.g3.client.exceptions.InvalidPropertyException;
import ar.edu.itba.pod.g3.client.exceptions.RequiredPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PropertyParser {
    private static Logger logger = LoggerFactory.getLogger(PropertyParser.class);
    public static Optional<Client> parseArguments(Properties arguments){
        try {
            Optional<String> maybeCity = Optional.ofNullable(arguments.getProperty("city"));
            Optional<String> maybeAddresses = Optional.ofNullable(arguments.getProperty("addresses"));
            Optional<String> maybeInPath = Optional.ofNullable(arguments.getProperty("inPath"));
            Optional<String> maybeOutPath = Optional.ofNullable(arguments.getProperty("outPath"));
            Optional<String> maybeQuery   = Optional.ofNullable(arguments.getProperty("query"));

            String city = maybeCity.orElseThrow(() -> new RequiredPropertyException("city"));
            String[] addresses = maybeAddresses.orElseThrow(
                    () -> new RequiredPropertyException("addresses")
            ).split(";");
            String inPath = maybeInPath.orElseThrow(() -> new RequiredPropertyException("inPath"));
            String outPath = maybeOutPath.orElseThrow(() -> new RequiredPropertyException("outPath"));
            String query   = maybeQuery.orElseThrow( () -> new RequiredPropertyException("query"));

            return Optional.of(new Client(
                    city,
                    Arrays.asList(addresses),
                    inPath,
                    outPath,
                    Integer.parseInt(query))
            );
        } catch (RequiredPropertyException | InvalidPropertyException ex) {
            logger.error(String.format("Error when parsing arguments: %s", ex.getMessage()));
            return Optional.empty();
        }
    }

    public static boolean setQuerySpecificArguments(Properties arguments, Client client){
        try {
            switch (client.getQuery()) {
                case 1:
                case 2:
                    Optional<String> maybeMinQ2 = Optional.ofNullable(arguments.getProperty("min"));
                    client.setMin(maybeMinQ2.orElseThrow(() -> new RequiredPropertyException("min")));
                    break;
                case 3:
                    Optional<String> maybeN = Optional.ofNullable(arguments.getProperty("n"));
                    client.setN(maybeN.orElseThrow(() -> new RequiredPropertyException("n")));
                    break;
                case 4:
                    Optional<String> maybeMin = Optional.ofNullable(arguments.getProperty("min"));
                    Optional<String> maybeName = Optional.ofNullable(arguments.getProperty("name"));

                    client.setMin(maybeMin.orElseThrow(() -> new RequiredPropertyException("min")));
                    client.setName(maybeName.orElseThrow(()-> new RequiredPropertyException("name")));
                    break;
                case 5:
                    // this query requires no specific arguments as of today
                    break;
                default:
                    throw new IllegalArgumentException("Expected query to be 1, 2, 3, 4 or 5.\n");
            }
        } catch (RequiredPropertyException rex) {
            logger.error(String.format("Missing required argument for query %d, %s", client.getQuery(), rex.getMessage()));
            return false;
        }
        return true;
    }

    public static String validateCity(String city) throws InvalidPropertyException {
        Set<String> validCities = new HashSet<>(Arrays.asList("BUE", "VAN"));
        if (!validCities.contains(city))
            throw new InvalidPropertyException(city, "city");
        return city;
    }

    public static String validateDirectory(String path, String property) throws InvalidPropertyException{
        Path filePath = Paths.get(path);
        if (! Files.exists(filePath))
            throw new InvalidPropertyException(path, property);

        return path;
    }

    public static int validateQuery(int query) throws InvalidPropertyException{
        if(query < 1 || query > 5)
            throw new InvalidPropertyException("Query must be a number between 1 and 5");

        return query;
    }

}
