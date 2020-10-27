package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.api.models.TreeData;
import ar.edu.itba.pod.g3.api.models.Tuple;
import ar.edu.itba.pod.g3.api.query2.Query2Collator;
import ar.edu.itba.pod.g3.api.query2.Query2Mapper;
import ar.edu.itba.pod.g3.api.query2.Query2ReducerFactory;
import ar.edu.itba.pod.g3.api.query4.Query4Collator;
import ar.edu.itba.pod.g3.api.query4.Query4Mapper;
import ar.edu.itba.pod.g3.api.query4.Query4ReducerFactory;
import ar.edu.itba.pod.g3.client.csv.BUETreeCSVReader;
import ar.edu.itba.pod.g3.client.csv.VANTreeCSVReader;
import ar.edu.itba.pod.g3.client.exceptions.InvalidPropertyException;
import ar.edu.itba.pod.g3.client.exceptions.MalformedCSVException;
import ar.edu.itba.pod.g3.client.util.ResultWriter;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;

import com.hazelcast.core.IList;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Result;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static ar.edu.itba.pod.g3.client.util.PropertyParser.*;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private final List<String> ipAddresses;
    private final String city;
    private final String inputDirectory;
    private final String outputDirectory;
    private final int query;
    private final String resultFilePath;
    private final String timeFilePath;
    private FileWriter timeFile;
    private BufferedWriter timeFileWriter;

    //TODO: (A big one) the logic behind handling the properties parsing in another class makes the constructor
    // a Necessity. It would be ideal that all attributes are static so there are not as many parameters being
    // handled between all methods. This requires a well though refactor.

    // QUERY SPECIFIC ARGUMENTS
    private int min;
    private String name;

    public Client(String city, List<String> ipAddresses, String inputDirectory, String outputDirectory, int query) throws InvalidPropertyException {
        this.city = validateCity(city);
        this.ipAddresses = ipAddresses;
        this.inputDirectory = validateDirectory(inputDirectory, "inPath");
        this.outputDirectory = validateDirectory(outputDirectory, "outPath");
        this.query = validateQuery(query);
        this.resultFilePath = outputDirectory + "/query" + query + ".csv";
        this.timeFilePath = outputDirectory + "/time" + query + ".txt";

    }

    public static void main(String[] args) throws IOException, MalformedCSVException, ExecutionException, InterruptedException {
        logger.info("UrbanTreesInformation Client Starting ...");
        final Properties arguments = System.getProperties();

        final Optional<Client> maybeClient = parseArguments(arguments);
        if (!maybeClient.isPresent())
            return;

        final Client client = maybeClient.get();
        boolean hasQuerySpecificArgs = setQuerySpecificArguments(arguments, client);

        if (!hasQuerySpecificArgs)
            return;

        logger.info(String.format("Created client with City: %s and IP Addresses: %s\n Input File: %s, Output File: %s. Query=%d %s",
                client.getCity(),
                client.getIpAddresses().toString(),
                client.getInputDirectory(),
                client.getOutputDirectory(),
                client.getQuery(),
                client.getQuery() != 2 ? "" : String.format("Min=%d", client.getMin())
        ));

        final ClientConfig clientConfig = initializeConfig(client);
        final HazelcastInstance hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);
        final IList<TreeData> treesList = hazelcastClient.getList("g3-trees");
        setUpOutput(client, treesList);
        client.solveQuery(hazelcastClient, treesList);
        client.timeFileWriter.close();

        logger.info("Client shutting down...");
        hazelcastClient.shutdown();
    }

    private static void setUpOutput(Client client, IList<TreeData> treesList) throws IOException, MalformedCSVException {
        // Time file
        client.timeFile = new FileWriter(client.timeFilePath);
        client.timeFileWriter = new BufferedWriter(client.timeFile);
        // Clear list
        treesList.clear();
        // Load info
        ResultWriter.writeTime(client.timeFileWriter, "Inicio de la lectura del archivo");

        if (client.getCity().equals("BUE")) { // TODO: better check for city
            BUETreeCSVReader.readCsv(treesList::add, buildTreesCSVPath(client));
        } else if (client.getCity().equals("VAN")) {
            VANTreeCSVReader.readCsv(treesList::add, buildTreesCSVPath(client));
        }

        ResultWriter.writeTime(client.timeFileWriter, "Fin de lectura del archivo");
    }

    private void solveQuery(HazelcastInstance hazelcastClient, IList<TreeData> treesList) throws IOException, MalformedCSVException, ExecutionException, InterruptedException {
        ResultWriter.writeTime(this.timeFileWriter, "Inicio del trabajo map/reduce");

        switch (this.getQuery()) {
            //TODO Implement other cases
            case 2:
                streetWithMoreTreesByNeighborhood(hazelcastClient, treesList);
                break;
            case 4:
                runMinTreeQuery(hazelcastClient);
                break;
            default:
                System.out.println("Not implemented");
        }
        ResultWriter.writeTime(this.timeFileWriter, "Fin del trabajo map/reduce");
    }

    private void runMinTreeQuery(HazelcastInstance hazelcastClient) throws InterruptedException, ExecutionException {
        IList<TreeData> treeDataList = hazelcastClient.getList("g3-treeData");
        try {
            if( this.city.equals("BUE"))
                BUETreeCSVReader.readCsv(treeDataList::add, buildBUETreesCSVPath(this), (TreeData data) -> data.getScientificName().equals(this.name));
            else
                VANTreeCSVReader.readCsv(treeDataList::add, buildVANTreesCSVPath(this), (TreeData data) -> data.getScientificName().equals(this.name.toUpperCase()));

            final JobTracker jobTracker = hazelcastClient.getJobTracker("query-4");
            final KeyValueSource<String, TreeData> source = KeyValueSource.fromList(treeDataList);
            Job<String, TreeData> job = jobTracker.newJob(source);
            Map<String, Integer> result = null;
            System.out.println("OH hi...");
            ICompletableFuture<Map<String, Integer>> future = job
                    .mapper(new Query4Mapper())
                    .reducer(new Query4ReducerFactory())
                    .submit(new Query4Collator(this.getMin()));

            result = future.get();
            ResultWriter.writeQuery4Result(result);
        } catch (MalformedCSVException | IOException ex) {
            logger.error(String.format("Parsing error: %s", ex.getMessage()));
        }
    }


    private void streetWithMoreTreesByNeighborhood(HazelcastInstance hazelcastClient, IList<TreeData> treesList) throws IOException, MalformedCSVException, ExecutionException, InterruptedException {
        final JobTracker jobTracker = hazelcastClient.getJobTracker("query-2");
        final KeyValueSource<String, TreeData> source = KeyValueSource.fromList(treesList);

        Job<String, TreeData> job = jobTracker.newJob(source);
        Map<String, Tuple<String, Integer>> result = null;

        ICompletableFuture<Map<String, Tuple<String, Integer>>> future = job
                .mapper(new Query2Mapper())
                .reducer(new Query2ReducerFactory())
                .submit(new Query2Collator(this.getMin()));
        result = future.get();

        ResultWriter.writeQuery2Result(this.resultFilePath, result, this.getCity());
    }

    private static ClientConfig initializeConfig(final Client client) {
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.setGroupConfig(new GroupConfig("g3", "n4ch170c4p0"));
        final ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
        client.getIpAddresses().forEach(networkConfig::addAddress);
        return clientConfig;
    }

    /* package */
    static String buildNeighbourhoodCSVPath(Client client) {
        return String.format("%s/barrios%s.csv", client.getInputDirectory(), client.getCity());
    }

    /* package */
    static String buildTreesCSVPath(Client client) {
        return String.format("%s/arboles%s.csv", client.getInputDirectory(), client.getCity());
    }

    /* package */
    static String buildVANTreesCSVPath(Client client) {
        return String.format("%s/arbolesVAN.csv", client.getInputDirectory());
    }

    /* package */
    static String buildBUETreesCSVPath(Client client) {
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