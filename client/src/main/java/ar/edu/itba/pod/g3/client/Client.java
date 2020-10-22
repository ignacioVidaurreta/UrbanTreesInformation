package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.client.csv.NeighbourhoodCSVReader;
import ar.edu.itba.pod.g3.client.csv.VANTreeCSVReader;
import ar.edu.itba.pod.g3.client.exceptions.InvalidPropertyException;
import ar.edu.itba.pod.g3.client.exceptions.MalformedCSVException;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import ar.edu.itba.pod.g3.client.exceptions.InvalidPropertyException;

import java.util.*;

import static ar.edu.itba.pod.g3.client.util.PropertyParser.*;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private final List<String> ipAddresses;
    private final String city;
    private final String inputDirectory;
    private final String outputDirectory;
    private final int query;

    public Client(String city, List<String> ipAddresses, String inputDirectory, String outputDirectory, int query) throws InvalidPropertyException {
        this.city = validateCity(city);
        this.ipAddresses = ipAddresses;
        this.inputDirectory = validateDirectory(inputDirectory, "inPath");
        this.outputDirectory = validateDirectory(outputDirectory, "outPath");
        this.query = query;

    }

    public static void main(String[] args) throws IOException, MalformedCSVException {
        logger.info("UrbanTreesInformation Client Starting ...");
        final Properties arguments = System.getProperties();

        final Optional<Client> maybeClient = parseArguments(arguments);
        if (!maybeClient.isPresent())
            return;

        final Client client = maybeClient.get();

        logger.info(String.format("Created client with City: %s and IP Addresses: %s\n Input File: %s, Output File: %s. Query=%d",
                client.getCity(),
                client.getIpAddresses().toString(),
                client.getInputDirectory(),
                client.getOutputDirectory(),
                client.getQuery()
                ));

        // Parse the required data for the queries
        List<NeighbourhoodData> neighbourhoodData = new LinkedList<>();
        List<TreeData> neighbourhoodTreeData = new LinkedList<>();
        NeighbourhoodCSVReader.readCsv(neighbourhoodData::add, buildNeighbourhoodCSVPath(client));
        System.out.println(neighbourhoodData.toString());
        List<TreeData> treeListVAN = new LinkedList<>();

        VANTreeCSVReader.readCsv(treeListVAN::add, buildTreesCSVPath(client));
        System.out.println(treeListVAN.toString());

        final ClientConfig clientConfig = initializeConfig(client);
        final HazelcastInstance hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);

        final Map<String, String> datos = hazelcastClient.getMap("materias");

        datos.put("72.42", "POD");

        System.out.println(String.format("%d Datos en el cluster", datos.size()));

        datos.keySet().forEach(k -> System.out.println(String.format("Datos con key %s= %s", k, datos.get(k))));
    }

    private static ClientConfig initializeConfig(final Client client) {
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.setGroupConfig(new GroupConfig("g3", "n4ch170c4p0"));
        final ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
        client.getIpAddresses().forEach(networkConfig::addAddress);
        return clientConfig;
    }

    /* package */ static String buildNeighbourhoodCSVPath(Client client) {
        return String.format( "%s/barrios%s.csv",client.getInputDirectory(), client.getCity());
    }

    /* package */ static String buildTreesCSVPath(Client client){
        return String.format( "%s/arboles%s.csv", client.getInputDirectory(), client.getCity());
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

    public int getQuery() {
        return query;
    }
}
