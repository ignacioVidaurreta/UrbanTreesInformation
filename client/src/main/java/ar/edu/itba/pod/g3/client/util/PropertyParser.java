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
            Optional<String> maybeMin = Optional.ofNullable(arguments.getProperty("min"));
            Optional<String> maybeN = Optional.ofNullable(arguments.getProperty("n"));

            String city = maybeCity.orElseThrow(() -> new RequiredPropertyException("city"));
            String[] addresses = maybeAddresses.orElseThrow(
                    () -> new RequiredPropertyException("addresses")
            ).split(";");
            String inPath = maybeInPath.orElseThrow(() -> new RequiredPropertyException("inPath"));
            String outPath = maybeOutPath.orElseThrow(() -> new RequiredPropertyException("outPath"));
            String query   = maybeQuery.orElseThrow( () -> new RequiredPropertyException("query"));

            String min = null;
            if(Integer.parseInt(query) == 2)
                min = maybeMin.orElseThrow( () -> new RequiredPropertyException("min"));

            String n = null;
            if(Integer.parseInt(query) == 3)
                n = maybeN.orElseThrow( () -> new RequiredPropertyException("n"));

            //Query2
            if(Integer.parseInt(query) == 2 && min != null) {
                return Optional.of(new Client(
                        city,
                        Arrays.asList(addresses),
                        inPath,
                        outPath,
                        Integer.parseInt(query),
                        Integer.parseInt(min)
                ));
            }

            //Query3
            if(Integer.parseInt(query) == 3 && n != null) {
                return Optional.of(new Client(
                        city,
                        Arrays.asList(addresses),
                        inPath,
                        outPath,
                        Integer.parseInt(query),
                        Integer.parseInt(n)
                ));
            }

            return Optional.of(new Client(
                    city,
                    Arrays.asList(addresses),
                    inPath,
                    outPath,
                    Integer.parseInt(query)

            ));
        }catch (RequiredPropertyException | InvalidPropertyException ex){
            logger.error(String.format("Error when parsing arguments: %s", ex.getMessage()));
            return Optional.empty();

        }
    }

    public static String validateCity(String city) throws InvalidPropertyException {
        Set<String> validCities = new HashSet<>(Arrays.asList("BUE", "VAN"));
        if (!validCities.contains(city))
            throw new InvalidPropertyException(city, "city");
        return city;
    }

    public static String validateDirectory(String path, String property) throws InvalidPropertyException{
        Path filePath = Paths.get(path);
        if(! Files.exists(filePath))
            throw new InvalidPropertyException(path, property);

        return path;
    }

}
