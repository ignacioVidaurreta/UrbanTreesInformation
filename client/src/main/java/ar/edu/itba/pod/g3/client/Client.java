package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.client.csv.NeighbourhoodCSVReader;
import ar.edu.itba.pod.g3.client.csv.BUETreeCSVReader;
import ar.edu.itba.pod.g3.client.csv.VANTreeCSVReader;
import ar.edu.itba.pod.g3.client.exceptions.InvalidPropertyException;
import ar.edu.itba.pod.g3.client.exceptions.MalformedCSVException;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.util.*;

import static ar.edu.itba.pod.g3.client.util.PropertyParser.*;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private final List<String> ipAddresses;
    private final String city;
    private final String inputDirectory;
    private final String outputDirectory;
    private final int query;

    // QUERY SPECIFIC ARGUMENTS
    private int min;
    private String name;

    public Client(String city, List<String> ipAddresses, String inputDirectory, String outputDirectory, int query) throws InvalidPropertyException {
        this.city = validateCity(city);
        this.ipAddresses = ipAddresses;
        this.inputDirectory = validateDirectory(inputDirectory, "inPath");
        this.outputDirectory = validateDirectory(outputDirectory, "outPath");
        this.query = validateQuery(query);

    }

    public void executeQuery(){

        ClientConfig config = initializeConfig(this);
        final HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(config);
        switch (this.query){
            case 1:
            case 2:
            case 3:
                break;
            case 4:
                runMinTreeQuery(hazelcastInstance);
                break;

        }
    }


    private void runMinTreeQuery(HazelcastInstance hazelcastInstance){
        IList<TreeData> treeDataList =  hazelcastInstance.getList("g3-treeData");
        try {
            BUETreeCSVReader.readCsv(treeDataList::add, buildBUETreesCSVPath(this), (TreeData data)-> data.getScientificName().equals(this.name));
            VANTreeCSVReader.readCsv(treeDataList::add, buildVANTreesCSVPath(this), (TreeData data)-> data.getScientificName().equals(this.name));

            System.out.println(Arrays.toString(treeDataList.toArray()));
        }catch (MalformedCSVException | IOException ex){
            logger.error(String.format("Parsing error: %s", ex.getMessage()));
        }
    }

    public static void main(String[] args) throws IOException, MalformedCSVException {
        logger.info("UrbanTreesInformation Client Starting ...");
        final Properties arguments = System.getProperties();

        final Optional<Client> maybeClient = parseArguments(arguments);
        if (!maybeClient.isPresent())
            return;

        final Client client = maybeClient.get();
        boolean hasQuerySpecificArgs = setQuerySpecificArguments(arguments, client);

        if(!hasQuerySpecificArgs)
            return;

        logger.info(String.format("Created client with City: %s and IP Addresses: %s\n Input File: %s, Output File: %s. Query=%d",
                client.getCity(),
                client.getIpAddresses().toString(),
                client.getInputDirectory(),
                client.getOutputDirectory(),
                client.getQuery()
                ));

        client.executeQuery();
    }

    private static ClientConfig initializeConfig(final Client client) {
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.setGroupConfig(new GroupConfig("g3", "n4ch170c4p0"));
        final ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
        client.getIpAddresses().forEach(networkConfig::addAddress);
        return clientConfig;
    }

    private static String buildNeighbourhoodCSVPath(Client client) {
        return String.format( "%s/barrios%s.csv",client.getInputDirectory(), client.getCity());
    }

    /* package */ static String buildTreesCSVPath(Client client){
        return String.format( "%s/arboles%s.csv", client.getInputDirectory(), client.getCity());
    }
    /* package */ static String buildVANTreesCSVPath(Client client){
        return String.format("%s/arbolesVAN.csv", client.getInputDirectory());
    }

    /* package */ static String buildBUETreesCSVPath(Client client){
        return String.format("%s/arbolesBUE.csv", client.getInputDirectory());
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

    public int getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = Integer.parseInt(min);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
